package com.cool.eye.func.notify.util

import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.text.TextUtils
import com.cool.eye.notify.R
import com.cool.eye.notify.util.dialog.ToastHelper
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/**
 *Created by cool on 2018/5/29
 */
object Util {

    private val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA)
    private val date = Date()
    private var mediaPlayer: MediaPlayer? = null

    @JvmStatic
    fun startAlarm(context: Context) {
        if (mediaPlayer == null) {
            val uri = getSystemDefaultRingtoneUri(context)
            mediaPlayer = MediaPlayer.create(context, uri)
            mediaPlayer!!.isLooping = false
        } else if (mediaPlayer!!.isPlaying) return

        try {
            mediaPlayer!!.setOnCompletionListener {
                mediaPlayer!!.release()
                mediaPlayer = null
            }
            mediaPlayer!!.prepare()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mediaPlayer!!.start()
    }

    fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    //获取系统默认铃声的Uri
    private fun getSystemDefaultRingtoneUri(context: Context): Uri {
        return RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE)
    }

    fun parseTime(time: String): Long {
        return sdf.parse(time).time
    }

    fun formatTime(time: Long): String {
        date.time = time
        return sdf.format(date)
    }

    fun getToday(): LongArray {
        val calendar = Calendar.getInstance()
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
        val startDate = calendar.time
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59)
        val endDate = calendar.time
        return longArrayOf(startDate.time, endDate.time)
    }

    fun createCircleBitmap(source: Bitmap): Bitmap {

        val bitmapSize = Math.max(source.width, source.height).toFloat()
        val matrix = Matrix()
        matrix.postScale(bitmapSize / source.width, bitmapSize / source.height)
        val bitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.isDither = true
        paint.isFilterBitmap = true
        val target = Bitmap.createBitmap(bitmapSize.toInt(), bitmapSize.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(target)
        canvas.drawCircle(bitmapSize / 2f, bitmapSize / 2f, bitmapSize / 2f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return target
    }

    /**
     * DP转PX
     */
    fun dpToPx(context: Context, dpSize: Float): Int {
        return (context.resources.displayMetrics.density * dpSize).toInt()
    }

    fun getDrawableResources(context: Context, name: String): Int {
        return context.resources.getIdentifier(name, "drawable", context.packageName)
    }

    fun getVersionCode(context: Context): Int {
        var versonCode = 0
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            versonCode = packageInfo.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return versonCode
    }

    fun getVersionName(context: Context): String {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * 纯数字
     *
     * @param str
     * @return
     */
    fun isNumeric(str: String): Boolean {
        var i = str.length
        while (--i >= 0) {
            if (!Character.isDigit(str[i])) {
                return false
            }
        }
        return true
    }

    /**
     * 纯字母
     *
     * @param fstrData
     * @return
     */
    fun isChar(fstrData: String): Boolean {
        val c = fstrData[0]
        return if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
            true
        } else {
            false
        }
    }

    fun isLetterDigit(str: String): Boolean {
        var isDigit = false
        var isLetter = false
        for (i in 0 until str.length) {
            if (Character.isDigit(str[i])) {
                isDigit = true
            } else if (Character.isLetter(str[i])) {
                isLetter = true
            }
        }
        val regex = "^[a-zA-Z0-9]+$"
        return isDigit && isLetter && str.matches(regex.toRegex())
    }

    fun isUrl(url: String): Boolean {
        if (TextUtils.isEmpty(url)) return false
        val pattern = Pattern
                .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+" + "([A-Za-z0-9-~/])+$")
        return pattern.matcher(url).matches()
    }

    fun call(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun toMarket(context: Context) {
        try {
            val uri = Uri.parse("market://details?id=" + context.packageName)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            ToastHelper.showToast(context, R.string.no_market_find)
        }

    }

    /**
     * 判断某个界面是否在前台
     *
     * @param activity 要判断的Activity
     * @return 是否在前台显示
     */
    fun isForeground(activity: Activity): Boolean {
        return isForeground(activity, activity.javaClass.name)
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    fun isForeground(context: Context?, className: String): Boolean {
        if (context == null || TextUtils.isEmpty(className))
            return false
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = am.getRunningTasks(1)
        if (list != null && list.size > 0) {
            val cpn = list[0].topActivity
            return className == cpn.className
        }
        return false
    }

    /**
     * 利用反射获取状态栏高度
     *
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        //获取状态栏高度的资源id
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen",
                "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun isPhone(mobile: String): Boolean {
        val p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[0-9]|18[0-9])[0-9]{8}$")
        val m = p.matcher(mobile)
        return m.matches()
    }

    @JvmStatic
    fun startService(context: Context, clazz: Class<out Service>) {
        val intent = Intent(context, clazz)
        intent.flags = Intent.FLAG_INCLUDE_STOPPED_PACKAGES
        context.startService(intent)
    }
}