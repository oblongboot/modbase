package com.example.mod.events.impl.packet

import com.example.mod.events.Event
import com.example.mod.events.EventManager
import com.example.mod.events.impl.PacketReceive
import com.example.mod.utils.Logger

object PacketEventRegistry {
    val handlers = mutableMapOf<Class<*>, (Any) -> List<Event>>()

    inline fun <reified P : Any> register(
        noinline arg: (P) -> Event,
    ) {
        Logger.debug("registering ${P::class.java.name} packetevent!")

        handlers[P::class.java] = { packet ->
            listOf(arg(packet as P))
        }
    }

    inline fun <reified P : Any> registerMany(
        noinline arg: (P) -> List<Event>,
    ) {
        Logger.debug("registering ${P::class.java.name} packetevent!")

        handlers[P::class.java] = { packet ->
            arg(packet as P)
        }
    }

    fun init() {
        EventManager.register(PacketReceive::class, {
            handlers[it.packet.javaClass]?.invoke(it.packet)?.forEach(EventManager::post)
        })
    }
}
