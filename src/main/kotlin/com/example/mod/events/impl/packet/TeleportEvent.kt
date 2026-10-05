package com.example.mod.events.impl.packet

import com.example.mod.events.Event
import com.example.mod.events.EventManager
import com.example.mod.events.impl.PacketReceive
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket

class TeleportEvent(
    val packet: ClientboundPlayerPositionPacket,
) : Event(cancellable = false)

internal object TeleportEventHandler {
    fun init() {
        EventManager.register(PacketReceive::class, {
            val packet = it.packet as? ClientboundPlayerPositionPacket ?: return@register

            EventManager.post(TeleportEvent(packet))
        })
    }
}
