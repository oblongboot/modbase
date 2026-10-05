package com.example.mod.events.impl.packet.impl

import com.example.mod.events.Event
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket

class TeleportEvent(
    val packet: ClientboundPlayerPositionPacket,
) : Event(cancellable = false)
