package com.example.informationapplication.ui.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.informationapplication.R
import com.example.informationapplication.ui.home.entity.ArticleItem
import com.example.informationapplication.ui.home.utils.RecyclerOnScrollerListener


class ArticleItemAdapter(private var items: List<ArticleItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mContext: Context
    private lateinit var listener: RecyclerOnScrollerListener
    private var canLoadMore: Boolean = true
    private lateinit var animation: Animation

    private val VIEW_TYPE_CONTENT = 0
    private val VIEW_TYPE_FOOTER = 1

    inner class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var itemTitle: TextView = view.findViewById(R.id.itemTitle)
        var itemDate: TextView = view.findViewById(R.id.itemDate)
    }

    inner class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun showTextOnly(s: String) {
            Log.d("Adapt test", "show Loading")
            ivImage.visibility = View.INVISIBLE
            tvName.text = s
        }

        fun showLoading() {
            Log.i("mytest", "show loading")
            ivImage.setImageResource(R.mipmap.ic_launcher)
            tvName.text = "正在加载"
            ivImage.startAnimation(animation)
        }

        var tvName: TextView = itemView.findViewById(R.id.tv_name)
        var ivImage: ImageView = itemView.findViewById(R.id.iv_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        animation = AnimationUtils.loadAnimation(mContext, R.anim.loading)
        animation.interpolator = LinearInterpolator()
        return if (viewType == VIEW_TYPE_CONTENT) {
            ContentViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.article_item, parent, false)
            )
        } else {
            FooterViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.footer_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_CONTENT) {
            val item = items[position]
            val contentViewHolder = holder as ContentViewHolder
            contentViewHolder.itemTitle.text = item.title
            contentViewHolder.itemDate.text = item.date
        } else {
            val footerViewHolder = holder as FooterViewHolder
            if (canLoadMore) {
                footerViewHolder.showLoading()
            } else {
                footerViewHolder.showTextOnly("无更多数据")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            VIEW_TYPE_FOOTER
        } else
            VIEW_TYPE_CONTENT
    }


    override fun getItemCount() = items.size + 1

    fun setArticleList(newItems: List<ArticleItem>) {
        items = newItems
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        listener = object : RecyclerOnScrollerListener(recyclerView) {
            override fun onLoadMore(currentPage: Int) {
                Log.i("loadingtest", "currentPage: $currentPage")
                mOnLoadMoreListener?.onLoadMore(currentPage)
            }
        }
        Log.d("test",mOnLoadMoreListener.toString())
        recyclerView.addOnScrollListener(listener)
    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerView.removeOnScrollListener(listener)
    }


    /*
    * 数据加载完毕时执行setCanLoadMore()，此时isLoading都置为false
    * */
    fun setCanLoadMore(isCanLoadMore: Boolean) {
        Log.d("set","false")
        canLoadMore = isCanLoadMore
        listener.setCanLoadMore(isCanLoadMore)
        listener.setLoading(false)
    }


    interface OnLoadMoreListener {
        fun onLoadMore(currentPage: Int)
    }

    private var mOnLoadMoreListener: OnLoadMoreListener? = null

    fun setOnLoadMoreListener(listener: OnLoadMoreListener?) {
        mOnLoadMoreListener = listener
    }

}