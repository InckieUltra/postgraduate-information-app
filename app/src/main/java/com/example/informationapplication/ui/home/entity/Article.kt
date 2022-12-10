package com.example.informationapplication.ui.home.entity

import java.time.LocalDate
import java.util.*

class Article(
    val title: String,
    val source: String,
    val date: LocalDate,
    val context:String,
    val articleUrl :String,
)