package com.example.informationapplication.ui.right

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.informationapplication.R
import com.example.informationapplication.databinding.FragmentRightBinding
import com.example.informationapplication.ui.right.DBOpenHelper.closeAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import java.io.File
import java.lang.Thread.sleep
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class RightFragment : Fragment() {

    private var _binding: FragmentRightBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var danmakuContext: DanmakuContext = DanmakuContext.create()
    val list: MutableList<Danmu> = ArrayList<Danmu>()
    var uid:Long = -1
    var exit:Boolean=false
    val format:SimpleDateFormat= SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var mydancolor: Int = Color.BLUE;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rightViewModel =
            ViewModelProvider(this).get(RightViewModel::class.java)

        _binding = FragmentRightBinding.inflate(inflater, container, false)
        var lastFetch: String = "2022-01-01 00:00:00"

        val prefs = requireActivity().getSharedPreferences("danconfig",0)
        mydancolor = prefs.getInt("color",Color.BLUE)

        val f = File(
            "/data/data/com.example.informationapplication/shared_prefs/userinfo.xml"
        )
        if (f.exists()){
            Log.d("TAG", "SharedPreferences Name_of_your_preference : exist")
            val prefs = requireActivity().getSharedPreferences("userinfo",0)
            uid = prefs.getLong("uid",-1)
        } else {
            Log.d("TAG", "Setup default preferences")
            thread {
                var conn1: Connection? = null //打开数据库对xiang
                conn1 = DBOpenHelper.conn
                var ps1: PreparedStatement? = null //操作整合sql语句的对象
                var rs1: ResultSet? = null //查询结果的集合//结果存放集合
                try {
                    Log.d("111", "thread1 begin")
                    //MySQL 语句
                    val sql1 = "insert into user(uid) VALUES(null)"
                    val sql2 = "select last_insert_id() as uid"
                    if (conn1 != null && !conn1.isClosed()) {
                        Log.d("111", "conn success")
                        ps1 = conn1.prepareStatement(sql1) as PreparedStatement
                        if (ps1 != null) {
                            ps1.executeUpdate()
                        }
                        ps1 = conn1.prepareStatement(sql2) as PreparedStatement
                        if (ps1 != null) {
                            rs1 = ps1.executeQuery()
                            if (rs1 != null) {
                                rs1.next()
                                uid = rs1.getLong("uid")
                                Log.d("111",uid.toString())
                                val prefs =
                                    this.activity?.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
                                val editor = prefs?.edit()
                                editor?.putLong("uid", uid)
                                editor?.commit()
                            }
                        }
                    }
                } catch (e: SQLException) {
                    closeAll(conn1,ps1,rs1)
                    e.printStackTrace()
                }
                closeAll(conn1,ps1,rs1)
            }
        }
        thread {
            var conn: Connection? = null //打开数据库对xiang
            conn = DBOpenHelper.conn
            var ps: PreparedStatement? = null //操作整合sql语句的对象
            var rs: ResultSet? = null //查询结果的集合//结果存放集合
            while(true) {
                list.clear()
                if(exit)break
                try {
                    Log.d("t1",uid.toString()+" "+lastFetch)

                    var Cal:Calendar = java.util.Calendar.getInstance();

                    Cal.setTime(Date());

                    Cal.add(java.util.Calendar.MINUTE,-5);

                    //MySQL 语句
                    val sql = "select * from danmu where time>\'"+lastFetch+"\' and time > \'"+format.format(Cal.getTime())+"\' order by time limit 5"
                    if (conn != null && ! conn.isClosed()) {
                        //Log.d("t1", "conn success")
                        ps = conn.prepareStatement(sql) as PreparedStatement
                        if (ps != null) {
                            rs = ps.executeQuery()
                            if (rs != null) {
                                while (rs.next()) {
                                    //Log.d("t1", "rs next")
                                    Log.d("t1", rs.getString("content"))
                                    val d = Danmu()
                                    d.content = rs.getString("content")
                                    d.uid = rs.getLong("uid")
                                    d.color = rs.getInt("color")
                                    if(d.uid!=uid)
                                        list.add(d)
                                    lastFetch = rs.getString("time")
                                }
                            }
                        }
                    }
                } catch (e: SQLException) {
                    closeAll(conn,ps,rs)
                    e.printStackTrace()
                }
                GlobalScope.launch(Dispatchers.Main) {
                    //GlobalScope开启协程：main
                    Log.d("TAG", "GlobalScope开启协程：" + Thread.currentThread().name)
                    //可以做UI操作
                    for(item in list) {
                        addDanmaku(false, item.content,false, item.color)
                    }
                }
                sleep(1200)
            }
            closeAll(conn,ps,rs)
        }
        // 滚动弹幕最大显示4行
        var maxLinesPair: HashMap<Int, Int> = HashMap<Int, Int>()
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5)
        //设置是否禁止重叠
        var overlappingEnablePair: HashMap<Int, Boolean> = HashMap<Int, Boolean>()
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_LR, true)
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_BOTTOM, true)

        var danmakuView=binding.danmaku

        //初始化配置
        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3.toFloat()) //设置描边样式
            .setDuplicateMergingEnabled(false)//是否启用合并重复弹幕
            .setScrollSpeedFactor(1.2f)   //设置弹幕滚动速度系数,只对滚动弹幕有效
            .setScaleTextSize(1.2f)
            .setCacheStuffer(SpannedCacheStuffer(), null) // 图文混排使用SpannedCacheStuffer  设置缓存绘制填充器，默认使用{@link SimpleTextCacheStuffer}只支持纯文字显示, 如果需要图文混排请设置{@link SpannedCacheStuffer}如果需要定制其他样式请扩展{@link SimpleTextCacheStuffer}|{@link SpannedCacheStuffer}
            .setMaximumLines(maxLinesPair) //设置最大显示行数
            .preventOverlapping(overlappingEnablePair); //设置防弹幕重叠，null为允许重叠

        //设置解析器
        var baseDanmakuParser: BaseDanmakuParser = object : BaseDanmakuParser() {
            override fun parse() = Danmakus()
        }


        //配置弹幕
        danmakuView.setCallback(object : DrawHandler.Callback {
            override fun prepared() {
                // 弹幕准备完成
                Log.d("DFM", "prepared")
                danmakuView.start()
            }

            override fun updateTimer(timer: DanmakuTimer) {
                // 弹幕播放时间
                //Log.d("DFM", "updateTimer : timer = " + timer.currMillisecond)
            }

            override fun danmakuShown(danmaku: BaseDanmaku) {
                // 播放新的一条弹幕
                Log.d("DFM", "danmakuShown : danmaku = " + danmaku.text)
            }

            override fun drawingFinished() {
                // 弹幕播放结束
                Log.d("DFM", "drawingFinished")
            }
        });//本来是this！！！！！！！
        danmakuView.prepare(baseDanmakuParser, danmakuContext);
        danmakuView.showFPS(false); //是否显示FPS
        danmakuView.enableDanmakuDrawingCache(true);
        binding.dansubmit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val inputcontent:String = binding.daninput.text.toString()
                hideKeyboard(binding.root)
                binding.daninput.setText("")
                if(inputcontent=="")
                    return
                addDanmaku(true,inputcontent,true, mydancolor)
                thread {
                    var conn2: Connection? = null //打开数据库对xiang
                    conn2 = DBOpenHelper.conn
                    var ps2: PreparedStatement? = null //操作整合sql语句的对象
                    var rs2: ResultSet? = null //查询结果的集合//结果存放集合
                    try {
                        Log.d("t2", "thread2 begin")
                        val currenttime=format.format(Date())
                        Log.d("t2",currenttime)
                        //MySQL 语句
                        val sql = "insert into danmu(time,uid,content,color) VALUES(\""+currenttime+"\","+uid.toString()+",\""+inputcontent+"\","+mydancolor+")"
                        Log.d("t2",sql)
                        var ps2: PreparedStatement? = null //操作整合sql语句的对象
                        var rs2: ResultSet? = null //查询结果的集合//结果存放集合
                        if (conn2 != null && !conn2.isClosed()) {
                            Log.d("111", "conn success")
                            ps2 = conn2.prepareStatement(sql) as PreparedStatement
                            if (ps2 != null) {
                                ps2.executeUpdate()
                            }
                        }
                    } catch (e: SQLException) {
                        closeAll(conn2,ps2,rs2)
                        e.printStackTrace()
                    }
                    closeAll(conn2,ps2,rs2)
                }
            }
        })
        binding.reddan.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?){
                mydancolor = Color.RED;
                val prefs =
                    requireActivity().getSharedPreferences("danconfig", Context.MODE_PRIVATE)
                val editor = prefs?.edit()
                editor?.putInt("color", Color.RED)
                editor?.commit()
                if (binding.changedancolor != null && binding.changedancolor.isExpanded()) {
                    binding.changedancolor.collapse();
                }
            }
        })
        binding.bluedan.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?){
                mydancolor = Color.BLUE;
                val prefs =
                    requireActivity().getSharedPreferences("danconfig", Context.MODE_PRIVATE)
                val editor = prefs?.edit()
                editor?.putInt("color", Color.BLUE)
                editor?.commit()
                if (binding.changedancolor != null && binding.changedancolor.isExpanded()) {
                    binding.changedancolor.collapse();
                }
            }
        })
        binding.greendan.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?){
                mydancolor = Color.GREEN;
                val prefs =
                    requireActivity().getSharedPreferences("danconfig", Context.MODE_PRIVATE)
                val editor = prefs?.edit()
                editor?.putInt("color", Color.GREEN)
                editor?.commit()
                if (binding.changedancolor != null && binding.changedancolor.isExpanded()) {
                    binding.changedancolor.collapse();
                }
            }
        })
        val root: View = binding.root
        Glide.with(this).load(R.drawable.bg3).into(binding.danimage)
        /*val textView: TextView = binding.textNotifications
        rightViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        exit=true
    }



    fun addDanmaku(isLive: Boolean,  sendContent: String, isMine: Boolean, dancolor: Int) {
        var danmakuView=binding.danmaku
        if (danmakuView == null || danmakuContext == null) {
            return;
        }
        //创建一个弹幕对象，这里后面的属性是设置滚动方向的！
        val danmaku: BaseDanmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || sendContent == null) {
            return;
        }
        //弹幕显示的文字
        danmaku.text = sendContent;

        //设置相应的边距，这个设置的是四周的边距
        danmaku.padding = 5;
        // 可能会被各种过滤器过滤并隐藏显示，若果是本机发送的弹幕，建议设置成1；
        danmaku.priority = 0;
        //是否是直播弹幕
        danmaku.isLive = isLive;
        danmaku.setTime(danmakuView.getCurrentTime() + 3600);
        //设置文字大小
        danmaku.textSize = 50.toFloat();
        //设置文字颜色
        danmaku.textColor = dancolor;
        //设置阴影的颜色
        danmaku.textShadowColor = Color.WHITE;
        if(isMine) {
            //设置线颜色
            danmaku.underlineColor = Color.GREEN
            //设置背景颜色
            danmaku.borderColor = Color.GREEN;
        }
        else {
            danmaku.underlineColor = Color.TRANSPARENT
            danmaku.borderColor = Color.TRANSPARENT
        }
        //添加这条弹幕，也就相当于发送
        danmakuView.addDanmaku(danmaku);
    }
}

fun hideKeyboard(view: View) {
    val imm = view.context
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}