package com.example.mod.utils

import com.example.mod.ModInit.mc
import net.minecraft.network.chat.Component

object ChatUtils {
    private val modbasePrefix = Component.literal("§8[§bmodbase§8] ")
    private val debugPrefix = Component.literal("§8[§bmodbase Debug§8] ")

    fun modMessage(msg: String, useDebugPrefix: Boolean = false) {
        val player = mc.player

        if (player === null) {
            Logger.error("failed to send message, null player. message is $msg")
            return
        }

        ThreadUtils.runOnMainThreadIfNotAlready {
            player.sendSystemMessage(
                (if (useDebugPrefix) debugPrefix else modbasePrefix).copy().append(Component.literal(msg)),
            )
        }
    }

}