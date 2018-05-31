package com.cool.eye.notify.db

import android.content.ContentValues
import com.cool.eye.func.notify.util.Util
import com.cool.eye.notify.business.notify.model.Notify
import com.cool.eye.notify.util.thread.ThreadHelper

/**
 *Created by cool on 2018/5/29
 */
object DBHelper {

    private var helper: NotifySqliteHelper = NotifySqliteHelper()

    @JvmStatic
    fun insertNotify(platform: String, name: String, msg: String, time: Long): Notify {
        val notify = Notify()
        notify.name = name
        notify.message = msg
        notify.time = time
        notify.platform = platform
        insertMessage(notify)
        return notify
    }

    fun insertUser(name: String, platform: String, matchFull: Boolean) {
        ThreadHelper.runOnThread(Runnable {
            val db = helper.writableDatabase
            val value = ContentValues()
            value.put(NotifySqliteHelper.NAME, name)
            value.put(NotifySqliteHelper.MATCH_FULL, if (matchFull) 1 else 0)
            value.put(NotifySqliteHelper.PLATFORM, platform)
            db.insert(NotifySqliteHelper.TB_USER, null, value)
        })
    }

    fun deleteUser(platform: String, name: String) {
        ThreadHelper.runOnThread(Runnable {
            val db = helper.writableDatabase
            db.delete(NotifySqliteHelper.TB_USER, "${NotifySqliteHelper.PLATFORM}=? and " +
                    "${NotifySqliteHelper.NAME}=?", arrayOf(platform, name))
        })
    }

    fun insertMessage(notify: Notify) {
        if (notify.name.isNullOrEmpty()) return
        ThreadHelper.runOnThread(Runnable {
            val db = helper.writableDatabase
            val value = ContentValues()
            value.put("name", notify.name)
            value.put("message", notify.message)
            value.put("time", notify.time)
            value.put("platform", notify.platform)
            db.insert(NotifySqliteHelper.TB_NOTIFY, null, value)
        })
    }

    fun getTodayMessages(): List<Notify>? {
        val today = Util.getToday()
        return getMessages(today[0], today[1])
    }

    fun getMessages(from: Long, to: Long): List<Notify>? {
        val db = helper.readableDatabase
        val cursor = db.query(NotifySqliteHelper.TB_NOTIFY, null, "${NotifySqliteHelper.TIME} >=? and " +
                "${NotifySqliteHelper.TIME} <= ?", arrayOf(from.toString(), to.toString()), null, null, null)
        cursor?.use { it ->
            val notices = mutableListOf<Notify>()
            while (it.moveToNext()) {
                val notify = Notify()
                notify.name = it.getString(it.getColumnIndex(NotifySqliteHelper.NAME))
                notify.message = it.getString(it.getColumnIndex(NotifySqliteHelper.MESSAGE))
                notify.platform = it.getString(it.getColumnIndex(NotifySqliteHelper.PLATFORM))
                notify.time = it.getLong(it.getColumnIndex(NotifySqliteHelper.TIME))
                notices.add(notify)
            }
            return notices
        }
        return null
    }

    fun getNames(platform: String): List<String>? {
        val db = helper.readableDatabase
        val cursor = db.query(NotifySqliteHelper.TB_USER, null,
                "${NotifySqliteHelper.PLATFORM} ==? ", arrayOf(platform), null, null, null)
        cursor?.use { it ->
            val names = mutableListOf<String>()
            while (it.moveToNext()) {
                names.add(it.getString(it.getColumnIndex(NotifySqliteHelper.NAME)))
            }
            return names
        }
        return null
    }

    fun getUsers(): Map<String, List<String>>? {
        val db = helper.readableDatabase
        val cursor = db.query(NotifySqliteHelper.TB_USER, null, null, null, null, null,
                NotifySqliteHelper.PLATFORM)
        cursor?.use { it ->
            val users = mutableMapOf<String, ArrayList<String>>()
            while (it.moveToNext()) {
                val key = it.getString(it.getColumnIndex(NotifySqliteHelper.PLATFORM))
                if (users.containsKey(key)) {
                    users[key]!!.add(it.getString(it.getColumnIndex(NotifySqliteHelper.NAME)))
                } else {
                    users[key] = ArrayList()
                }
            }
            return users
        }
        return null
    }
}