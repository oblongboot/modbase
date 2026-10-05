package com.example.mod.events

open class Event(
    val cancellable: Boolean = false,
) {
    var cancelled: Boolean = false
        private set

    fun cancel() {
        if (cancellable) {
            cancelled = true
        }
    }
}
