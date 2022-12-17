package com.example.informationapplication.ui.home.network

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object HttpUtil {
    fun okGetArticle(url: String): String {
        var html = "<div></div>"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        try {
            val response = client.newCall(request).execute()
            html = response.body?.string().toString()
        } catch (e:IOException){
            return html
        }
        return html
    }
}