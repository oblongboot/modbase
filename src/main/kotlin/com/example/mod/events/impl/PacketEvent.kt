package com.example.mod.events.impl

import com.example.mod.events.Event
import net.minecraft.network.protocol.Packet

abstract class PacketEvent(
    val packet: Packet<*>,
) : Event(
    cancellable = false
)

class PacketSend(
    packet: Packet<*>
) : PacketEvent(
    packet = packet,
)

class PacketReceive(
    packet: Packet<*>
) : PacketEvent(
    packet = packet,
)
