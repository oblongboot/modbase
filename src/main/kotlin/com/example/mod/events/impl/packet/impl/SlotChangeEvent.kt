package com.example.mod.events.impl.packet.impl

import com.example.mod.events.Event
import com.example.mod.events.EventManager
import com.example.mod.events.impl.PacketReceive
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket

class SlotChangeEvent(
    val packet: ClientboundSetHeldSlotPacket,
) : Event(cancellable = false)