package com.example.mod.events.impl.packet.impl

import com.example.mod.events.Event
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket


class SlotChangeEvent(
    val packet: ClientboundSetHeldSlotPacket,
) : Event(cancellable = false)