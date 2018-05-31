package com.cool.eye.notify.view

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class RecyclerItemDecoration
/**
 * @param itemSpace item间隔
 * @param itemNum   每行item的个数
 */
(private val itemSpace: Int, private val itemNum: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = itemSpace
        if (parent.getChildLayoutPosition(view) % itemNum == 0) {
            outRect.left = 0
        } else {
            outRect.left = itemSpace
        }

    }
}