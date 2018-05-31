package com.cool.eye.notify.util.dialog

/**
 *Created by cool on 2018/5/2
 */
//object ProgressDialogHelper {
//
//    private var isShowing = false
//    private var progressDialog: Dialog? = null
//
//    @JvmStatic
//    fun show(context: Context,
//             message: String? = null) {
//        if (isShowing) return
//        isShowing = true
//        progressDialog = Dialog(context, R.style.CustomDialog)
//        progressDialog!!.setContentView(R.layout.layout_loading_dialog)
//        progressDialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        progressDialog!!.setCanceledOnTouchOutside(false)
//        progressDialog!!.setCancelable(true)
//        if (!message.isNullOrEmpty()) {
//            progressDialog!!.tv_message.text = message
//        }
//        progressDialog!!.setOnCancelListener { isShowing = false }
//        progressDialog!!.setOnDismissListener { isShowing = false }
//        progressDialog!!.show()
//    }
//
//    @JvmStatic
//    fun cancel() {
//        isShowing = false
//        progressDialog?.cancel()
//        progressDialog = null
//    }
//
//    /**
//     * FIXME call on net error for old request
//     * May case error shown, if shown in a ,bug throw error by b.
//     */
//    @JvmStatic
//    fun cancelOnError() {
//        if (progressDialog != null && progressDialog!!.isShowing) {
//            progressDialog!!.cancel()
//            ToastHelper.showToast(progressDialog!!.context, R.string.network_error)
//            progressDialog = null
//        }
//        isShowing = false
//    }
//}