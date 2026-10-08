package com.example.mod.utils

object Logger {
    private var isDebugEnabled = false

    @JvmStatic
    fun setDebug(isDebugEnabled: Boolean) {
        this.isDebugEnabled = isDebugEnabled
    }
    @JvmStatic
    fun print(msg: String) {
        println(msg)
    }
    @JvmStatic
    fun debug(msg: String) {
        if (isDebugEnabled) {
            println("[modbase DEBUG] $msg")
        } else return
    }
    @JvmStatic
    fun error(msg: String) {
        System.err.println("[modbase ERROR] $msg")
    }
}