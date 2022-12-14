package com.example.informationapplication.ui.home.entity

class ArticleItem(
    val title: String,
    val date: String,
    val articleUrl:String,
){
    override fun toString(): String {
        return "$title $date $articleUrl"
    }
}