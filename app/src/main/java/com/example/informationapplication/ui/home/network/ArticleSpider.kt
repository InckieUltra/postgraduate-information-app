package com.example.informationapplication.ui.home.network

import com.example.informationapplication.ui.home.entity.ArticleItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object ArticleSpider {

    fun getAllArticleItem(html: String?): ArrayList<ArticleItem> {
        val items:ArrayList<ArticleItem> = ArrayList()
        if(html == null) return items
        val document: Document = Jsoup.parse(html)
        val elements: Elements = document.select("ul[class=news-list]")
            .select("li")
        for(element in elements){
            var title = element.select("a").text()
            if(title.length > 25) {
                title = title.substring(0,25)+".."
            }
            val date = element.select("span[class=span-time]").text()
            val url = "https://yz.chsi.com.cn"+element.select("a").attr("href")
            items.add(ArticleItem(title,date,url))
        }
        return items
    }

}