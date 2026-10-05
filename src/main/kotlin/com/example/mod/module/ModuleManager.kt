package com.example.mod.module

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.io.File

object ModuleManager {
    private val configDir = File("config/modbase")
    private val configFile = File(configDir, "config.json")
    private val backupFile = File(configDir, "config.json.bak")

    private val gson = GsonBuilder().setPrettyPrinting().create()

    private val modules = LinkedHashMap<String, Module>()

    fun init() {
        //registerModules() // not yet!
        load()
    }

    fun register(module: Module) {
        val id = module.id.lowercase()

        require(id !in modules) {
            "A module with the id '$id' is already registered"
        }

        modules[id] = module
    }


    fun unregister(module: Module) {
        modules.remove(module.id.lowercase())
    }

    fun getModule(id: String): Module? {
        return modules[id.lowercase()]
    }

    fun getModules(): Collection<Module> {
        return modules.values
    }

    fun enable(id: String) {
        getModule(id)?.enable()
    }

    fun disable(id: String) {
        getModule(id)?.disable()
    }

    fun toggle(id: String) {
        getModule(id)?.toggle()
    }

    fun save() {
        configDir.mkdirs()

        if (configFile.exists() && configFile.length() > 0) {
            backup()
        }

        val root = JsonObject()
        val modulesJson = JsonObject()

        for ((id, module) in modules) {
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

            modulesJson.add(id, moduleJson)
        }

        root.add("modules", modulesJson)

        configFile.writer().use { writer ->
            gson.toJson(root, writer)
        }
    }

    fun load() {
        configDir.mkdirs()

        if (!configFile.exists()) {
            configFile.createNewFile()
            return
        }

        if (configFile.length() == 0L) {
            return
        }

        val root = runCatching {
            configFile.reader().use { reader ->
                gson.fromJson(reader, JsonObject::class.java)
            }
        }.getOrElse {
            println("Failed to load module config: ${it.message}")
            return
        }

        val modulesJson = root.getAsJsonObject("modules")
            ?: return

        for ((id, moduleElement) in modulesJson.entrySet()) {
            val module = modules[id] ?: continue

            if (!moduleElement.isJsonObject) {
                continue
            }

            val moduleJson = moduleElement.asJsonObject

            if (moduleJson.has("enabled")) {
                val enabled = runCatching {
                    moduleJson.get("enabled").asBoolean
                }.getOrDefault(false)

                if (enabled) {
                    module.enable()
                } else {
                    module.disable()
                }
            }

            for (setting in module.getSettings()) {
                val element = moduleJson.get(setting.name) ?: continue

                setSettingValue(
                    setting = setting,
                    element = element,
                )
            }
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
            is Boolean -> runCatching {
                element.asBoolean
            }.getOrNull()

            is Int -> runCatching {
                element.asInt
            }.getOrNull()

            is Long -> runCatching {
                element.asLong
            }.getOrNull()

            is Float -> runCatching {
                element.asFloat
            }.getOrNull()

            is Double -> runCatching {
                element.asDouble
            }.getOrNull()

            is String -> runCatching {
                element.asString
            }.getOrNull()

            else -> {
                println(
                    "Unsupported setting type for '${setting.name}': " + setting.default!!::class.simpleName
                )

                return
            }
        }

        if (value == null) {
            println(
                "Failed to load setting '${setting.name}'"
            )

            return
        }

        (setting as Setting<Any?>).set(value)
    }

    private fun backup() {
        if (!configFile.exists()) {
            return
        }

        configFile.copyTo(
            target = backupFile,
            overwrite = true,
        )
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
}
