package com.cool.eye.notify.util.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cool.eye.func.notify.util.Util
import com.cool.eye.notify.R
import kotlinx.android.synthetic.main.layout_dialog.*

/**
 *Created by cool on 2018/5/2
 */
object DialogHelper {

    @Volatile
    private var isShowing = false

    @JvmStatic
    fun showDialog(context: Context,
                   message: String,
                   callback: ((result: Boolean) -> Unit)?): Boolean {
        return showDialog(context, message, context.getString(R.string.warn_pompt),
                false, context.getString(R.string.sure), callback)
    }

    @JvmStatic
    fun showDialog(context: Context,
                   throwable: Throwable,
                   callback: ((result: Boolean) -> Unit)?): Boolean {
        return showDialog(context, throwable.message ?: "",
                context.getString(R.string.warn_pompt), false,
                context.getString(R.string.sure), callback)
    }

    @JvmStatic
    fun showDialog(context: Context,
                   message: String,
                   title: String = context.getString(R.string.warn_pompt),
                   showCancel: Boolean = false,
                   btnText: String = context.getString(R.string.sure),
                   callback: ((result: Boolean) -> Unit)?): Boolean {

        if (message.trim().isEmpty()) return false

        if (context is Activity) {
            if (!Util.isForeground(context)) return false
        } else return false

        if (isShowing) return false

        val dialog = Dialog(context, R.style.CustomDialog)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_dialog, null)
        dialog.setContentView(view, ViewGroup.LayoutParams((260f * context.resources.displayMetrics.density).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT))
        dialog.setCancelable(false)
        dialog.tv_title.text = title
        dialog.tv_message.text = message

        if (showCancel) {
            dialog.btn_cancel.visibility = View.VISIBLE
            dialog.btn_cancel.setOnClickListener {
                isShowing = false
                dialog.dismiss()
            }
        } else {
            dialog.btn_cancel.visibility = View.GONE
        }

        dialog.btn_ok.text = btnText
        dialog.btn_ok.setOnClickListener {
            callback?.invoke(true)
            isShowing = false
            dialog.dismiss()
        }
        dialog.setOnDismissListener {
            isShowing = false
        }
        dialog.setOnCancelListener {
            isShowing = false
        }

        dialog.show()

        isShowing = dialog.isShowing

        return true
    }
}