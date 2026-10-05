package com.example.mod.utils

import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.impl.FabricLoaderImpl
import net.fabricmc.loader.impl.launch.FabricLauncherBase

object Logger {
    private var isDebugEnabled = false
    @JvmStatic
    fun setDebug(isDebugEnabled: Boolean) {
        this.isDebugEnabled = isDebugEnabled
    }
    @JvmStatic
    fun print(msg: String) {
        System.out.println(msg)
    }
    @JvmStatic
    fun debug(msg: String) {
        if (isDebugEnabled || FabricLoader.getInstance().isDevelopmentEnvironment) {
            System.out.println("[modbase DEBUG] $msg")
        } else return
    }
    @JvmStatic
    fun error(msg: String) {
        System.err.println("[modbase ERROR] $msg")
    }
}