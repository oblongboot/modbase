package com.example.mod.module

import com.example.mod.utils.Logger
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.io.File

object ConfigManager {
    private val configDir = File("config/modbase")
    private val configFile = File(configDir, "config.json")
    private val backupFile = File(configDir, "config.backup.json")

    private val gson = Gson()

    fun save() {
        configDir.mkdirs()

        if (configFile.exists() && configFile.length() > 0) {
            backup()
        }

        val jsonObj = JsonObject()
        jsonObj.add("modules", createModulesJson())

        configFile.writer().use { writer ->
            gson.toJson(jsonObj, writer)
        }
    }

    fun load() {
        configDir.mkdirs()

        if (!configFile.exists() || configFile.length() == 0L) {
            return
        }

        val config = runCatching {
            configFile.reader().use { reader ->
                gson.fromJson(reader, JsonObject::class.java)
            }
        }.getOrElse {
            Logger.error("Failed to load module config: ${it.message}")
            return
        }

        val modulesJson = config.getAsJsonObject("modules") ?: return

        loadModules(modulesJson)
    }

    fun restoreBackup() {
        if (!backupFile.exists()) {
            return
        }

        backupFile.copyTo(
            target = configFile,
            overwrite = true,
        )
    }

    private fun createModulesJson(): JsonObject {
        val modulesJson = JsonObject()

        for (module in ModuleManager.getModules()) {
            modulesJson.add(
                module.id.lowercase(),
                createModuleJson(module),
            )
        }

        return modulesJson
    }

    private fun createModuleJson(module: Module): JsonObject {
        val moduleJson = JsonObject()

        moduleJson.addProperty(
            "enabled",
            module.enabled,
        )

        for (setting in module.getSettings()) {
            moduleJson.add(
                setting.name,
                gson.toJsonTree(setting.value),
            )
        }

        return moduleJson
    }

    private fun loadModules(modulesJson: JsonObject) {
        for ((id, moduleElement) in modulesJson.entrySet()) {
            val module = ModuleManager.getModule(id) ?: continue

            if (!moduleElement.isJsonObject) {
                continue
            }

            loadModule(
                module = module,
                moduleJson = moduleElement.asJsonObject,
            )
        }
    }

    private fun loadModule(
        module: Module,
        moduleJson: JsonObject,
    ) {
        loadEnabledState(
            module = module,
            moduleJson = moduleJson,
        )

        for (setting in module.getSettings()) {
            val element = moduleJson.get(setting.name) ?: continue

            setSettingValue(
                setting = setting,
                element = element,
            )
        }
    }

    private fun loadEnabledState(
        module: Module,
        moduleJson: JsonObject,
    ) {
        if (!moduleJson.has("enabled")) {
            return
        }

        val enabled = runCatching {
            moduleJson.get("enabled").asBoolean
        }.getOrNull() ?: return

        if (enabled) {
            module.enable()
        } else {
            module.disable()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setSettingValue(
        setting: Setting<*>,
        element: JsonElement,
    ) {
        if (element.isJsonNull) {
            return
        }

        val value = when (setting.default) {
            is Boolean -> runCatching { element.asBoolean }.getOrNull()
            is Int -> runCatching { element.asInt }.getOrNull()
            is Long -> runCatching { element.asLong }.getOrNull()
            is Float -> runCatching { element.asFloat }.getOrNull()
            is Double -> runCatching { element.asDouble }.getOrNull()
            is String -> runCatching { element.asString }.getOrNull()

            else -> {
                Logger.error(
                    "unsupported setting type for '${setting.name}': " + setting.default!!::class.simpleName
                )

                return
            }
        }

        if (value == null) {
            Logger.error("failed to load setting '${setting.name}'")
            return
        }

        (setting as Setting<Any?>).set(value)
    }

    private fun backup() {
        configFile.copyTo(
            target = backupFile,
            overwrite = true,
        )
    }
}
