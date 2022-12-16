package com.example.informationapplication.ui.home.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.informationapplication.R

class NewsContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_content)
        val url = intent.getStringExtra("news_url")
        val webView:WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.textZoom = 200
        webView.webViewClient = WebViewClient()
        if(url!=null){
            webView.loadUrl(url)
        } else {
            webView.loadUrl("https://yz.chsi.com.cn/")
        }
    }

    companion object {
        fun actionStart(context: Context, url: String) {
            val intent = Intent(context, NewsContentActivity::class.java).apply {
                putExtra("news_url", url)
            }
            context.startActivity(intent)
        }
    }
}