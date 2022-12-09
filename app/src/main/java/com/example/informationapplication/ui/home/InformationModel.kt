package com.example.informationapplication.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.informationapplication.ui.home.entity.ArticleItem
import kotlin.concurrent.thread

class InformationModel : ViewModel() {
    var articles = listOf(
        ArticleItem("a","2020-11-20","www.baidu.com"),
        ArticleItem("b","2020-11-22","www.caidu.com"),
        ArticleItem("c","2020-11-24","www.caidu.com"),
        ArticleItem("d","2020-11-24","www.caidu.com"),
    )

    var list : MutableList<ArticleItem> = ArrayList()

    private val articleItemList:MutableLiveData<List<ArticleItem>> by lazy {
        MutableLiveData<List<ArticleItem>>().also {
            loadArticleItems()
        }
    }

    fun getArticleItems() : LiveData<List<ArticleItem>>{
        return articleItemList
    }

    private fun loadArticleItems(){
        thread(start = true) {
            list.clear()
            repeat(50){
                val index = (articles.indices).random()
                list.add(articles[index])
            }
            articleItemList.postValue(list)
        }
    }

    fun refreshArticleItems(swipeRefreshLayout: SwipeRefreshLayout) {
        loadArticleItems()
        swipeRefreshLayout.isRefreshing = false
    }


}


