package com.example.informationapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.informationapplication.ui.home.adapter.ArticleItemAdapter
import com.example.informationapplication.ui.home.entity.ArticleItem
import kotlin.concurrent.thread

class InformationModel : ViewModel() {
    private val PER_PAGE = 50
    private var currentPage = 0
    var articles = listOf(
        ArticleItem("a", "2020-11-20", "www.baidu.com"),
        ArticleItem("b", "2020-11-22", "www.baidu.com"),
        ArticleItem("c", "2020-11-24", "www.baidu.com"),
        ArticleItem("d", "2020-11-24", "www.baidu.com"),
    )

    var list: MutableList<ArticleItem> = ArrayList()

    private val articleItemList: MutableLiveData<List<ArticleItem>> by lazy {
        MutableLiveData<List<ArticleItem>>().also {
            loadArticleItems()
        }
    }

    fun getArticleItems(): LiveData<List<ArticleItem>> {
        return articleItemList
    }

    private fun loadArticleItems() {
        thread(start = true) {
            list.clear()
            repeat(50) {
                val index = (articles.indices).random()
                list.add(articles[index])
            }
            articleItemList.postValue(list)
            currentPage = 1
        }
    }

    fun refreshArticleItems(swipeRefreshLayout: SwipeRefreshLayout) {
        loadArticleItems()
        swipeRefreshLayout.isRefreshing = false
    }

    fun loadMore(adapter: ArticleItemAdapter, currentPage: Int) {
        thread(start = true) {
            this.currentPage = currentPage
            repeat(50) {
                val index = (articles.indices).random()
                list.add(articles[index])
            }
//            articleSpider.getAllArticleItem("")
            Thread.sleep(2000)
            if (list.size == currentPage * PER_PAGE) {
                adapter.setCanLoadMore(true)
            } else {
                adapter.setCanLoadMore(false)
            }
            articleItemList.postValue(list)

        }
    }


}


