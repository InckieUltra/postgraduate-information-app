package com.example.informationapplication.ui.middle

import android.content.ContentValues
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
import com.example.informationapplication.databinding.FragmentMiddleBinding
import java.util.*

class MiddleFragment : Fragment() {
    private var _binding: FragmentMiddleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val middleViewModel =
            ViewModelProvider(this).get(MiddleViewModel::class.java)

        _binding = FragmentMiddleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val db = ScheduleDataHelper(this.context, 1)

        val textView: TextView = binding.currentDateText
        // choosing a certain date on calendarView
        val today: Date = DatesUtil.getDayBegin()
        val cal: Calendar = Calendar.getInstance()
        val calendarView: CalendarView = binding.calendarView
        calendarView.setOnDateChangeListener {
                calendarView, year, month, day ->
                    val toast: Toast = Toast.makeText(calendarView.context, "您选择了" + year + "年" + (month+1) + "月" + day + "日。", Toast.LENGTH_SHORT)
                    toast.show()

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
                    db.writableDatabase
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

    private fun addSchedule(schedule: Schedule, db: ScheduleDataHelper) {

        val value = ContentValues()
        value.put("title", schedule.title)
        value.put("content", schedule.content)
        value.put("date", (schedule.date.year+1900).toString() + "-" + (schedule.date.month+1).toString() + "-" + schedule.date.date.toString())
        value.put("tag", schedule.tag)

        db.writableDatabase.insert("schedule", null, value)
    }
}