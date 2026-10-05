package com.example.mod.events

import com.example.mod.events.impl.ChatReceive
import com.example.mod.events.impl.WorldRenderBeforeBlockOutline
import com.example.mod.events.impl.WorldRenderBeforeGizmos
import com.example.mod.events.impl.packet.SlotChangeEventHandler
import com.example.mod.events.impl.packet.TeleportEventHandler
import com.example.mod.events.impl.packet.VelocityEventHandler
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents

object FabricProvidedEvents {
    fun register() {
        TeleportEventHandler.init()
        VelocityEventHandler.init()
        SlotChangeEventHandler.init()

        ClientReceiveMessageEvents.CHAT.register { component, message, profile, bound, instant ->
            EventManager.post(ChatReceive(component, message, profile, bound, instant))
        }

        LevelRenderEvents.BEFORE_GIZMOS.register { a ->
            EventManager.post(WorldRenderBeforeGizmos(a))
        }

        LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register { context, state ->
            EventManager.post(WorldRenderBeforeBlockOutline(context, state))
            true
        }
    }
}