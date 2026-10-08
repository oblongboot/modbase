package com.example.mod.events.impl

import com.example.mod.events.Event

abstract class TickEvent : Event(cancellable = false)

class TickStart : TickEvent()
class TickEnd : TickEvent()