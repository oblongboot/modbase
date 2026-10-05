package com.example.mod.module

import net.minecraft.client.Minecraft

open class Module(
    val id: String,
    val name: String,
    val category: ModuleCategory,
    val description: String,
    defaultEnabled: Boolean = false,
) {
    protected val mc: Minecraft = Minecraft.getInstance()

    protected val player
        get() = mc.player

    var enabled: Boolean = defaultEnabled
        private set

    private val settings = mutableListOf<Setting<*>>()

    protected fun <T : Setting<*>> setting(setting: T): T {
        settings += setting
        return setting
    }

    fun getSettings(): List<Setting<*>> = settings

    fun toggle() {
        if (enabled) disable()
        else enable()
    }

    fun enable() {
        if (enabled) return

        enabled = true
        onEnable()
    }

    fun disable() {
        if (!enabled) return

        enabled = false
        onDisable()
    }

    protected open fun onEnable() {}
    protected open fun onDisable() {}
}
