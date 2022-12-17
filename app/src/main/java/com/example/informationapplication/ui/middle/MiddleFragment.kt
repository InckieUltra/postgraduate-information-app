package com.example.informationapplication.ui.middle

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.informationapplication.databinding.FragmentMiddleBinding
import com.example.informationapplication.ui.middle.adapter.ScheduleAdapter
import com.example.informationapplication.ui.middle.dbhelper.ScheduleDataHelper
import com.example.informationapplication.ui.middle.entity.Schedule
import com.example.informationapplication.ui.middle.utils.DatesUtil
import java.util.*

class MiddleFragment : Fragment() {
    private var _binding: FragmentMiddleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: MiddleViewModel
    private lateinit var scheduleDB: ScheduleDataHelper
    private var adapter: ScheduleAdapter? = null
    private var chosenDate: String = "1900-1-1"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val middleViewModel =
            ViewModelProvider(this)[MiddleViewModel::class.java]
        viewModel = middleViewModel

        _binding = FragmentMiddleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val db = ScheduleDataHelper(this.context, 1)
        scheduleDB = db

        val textView: TextView = binding.currentDateText
        // choosing a certain date on calendarView
        val today: Date = DatesUtil.getDayBegin()
        chosenDate = DatesUtil.KotlinDateToString(today)

        val recyclerView = binding.recycler
        refresh(middleViewModel, db, chosenDate)
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        adapter = ScheduleAdapter(middleViewModel.scheduleList, this)
        adapter!!.notifyDataSetChanged()
        recyclerView.adapter = adapter

        val cal: Calendar = Calendar.getInstance()
        val calendarView: CalendarView = binding.calendarView
        calendarView.setOnDateChangeListener {
                _calendarView, year, month, day ->
                    val toast: Toast = Toast.makeText(_calendarView.context, "您选择了" + year + "年" + (month+1) + "月" + day + "日。", Toast.LENGTH_SHORT)
//                    toast.show()

                    cal.set(year, month, day)
                    val currentChosenDate: Date = cal.time
                    val diff = DatesUtil.getDiffDays(currentChosenDate, today)
                    textView.text = (when{
                        diff > 2 -> diff.toString() + "天后"
                        diff < -2 -> (-diff).toString() + "天前"
                        diff == 0 -> "今天"
                        diff == 1 -> "明天"
                        diff == 2 -> "后天"
                        diff == -1 -> "昨天"
                        diff == -2 -> "前天"
                        else -> ""
                    })

                    chosenDate = DatesUtil.KotlinDateToString(currentChosenDate)
                    refresh(middleViewModel, db, chosenDate)
                    adapter = ScheduleAdapter(middleViewModel.scheduleList, this)
                    recyclerView.adapter!!.notifyDataSetChanged()

        }

        val addScheduleButton = binding.addScheduleButton
        addScheduleButton.setOnClickListener{
            val schedule = Schedule()
            schedule.date = cal.time

            val intent: Intent = Intent(this.context, ScheduleActivity::class.java)
            intent.putExtra("year", cal.time.year + 1900)
            intent.putExtra("month", cal.time.month + 1)
            intent.putExtra("date", cal.time.date)
            intent.putExtra("day", cal.time.day)
            intent.putExtra("isNew", true)

            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun refresh(viewModel: MiddleViewModel, db: ScheduleDataHelper, date: String) {
        viewModel.scheduleList.clear()
        val cursor = db.readableDatabase.query("schedule", null, "date = ?",
            arrayOf(date), null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val schedule = Schedule()
                schedule.id = cursor.getInt(cursor.getColumnIndex("id"))
                schedule.title = cursor.getString(cursor.getColumnIndex("title"))
                schedule.content = cursor.getString(cursor.getColumnIndex("content"))
                schedule.date = DatesUtil.toDate(date)
                schedule.tag = cursor.getString(cursor.getColumnIndex("tag"))
                viewModel.scheduleList.add(schedule)
            } while (cursor.moveToNext())
        }
        cursor.close()
        binding.recycler.adapter = ScheduleAdapter(viewModel.scheduleList, this)
    }


    fun deleteSchedule (schedule: Schedule) {
        scheduleDB.deleteSchedule(schedule)
        refresh(viewModel, scheduleDB, chosenDate)
    }

    override fun onResume() {
        super.onResume()
        refresh(viewModel, scheduleDB, chosenDate)
    }

    override fun onStart() {
        super.onStart()
        refresh(viewModel, scheduleDB, chosenDate)
    }

}