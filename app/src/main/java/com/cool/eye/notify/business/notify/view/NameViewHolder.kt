package com.cool.eye.notify.business.notify.view

import android.view.View
import com.cool.eye.notify.R
import com.cool.eye.notify.util.recycler.DataViewHolder
import com.cool.eye.notify.util.recycler.LayoutId
import kotlinx.android.synthetic.main.item_name.view.*

/**
 *Created by cool on 2018/5/30
 */
@LayoutId(R.layout.item_name)
class NameViewHolder(var view: View) : DataViewHolder<String>(view) {
    override fun updateViewByData(data: String) {
        super.updateViewByData(data)
        view.tv_name.text = data
    }
}