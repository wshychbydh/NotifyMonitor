package com.cool.eye.notify.business.notify.view

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.cool.eye.notify.R
import com.cool.eye.notify.base.BaseStatusBarActivity
import com.cool.eye.notify.db.DBHelper
import com.cool.eye.notify.util.Constant
import com.cool.eye.notify.util.dialog.DialogHelper
import com.cool.eye.notify.util.dialog.ToastHelper
import com.cool.eye.notify.util.recycler.RecyclerAdapter
import com.cool.eye.notify.util.thread.ThreadHelper
import kotlinx.android.synthetic.main.activity_attention_add.*

class AttentionAddActivity : BaseStatusBarActivity() {

    private lateinit var adapter: RecyclerAdapter
    private var platform: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attention_add)

        recyclerView.layoutManager = GridLayoutManager(this, 4)
        adapter = RecyclerAdapter()
        recyclerView.adapter = adapter
        adapter.registerViewHolder(String::class.java, NameViewHolder::class.java)
        adapter.setItemLongClickListener(View.OnLongClickListener { view ->
            DialogHelper.showDialog(this, getString(R.string.delete_tips), showCancel = true, callback = {
                val data = view.tag as String
                val removed = adapter.removeData(data)
                if (removed) {
                    println("platform------->>$platform")
                    println("name------->>$data")
                    DBHelper.deleteUser(platform!!, data)
                }
            })
        })
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            platform = when (checkedId) {
                R.id.qq -> Constant.QQ
                R.id.wx -> Constant.WX
                R.id.shortMsg -> Constant.MMS
                R.id.phone -> Constant.CALL
                else -> Constant.OTHER
            }
            updateAddedName(platform!!)
        }

        titleView.setSubmitListener(View.OnClickListener {
            if (platform.isNullOrEmpty()) return@OnClickListener
            val name = et_name.text.toString()
            if (name.isEmpty()) return@OnClickListener
            if (adapter.data.contains(name)) {
                ToastHelper.showToast(this, getString(R.string.name_added))
            } else {
                DBHelper.insertUser(name, platform!!, cb_match.isChecked)
                finish()
            }
        })
    }

    private fun updateAddedName(platform: String) {
        ThreadHelper.finallyOnUiThread(this, {
            DBHelper.getNames(platform)
        }, {
            adapter.updateData(it)
        })
    }
}
