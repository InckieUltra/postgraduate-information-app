package com.example.informationapplication.ui.right

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.informationapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.lang.Thread.sleep
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/**
 * Created by Xinghai.Zhao on 2020/8/4.
 * 自定义选择弹框
 */
class ImageDialog(context: Context?) : AlertDialog(context){

    var myimage: ImageView?= null
    var wcurl: String ?= null
    var myBitmap: Bitmap ?= null

    constructor(context: Context?, url: String) : this(context) {
        wcurl = url
    }

    init {
        val inflate = LayoutInflater.from(context).inflate(R.layout.wordcloud_danmaku, null);
        setView(inflate)
        //设置点击别的区域不关闭页面
        setCancelable(true)
        val progbar: ProgressBar = inflate.findViewById<ProgressBar>(R.id.wc_bar)
        progbar.setProgress(25,true)
        // 开启一个单独线程进行网络读取
        Thread(Runnable {
            var bitmap: Bitmap ? = null
            try {
                // 根据URL 实例， 获取HttpURLConnection 实例
                val url = URL(wcurl)
                var httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                // 设置读取 和 连接 time out 时间
                httpURLConnection.readTimeout = 2000
                httpURLConnection.connectTimeout = 2000
                // 获取图片输入流
                var inputStream = httpURLConnection.inputStream
                GlobalScope.launch(Dispatchers.Main) {
                    //可以做UI操作
                    val myimage = inflate.findViewById<ImageView>(R.id.wc_danmaku)
                    myimage.setImageBitmap(bitmap)
                    progbar.setProgress(50,true)
                }
                // 获取网络响应结果
                var responseCode = httpURLConnection.responseCode
                sleep(1000)

                // 获取正常
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 解析图片
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    GlobalScope.launch(Dispatchers.Main) {
                        //可以做UI操作
                        val myimage = inflate.findViewById<ImageView>(R.id.wc_danmaku)
                        myimage.setImageBitmap(bitmap)
                        progbar.setProgress(100,true)
                    }
                }
            } catch(e: IOException) { // 捕获异常 (例如网络异常)

            }
        }).start()

    }


}