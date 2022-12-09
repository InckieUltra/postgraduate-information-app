package com.example.informationapplication.ui.home.network

import com.example.informationapplication.ui.home.entity.ArticleItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.time.LocalDate

object ArticleSpider {

    fun getAllArticleItem(html: String) {
        val items:ArrayList<ArticleItem> = ArrayList()
        val document: Document = Jsoup.parse(html)
        val elements: Elements = document.select("ul[class=news-list]")
            .select("li");
        for(element in elements){
            val title = element.select("a").text()
            val date = element.select("span[class=span-time]").text()
            val url = "https://yz.chsi.com.cn"+element.select("a").attr("href")
            items.add(ArticleItem(title,date,url))
        }
    }

}