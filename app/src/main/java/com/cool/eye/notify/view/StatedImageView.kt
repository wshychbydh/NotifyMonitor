package com.cool.eye.notify.view

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.StateSet
import android.widget.ImageView

/**
 * When assigned a normal drawable, this class will add a pressed state automatically.
 * By default_img, a 50% transparent affect is applied to the pressed state. And you can customized
 * it by adding a PorterDuffColorFilter.
 */
class StatedImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    var pressedColorFilter: PorterDuffColorFilter? = null
        get() = if (field != null) field else DEFAULT_COLOR_FILTER

    override fun setImageDrawable(drawable: Drawable?) {
        if (!drawable!!.isStateful) {
            val stateDrawable = ButtonStateListDrawable()
            stateDrawable.addState(StateSet.WILD_CARD, drawable)
            super.setImageDrawable(stateDrawable)
        } else {
            super.setImageDrawable(drawable)
        }
    }

    override fun setImageResource(resId: Int) {
        setImageDrawable(resources.getDrawable(resId))
    }

    private inner class ButtonStateListDrawable : StateListDrawable() {

        override fun onStateChange(stateSet: IntArray): Boolean {
            val hasPressedState = stateSet.contains(android.R.attr.state_pressed)
            if (hasPressedState || !isEnabled) {
                colorFilter = pressedColorFilter
            } else {
                clearColorFilter()
            }
            return super.onStateChange(stateSet)
        }
    }

    companion object {

        private val DEFAULT_COLOR_FILTER = PorterDuffColorFilter(0x80FFFFFF.toInt(), PorterDuff.Mode.MULTIPLY)
    }
}
