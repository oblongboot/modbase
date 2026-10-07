package com.example.mod.module

import com.example.mod.module.annotation.AlphaModule
import com.example.mod.module.annotation.BetaModule
import com.example.mod.module.annotation.DevModule
import com.example.mod.module.annotation.ReleaseModule
import com.example.mod.utils.ClassScanner
import com.example.mod.utils.Logger
import net.fabricmc.loader.api.FabricLoader

object ModuleManager {
    private var modState: ModState = ModState.RELEASE
    private val modules = LinkedHashMap<String, Module>()

    internal fun setModState(modState: ModState) {
        this.modState = modState
        Logger.debug("mod state is $modState")
    }

    fun register(module: Module) {
        val id = module.id.lowercase()

        require(id !in modules) {
            "A module with the id '$id' is already registered"
        }

        modules[id] = module
    }

    private fun canUse(clazz: Class<*>): Boolean {
        return when {
            clazz.isAnnotationPresent(DevModule::class.java) -> {
                if (!FabricLoader.getInstance().isDevelopmentEnvironment) return false

                Logger.debug("registering dev module ${clazz.simpleName}")
                true
            }

            clazz.isAnnotationPresent(BetaModule::class.java) -> {
                if (modState != ModState.BETA && modState != ModState.ALPHA) return false

                Logger.debug("registering beta module ${clazz.simpleName}")
                true
            }

            clazz.isAnnotationPresent(AlphaModule::class.java) -> {
                if (modState != ModState.ALPHA) return false

                Logger.debug("registering alpha module ${clazz.simpleName}")
                true
            }

            clazz.isAnnotationPresent(ReleaseModule::class.java) -> {
                Logger.debug("registering release module ${clazz.simpleName}")
                true
            }

            else -> false
        }
    }

    /*
    * this auto discovers the modules and includes/ignores the relevant annotations, see annotation package
     */
    fun register(packagePath: String) {
        val csr = ClassScanner.find(packagePath)

        if (csr.isEmpty()) Logger.debug("no classes found in module")

        for (classes in csr) {
            Logger.debug("found $classes")
            val clazz = runCatching { Class.forName(classes) }.getOrNull() ?: continue
            val module = clazz.getField("INSTANCE").get(null) as? Module ?: clazz.getDeclaredConstructor().newInstance() as Module
            if (canUse(clazz)) register(module) else continue

            Logger.debug("registering module ${clazz.simpleName}")
        }
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
}
