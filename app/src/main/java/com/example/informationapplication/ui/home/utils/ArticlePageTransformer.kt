package com.example.informationapplication.ui.home.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 * google 的源码教程改编
 * position 0 表示当前页面 -1 表示左侧待加载界面， 1表示右侧代价在界面
 */
class ArticlePageTransformer : ViewPager2.PageTransformer {
    private val MIN_SCALE = 0.75f

    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        if (position < -1) { //待加载fragment还很远
            // 不显示
            page.alpha = 0f
        } else if (position <= 0) { // [-1,0]
            // 左侧界面到中央或者是左侧界面划走都是默认变换
            page.alpha = 1f
            page.translationX = 0f
            page.scaleX = 1f
            page.scaleY = 1f
        } else if (position <= 1) { // (0,1]
            // 透明度等于1 - position
            page.alpha = 1 - position

            // 右侧页面向左侧平移
            page.translationX = pageWidth * -position

            // 放缩因子
            val scaleFactor = (MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position)))
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
        } else { // 右侧代显示界面
            page.alpha = 0f
        }
    }
}