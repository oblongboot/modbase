package com.example.mod.events.impl.packet

import com.example.mod.events.Event
import com.example.mod.events.EventManager
import com.example.mod.events.impl.PacketReceive
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket

class SelfVelocityChangeEvent(val packet: ClientboundSetEntityMotionPacket) : Event(cancellable = false)
class EntityVelocityChangeEvent(val packet: ClientboundSetEntityMotionPacket) : Event(cancellable = false)

internal object VelocityEventHandler {
    fun init() {
        EventManager.register(PacketReceive::class, {
            val packet = it.packet as? ClientboundSetEntityMotionPacket ?: return@register

            if (it.packet.id != Minecraft.getInstance().player?.id) {// if not player
                EventManager.post(EntityVelocityChangeEvent(packet))
            } else {
                EventManager.post(SelfVelocityChangeEvent(packet))
            }
        })
    }
}
