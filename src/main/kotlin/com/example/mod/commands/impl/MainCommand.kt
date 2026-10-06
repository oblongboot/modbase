package com.example.mod.commands.impl

import com.example.mod.commands.Command
import com.example.mod.commands.annotation.CommandHandler
import com.example.mod.commands.annotation.SubCommand
import com.example.mod.module.ConfigManager
import com.example.mod.module.ModuleManager
import com.example.mod.utils.ChatUtils

object MainCommand: Command(listOf("modbase", "testcommand")) {
    @CommandHandler
    fun main() {
        ChatUtils.modMessage("CommandHandler for modbase")
    }

    @SubCommand
    fun restoreConfig() {
        ConfigManager.restoreBackup()
        ChatUtils.modMessage("Restored config backup.")
    }
}