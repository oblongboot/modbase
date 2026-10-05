package com.example.mod.events

import com.example.mod.events.impl.ChatReceive
import com.example.mod.events.impl.WorldRenderBeforeBlockOutline
import com.example.mod.events.impl.WorldRenderBeforeGizmos
import com.example.mod.events.impl.packet.PacketEventRegistry
import com.example.mod.events.impl.packet.impl.EntityVelocityChangeEvent
import com.example.mod.events.impl.packet.impl.SelfVelocityChangeEvent
import com.example.mod.events.impl.packet.impl.SlotChangeEvent
import com.example.mod.events.impl.packet.impl.TeleportEvent
import com.example.mod.utils.Logger
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket

object FabricProvidedEvents {
    fun register() {
        PacketEventRegistry.init()

        PacketEventRegistry.register<ClientboundSetHeldSlotPacket>(::SlotChangeEvent)

        PacketEventRegistry.registerMany<ClientboundSetEntityMotionPacket> { packet ->
            if (packet.id == Minecraft.getInstance().player?.id) {
                listOf(SelfVelocityChangeEvent(packet))
            } else {
                listOf(EntityVelocityChangeEvent(packet))
            }
        }

        PacketEventRegistry.register<ClientboundPlayerPositionPacket>(::TeleportEvent)

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