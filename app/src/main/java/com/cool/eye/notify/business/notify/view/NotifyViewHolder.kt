package com.cool.eye.notify.business.notify.view

import android.view.View
import com.cool.eye.func.notify.util.Util
import com.cool.eye.notify.R
import com.cool.eye.notify.business.notify.model.Notify
import com.cool.eye.notify.util.recycler.DataViewHolder
import com.cool.eye.notify.util.recycler.LayoutId
import kotlinx.android.synthetic.main.item_notify.view.*

/**
 *Created by cool on 2018/5/29
 */
@LayoutId(R.layout.item_notify)
class NotifyViewHolder(var view: View) : DataViewHolder<Notify>(view) {
    override fun updateViewByData(data: Notify) {
        super.updateViewByData(data)
        view.tv_message.text = view.context.getString(R.string.speak, Util.formatTime(data.time)
                , data.name, data.message)
    }
}