package com.example.mod.utils

import com.example.mod.ModInit.mc
import java.util.*
import java.util.concurrent.Executors

object ThreadUtils {
    private val singleThreadExecutor = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable, "modbase-async-main").apply {
            isDaemon = true
        }
    }

    fun runAsync(task: () -> Unit, newThread: Boolean = false) {
        val runnable = Runnable {
            try {
                task()
            } catch (e: Exception) {
                ChatUtils.modMessage(
                    "Failed running ${task.javaClass.simpleName} async :(, check logs for error"
                )
                e.printStackTrace()
            }
        }

        if (newThread) {
            Thread(
                runnable,
                "modbase-async-${UUID.randomUUID()}" // todo: move to ThreadPoolExecutor? too tired rn xd
            ).apply {
                isDaemon = true
            }.start()
        } else {
            singleThreadExecutor.execute(runnable)
        }
    }

    fun runOnMainThreadIfNotAlready(code: () -> Unit) {
        if (mc.isSameThread) {
            code()
        } else {
            mc.execute {
                code()
            }
        }
    }
}
