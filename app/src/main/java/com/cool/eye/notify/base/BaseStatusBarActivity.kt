package com.cool.eye.notify.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.cool.eye.func.notify.util.Util
import com.cool.eye.notify.R

/**
 *Created by cool on 2018/5/30
 */
abstract class BaseStatusBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isFullScreen()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = window
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setFitsSystemWindows(!isImmersion())
    }

    private fun setFitsSystemWindows(fitSystemWindows: Boolean) {
        val contentFrameLayout = findViewById(android.R.id.content) as ViewGroup
        val parentView = contentFrameLayout.getChildAt(0)
        if (parentView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            parentView.fitsSystemWindows = fitSystemWindows
        }
        if (fitSystemWindows) {
            setStatusViewColor(getStatusBarColor())
        }
    }

    open fun isFullScreen(): Boolean {
        return false
    }

    /**
     * Prevent overwriting elsewhere.
     */
    final override fun isImmersive(): Boolean {
        return super.isImmersive()
    }

    open fun isImmersion(): Boolean {
        return true
    }

    open fun getStatusBarColor(): Int {
        return resources.getColor(R.color.gold)
    }

    private fun setStatusViewColor(color: Int) {
        val contentView = findViewById(android.R.id.content) as ViewGroup
        val statusBarView = View(this)
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                Util.getStatusBarHeight(this))
        statusBarView.setBackgroundColor(color)
        contentView.addView(statusBarView, lp)
    }
}