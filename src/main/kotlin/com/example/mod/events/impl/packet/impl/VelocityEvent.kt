package com.example.mod.events.impl.packet.impl

import com.example.mod.events.Event
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket

class SelfVelocityChangeEvent(val packet: ClientboundSetEntityMotionPacket) : Event(cancellable = false)
class EntityVelocityChangeEvent(val packet: ClientboundSetEntityMotionPacket) : Event(cancellable = false)