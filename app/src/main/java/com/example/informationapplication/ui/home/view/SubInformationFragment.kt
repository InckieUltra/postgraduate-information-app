package com.example.informationapplication.ui.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.informationapplication.databinding.FragmentSubInformationBinding
import com.example.informationapplication.ui.home.InformationModel
import com.example.informationapplication.ui.home.adapter.ArticleItemAdapter
import com.example.informationapplication.ui.home.entity.ArticleItem


private const val ARG_PARAM1 = "param1"

/**
 * viewPager子布局
 */
class SubInformationFragment : Fragment() {

    private var title: String? = null
    private val titleMap: Map<String, String> =
        mapOf(
            Pair("国家政策", "zcdh/"),
            Pair("院校政策", "yxzc/"),
            Pair("考研动态", "kydt/"),
            Pair("复试指导", "fstj/"),
            Pair("专硕巡展", "zsxz/"),
            Pair("推免面试", "zsjz/tmjz/")
        )
    private val pageMap:Map<String,Int> = mapOf(
        Pair("国家政策",40),
        Pair("院校政策",80),
        Pair("考研动态",50),
        Pair("复试指导",60),
        Pair("专硕巡展",30),
        Pair("推免面试",80),
    )
    private var _binding: FragmentSubInformationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubInformationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val informationModel =
            ViewModelProvider(this)[InformationModel::class.java]
        val tail = titleMap[title]
        Log.d("tail", tail.toString())
        informationModel.setUrl("https://yz.chsi.com.cn/kyzx/$tail")
        informationModel.setPage(pageMap[title])
        val articleItemList:List<ArticleItem> = ArrayList()
        val adapter = ArticleItemAdapter(articleItemList)

        val swipeRefreshLayout: SwipeRefreshLayout = binding.articleSwipeRefresh


        informationModel.getArticleItems().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setArticleList(it)
                adapter.notifyDataSetChanged()
            }
        }

        val recyclerView = binding.articleRecyclerView

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        adapter.setOnLoadMoreListener(object : ArticleItemAdapter.OnLoadMoreListener {
            override fun onLoadMore(currentPage: Int) {
                informationModel.loadMore(adapter,currentPage)
            }
        })
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener {
            informationModel.refreshArticleItems(adapter,swipeRefreshLayout)
        }
        return root
    }

    companion object {
        /**
         * 工厂方法
         * @param title 标题属性
         * @return 新实例 SubInformationFragment.
         */
        @JvmStatic
        fun newInstance(title: String) =
            SubInformationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, title)
                }
            }
    }
}