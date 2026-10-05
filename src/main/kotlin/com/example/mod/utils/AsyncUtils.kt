package com.example.mod.utils

import java.util.UUID
import java.util.concurrent.Executors

object AsyncUtils {
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
}
