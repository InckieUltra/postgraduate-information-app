package com.example.informationapplication.ui.middle

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun addOrUpdateSchedule(schedule: Schedule) {
        // TODO insert schedule
    }
}