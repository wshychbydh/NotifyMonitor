package com.cool.eye.notify.util.recycler

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by cool on 18/4/18.
 */
class RecyclerAdapter : RecyclerView.Adapter<DataViewHolder<Any>>() {

    private val viewHolder = SparseArray<Class<out DataViewHolder<*>>>()
    private val listeners = SparseArray<Any>()

    private var inflater: LayoutInflater? = null

    val data = ArrayList<Any>()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].javaClass.name.hashCode()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<Any> {
        val clazz = viewHolder.get(viewType)
        val layoutId = clazz.getAnnotation(LayoutId::class.java)
                ?: throw IllegalArgumentException(clazz.simpleName + "must be has @LayoutId annotation!")

        if (inflater == null) {
            inflater = LayoutInflater.from(parent.context.applicationContext)
        }

        val itemView = inflater!!.inflate(layoutId.value, parent, false)
        val holder: DataViewHolder<Any>
        try {
            holder = clazz.getConstructor(View::class.java).newInstance(itemView) as DataViewHolder<Any>
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        return holder
    }

    override fun onBindViewHolder(holder: DataViewHolder<Any>, position: Int) {
        val data = data[position]
        if (listeners.size() > 0) {
            holder.applyListeners(data, listeners)
        }
        holder.updateViewByData(data)
    }

    /**
     * Register ViewHolder by dataClass, data is exclusive.
     *
     * @param dataClazz data Class
     * @param clazz     ViewHolder Class
     */
    fun registerViewHolder(dataClazz: Class<*>, clazz: Class<out DataViewHolder<*>>) {
        viewHolder.put(dataClazz.name.hashCode(), clazz)
    }

    fun setItemClickListener(clickListener: View.OnClickListener) {
        setClickListener(ITEM_CLICK, clickListener)
    }

    fun setItemLongClickListener(longClickListener: View.OnLongClickListener) {
        setLongClickListener(ITEM_LONG_CLICK, longClickListener)
    }

    /**
     * id : The view's id
     * The view's tag will be bind with data , call view.getTag() to get it.
     */
    fun setClickListener(id: Int = -1, clickListener: View.OnClickListener) {
        listeners.put(id, clickListener)
    }

    /**
     * id : The view's id
     * The view's tag will be bind with data , call view.getTag() to get it.
     */
    fun setLongClickListener(id: Int = -2, longClickListener: View.OnLongClickListener) {
        listeners.put(id, longClickListener)
    }

    fun appendData(data: List<String>?) {
        if (data != null) {
            this.data.addAll(data)
        }
        notifyDataSetChanged()
    }

    fun appendData(data: Any?) {
        if (data != null) {
            this.data.add(data)
        }
        notifyDataSetChanged()
    }

    fun updateData(data: List<Any>?) {
        this.data.clear()
        if (data != null) {
            this.data.addAll(data)
        }
        notifyDataSetChanged()
    }

    fun updateData(data: Any?) {
        this.data.clear()
        if (null != data) {
            this.data.add(data)
        }
        notifyDataSetChanged()
    }

    fun removeData(data: Any): Boolean {
        val removed = this.data.remove(data)
        if (removed) {
            notifyDataSetChanged()
        }
        return removed
    }

    val lastData: Any?
        get() = if (data.isEmpty()) {
            null
        } else data[data.size - 1]


    companion object {
        const val ITEM_CLICK = -1
        const val ITEM_LONG_CLICK = -2
    }
}
