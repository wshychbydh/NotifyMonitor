package com.cool.eye.notify.util.dialog

import android.content.Context
import android.support.annotation.StringRes
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.cool.eye.notify.R
import kotlinx.android.synthetic.main.layout_toast.view.*

/**
 *Created by cool on 2018/5/2
 */
object ToastHelper {

    private var lastShowTime = 0L

    @JvmStatic
    fun showToast(context: Context, @StringRes resId: Int) {
        showToast(context, context.getString(resId))
    }

    @JvmStatic
    fun showError(context: Context, throwable: Throwable) {
        showToast(context, throwable.message ?: "")
    }

    @JvmStatic
    fun showToast(context: Context, msg: String) {
        if (msg.isEmpty()) return
        if (System.currentTimeMillis() - lastShowTime < 1000L) return
        lastShowTime = System.currentTimeMillis()
        val toast = Toast(context.applicationContext)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null)
        view.tv_message.text = msg
        toast.view = view
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }
}