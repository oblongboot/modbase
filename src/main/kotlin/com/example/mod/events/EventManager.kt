package com.example.mod.events

import kotlin.reflect.KClass

object EventManager {
    typealias EventListener<T> = (T) -> Unit

    private val events = mutableMapOf<KClass<out Event>, MutableList<EventListener<*>>>()

    fun <T : Event> register(
        eventClass: KClass<T>,
        vararg listeners: EventListener<T>
    ) {
        val list = events.getOrPut(eventClass) { mutableListOf() }

        @Suppress("UNCHECKED_CAST")
        list.addAll(listeners as Array<out EventListener<*>>)
    }

    fun <T : Event> post(event: T) {
        val listeners = events[event::class] ?: return

        for (listener in listeners) {
            @Suppress("UNCHECKED_CAST")
            (listener as EventListener<T>)(event)
        }
    }
}
