package com.example.informationapplication.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.informationapplication.ui.home.adapter.ArticleItemAdapter
import com.example.informationapplication.ui.home.entity.ArticleItem
import com.example.informationapplication.ui.home.network.ArticleSpider
import com.example.informationapplication.ui.home.network.HttpUtil
import kotlin.concurrent.thread

/**
 * 研究生咨询数据类
 */
class InformationModel : ViewModel() {
    private var perPage = 80
    private var currentPage = 0
    private var baseUrl = "https://yz.chsi.com.cn/kyzx/yxzc/"

    var list: MutableList<ArticleItem> = ArrayList()

    private val articleItemList: MutableLiveData<List<ArticleItem>> by lazy {
        MutableLiveData<List<ArticleItem>>().also {
            list
        }
    }

    fun getArticleItems(): LiveData<List<ArticleItem>> {
        return articleItemList
    }

    private fun loadArticleItems() {
        thread(start = true) {
            currentPage = 0
            list.clear()
            articleItemList.postValue(list)
        }
    }

    fun refreshArticleItems(adapter: ArticleItemAdapter, swipeRefreshLayout: SwipeRefreshLayout) {
        adapter.resetListener()
        loadArticleItems()
        swipeRefreshLayout.isRefreshing = false
    }

    fun loadMore(adapter: ArticleItemAdapter, currentPage: Int) {
        thread(start = true) {
            this.currentPage = currentPage
            Log.d("currentPage",currentPage.toString())
            val start = perPage * (currentPage - 1)
            val url = "$baseUrl?start=$start"
            val html = HttpUtil.okGetArticle(url)
            val tmp = ArticleSpider.getAllArticleItem(html)
            list.addAll(tmp)
            if (list.size == currentPage * perPage) {
                adapter.setCanLoadMore(true)
            } else {
                Log.d("currentPage",currentPage.toString())
                adapter.setCanLoadMore(false)
            }
            articleItemList.postValue(list)

        }
    }

    fun setUrl(url:String){
        baseUrl = url
    }

    fun setPage(page: Int?) {
        if (page != null) {
            perPage = page
        }
    }
}


