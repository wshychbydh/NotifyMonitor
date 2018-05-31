package com.cool.eye.notify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout.VERTICAL
import android.widget.Toast
import com.cool.eye.func.notify.util.Util
import com.cool.eye.notify.base.BaseApp.Companion.context
import com.cool.eye.notify.base.BaseStatusBarActivity
import com.cool.eye.notify.business.notify.model.Notify
import com.cool.eye.notify.business.notify.view.AttentionAddActivity
import com.cool.eye.notify.business.notify.view.NotifyViewHolder
import com.cool.eye.notify.db.DBHelper
import com.cool.eye.notify.service.ServiceUtil
import com.cool.eye.notify.util.Constant
import com.cool.eye.notify.util.dialog.DialogHelper
import com.cool.eye.notify.util.recycler.RecyclerAdapter
import com.cool.eye.notify.util.thread.ThreadHelper
import kotlinx.android.synthetic.main.content_main.*

class NotifyActivity : BaseStatusBarActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var adapter: RecyclerAdapter

    private lateinit var source: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)

        titleView.setBackBtnVisible(View.GONE)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            startActivity(Intent(this, AttentionAddActivity::class.java))
        }

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val headIv = navigationView.getHeaderView(0).findViewById(R.id.iv_head) as ImageView
        headIv.setImageBitmap(Util.createCircleBitmap((resources.getDrawable(R.drawable.timg) as BitmapDrawable).bitmap))

        source = resources.getStringArray(R.array.source)
        //设置布局管理器
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, VERTICAL))
        adapter.registerViewHolder(Notify::class.java, NotifyViewHolder::class.java)
        loadMessageFromLocal()

        ServiceUtil.toggleNotificationListenerService(this)
        ServiceUtil.openSetting(this)

        registerBroadCast()
    }

    private fun registerBroadCast() {
        val filter = IntentFilter(Constant.SEND_WX_BROADCAST)
        context.registerReceiver(receiver, filter)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bundle = intent.extras ?: return
            val notify = bundle.getSerializable(Constant.NOTIFY) as Notify? ?: return
            Util.startAlarm(this@NotifyActivity)
            DialogHelper.showDialog(this@NotifyActivity, getString(R.string.message_obtain,
                    notify.name, notify.message), notify.platform!!, false, callback = {
                Util.stopAlarm()
                reloadMessage()
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun loadMessageFromLocal() {
        ThreadHelper.finallyOnUiThread(this, {
            DBHelper.getTodayMessages()
        }, {
            adapter.updateData(it)
        })
    }

    override fun onResume() {
        super.onResume()
        reloadMessage()
    }

    private fun reloadMessage() {
        ThreadHelper.finallyOnUiThread(this, {
            DBHelper.getTodayMessages()
        }, {
            adapter.updateData(it)
        })
    }

    fun stopAlarm(view: View) {
        Util.stopAlarm()
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        Toast.makeText(this, getString(R.string.function_complete), Toast.LENGTH_SHORT).show()
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        Toast.makeText(this, getString(R.string.function_complete), Toast.LENGTH_SHORT).show()
        return true
    }
}
