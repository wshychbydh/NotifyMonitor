package com.cool.eye.notify.util.thread

import android.app.Activity
import java.util.concurrent.Executors

/**
 *Created by cool on 2018/5/29
 */
object ThreadHelper {

    private val executor = Executors.newSingleThreadExecutor()

    @JvmStatic
    fun runOnThread(runnable: Runnable) {
        executor.execute(runnable)
    }

    @JvmStatic
    fun <T> finallyOnUiThread(activity: Activity, async: () -> T, ui: (data: T) -> Unit) {
        executor.execute {
            val data = async.invoke()
            activity.runOnUiThread {
                ui.invoke(data)
            }
        }
    }
}