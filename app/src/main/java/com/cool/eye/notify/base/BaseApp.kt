package com.cool.eye.notify.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 *Created by cool on 2018/5/29
 */

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}