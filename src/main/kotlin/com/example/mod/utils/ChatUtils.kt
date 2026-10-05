package com.example.mod.utils

import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

object ChatUtils {
    private val modbasePrefix = Component.literal("§8[§bmodbase§8] ")
    private val debugPrefix = Component.literal("§8[§bmodbase Debug§8] ")

    fun modMessage(msg: String, useDebugPrefix: Boolean = false) {
        val player = Minecraft.getInstance().player

        if (player === null) {
            println("failed to send message, null player. message is $msg")
            return@modMessage
        }
        Minecraft.getInstance().execute {
            player.sendSystemMessage(
                (if (useDebugPrefix) debugPrefix else modbasePrefix).copy().append(Component.literal(msg)),
            )
        }
    }

}