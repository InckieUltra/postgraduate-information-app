package com.example.informationapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.informationapplication.databinding.FragmentLeftBinding
import com.example.informationapplication.ui.home.adapter.ArticleItemAdapter
import com.example.informationapplication.ui.home.entity.ArticleItem

class InformationFragment : Fragment() {

    private var _binding: FragmentLeftBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var currentPage = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val informationModel =
            ViewModelProvider(this).get(InformationModel::class.java)

        _binding = FragmentLeftBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val articleItemList:List<ArticleItem> = ArrayList()
        val adapter = ArticleItemAdapter(articleItemList)
        val swipeRefreshLayout:SwipeRefreshLayout = binding.articleSwipeRefresh

        adapter.setOnLoadMoreListener(object : ArticleItemAdapter.OnLoadMoreListener {
            override fun onLoadMore(currentPage: Int) {
                this@InformationFragment.currentPage = currentPage
                informationModel.refreshArticleItems(swipeRefreshLayout)

            }
        })
        informationModel.getArticleItems().observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                adapter.setArticleList(it)
                adapter.notifyDataSetChanged()
            }
        }

        val recyclerView = binding.articleRecyclerView

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            informationModel.refreshArticleItems(swipeRefreshLayout)
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}