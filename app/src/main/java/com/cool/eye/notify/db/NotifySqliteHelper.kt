package com.cool.eye.notify.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.cool.eye.notify.base.BaseApp

/**
 *Created by cool on 2018/5/29
 */
class NotifySqliteHelper : SQLiteOpenHelper(BaseApp.context, DB_NAME, null, VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        createTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TB_NOTIFY")
        db.execSQL("DROP TABLE IF EXISTS $TB_USER")
        createTables(db)
    }

    private fun createTables(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS $TB_NOTIFY($NAME VARCHAR(50), $MESSAGE TEXT, " +
                "$TIME INTEGER , $PLATFORM VARCHAR(20))")

        db.execSQL("CREATE TABLE IF NOT EXISTS $TB_USER($NAME VARCHAR(50), $PLATFORM VARCHAR(20)," +
                "$MATCH_FULL INTEGER)")
    }

    companion object {
        const val DB_NAME = "monitor"
        const val TB_NOTIFY = "notify"
        const val TB_USER = "user"
        const val VERSION = 1
        const val NAME = "name"
        const val MESSAGE = "message"
        const val TIME = "time"
        const val PLATFORM = "platform"
        const val MATCH_FULL = "match_full"
    }
}