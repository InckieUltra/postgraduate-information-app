package com.example.informationapplication.ui.home.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerOnScrollerListener(var recyclerView: RecyclerView) :
    RecyclerView.OnScrollListener() {
    private var currentPage: Int = 1
    private var isLoading: Boolean = false
    private var isCanLoadMore: Boolean = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (isCanLoadMore) {
            currentPage++
            onLoadMore(currentPage)
            isLoading = true
        }
    }

    abstract fun onLoadMore(currentPage: Int)

    private fun isSlideToBottom(): Boolean {
        val layoutManager: LinearLayoutManager? = recyclerView.layoutManager as LinearLayoutManager?
        val totalItemCount = layoutManager?.itemCount
        val visibleItemCount = layoutManager?.childCount
        val lastVisiblePosition = layoutManager?.findLastVisibleItemPosition()
        if (visibleItemCount != null) {
            return !isLoading && visibleItemCount > 0 && lastVisiblePosition == totalItemCount
        }
        return false
    }

    fun setCanLoadMore(load:Boolean){
        isCanLoadMore = load
    }

    fun setLoading(load:Boolean){
        isLoading = load
    }
}