package com.example.informationapplication.ui.middle.entity

import com.example.informationapplication.R
import java.util.*

class Schedule {
    var id : Int? = null
    var title : String = "新建日程"
    var content : String = ""
    lateinit var date : Date
    var tag : String = R.id.box1.toString()
}