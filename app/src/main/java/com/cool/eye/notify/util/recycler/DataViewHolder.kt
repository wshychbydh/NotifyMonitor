package com.cool.eye.notify.util.recycler

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View


/**
 * Created by cool on 18/4/18.
 * Child class must public static inner class or public outer class.
 */
abstract class DataViewHolder<D>(view: View) : RecyclerView.ViewHolder(view) {

    protected var data: D? = null
        private set

    open fun updateViewByData(data: D) {
        this.data = data
    }

    fun applyListeners(data: D, listeners: SparseArray<Any>) {
        val size = listeners.size()
        (0..size).forEach {
            val key = listeners.keyAt(it)
            val value = listeners.valueAt(it)
            if (value is View.OnClickListener) {
                if (key == RecyclerAdapter.ITEM_CLICK) {
                    itemView.tag = data
                    itemView.setOnClickListener(value)
                } else {
                    val subView = itemView.findViewById(key)
                    subView?.tag = data
                    subView?.setOnClickListener(value)
                }
            } else if (value is View.OnLongClickListener) {
                if (key == RecyclerAdapter.ITEM_LONG_CLICK) {
                    itemView.tag = data
                    itemView.setOnLongClickListener(value)
                } else {
                    val subView = itemView.findViewById(key)
                    subView?.tag = data
                    subView?.setOnLongClickListener(value)
                }
            } else {
                //TODO another listeners
            }
        }
    }
}
