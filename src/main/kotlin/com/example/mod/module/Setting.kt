package com.example.mod.module

abstract class Setting<T>(
    val name: String,
    val default: T,
) {
    var value: T = default
        protected set

    fun set(value: T) {
        this.value = value
    }

    fun reset() {
        value = default
    }
}