package com.example.informationapplication.ui.home.network

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

object HttpUtil {
    fun okGetArticle(url:String, callback:Callback){
        var html: String? = null
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(callback)
    }
}