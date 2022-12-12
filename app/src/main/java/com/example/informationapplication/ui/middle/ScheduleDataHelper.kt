package com.example.informationapplication.ui.middle

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.RuntimeException

class ScheduleDataHelper(private val context: Context?, version: Int): SQLiteOpenHelper(context, "schedule.db", null, version) {
    private val createDB: String = "CREATE TABLE schedule " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "title TEXT, " +
            "content TEXT, " +
            "date TEXT NOT NULL, " +
            "tag TEXT)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createDB)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS schedule")
        onCreate(db)
    }

    fun addOrUpdateSchedule(schedule: Schedule) {
        if (schedule.id == null || schedule.id!! < 0) {
            addSchedule(schedule)
        }
        else {
            upgradeSchedule(schedule)
        }
    }

    fun deleteSchedule(schedule: Schedule) {
        if (schedule.id != null && schedule.id != -1) {
            deleteScheduleFromDB(schedule.id!!)
        }
        else return
    }

    private fun addSchedule(schedule: Schedule) = try {
        val db = this.writableDatabase
        var value = ContentValues().apply {
            put("title", schedule.title)
            put("content", schedule.content)
            put("date", DatesUtil.toString(schedule.date))
            put("tag", schedule.tag)
        }
        db.insert("schedule", null, value)
    } catch (exception: RuntimeException) {
        Log.e("SQLite", "addSchedule error", exception)
    }

    private fun upgradeSchedule(schedule: Schedule) = try {
        val db = this.writableDatabase
        var value = ContentValues().apply {
            put("title", schedule.title)
            put("content", schedule.content)
            put("tag", schedule.tag)
        }
        db.update("schedule", value, "id = ?", arrayOf(schedule.id.toString()))
    } catch (exception: RuntimeException) {
        Log.e("SQLite", "upgradeSchedule error", exception)
    }

    private fun deleteScheduleFromDB(id: Int) = try {
        val db = this.writableDatabase
        db.delete("schedule", "id = ?", arrayOf(id.toString()))
    } catch (exception: RuntimeException) {
        Log.e("SQLite", "deleteSchedule error", exception)
    }
}