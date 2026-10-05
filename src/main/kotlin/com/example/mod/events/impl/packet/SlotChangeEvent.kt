package com.example.mod.events.impl.packet

import com.example.mod.events.Event
import com.example.mod.events.EventManager
import com.example.mod.events.impl.PacketReceive
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket

class SlotChangeEvent(
    val packet: ClientboundSetHeldSlotPacket,
) : Event(cancellable = false)

internal object SlotChangeEventHandler {
    fun init() {
        EventManager.register(PacketReceive::class, { // im sure i can optimize these, ill commit in 5 mins:tm:
            val packet = it.packet as? ClientboundSetHeldSlotPacket ?: return@register

            EventManager.post(SlotChangeEvent(packet))
        })
    }
}
