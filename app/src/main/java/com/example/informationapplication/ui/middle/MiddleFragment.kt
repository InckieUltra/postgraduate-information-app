package com.example.informationapplication.ui.middle

import android.os.Bundle
import android.util.Log
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
import kotlin.math.log

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

        val textView: TextView = binding.currentDateText
        // choosing a certain date on calendarView
        val today: Date = DatesUtil.getDayBegin()
        val calendarView: CalendarView = binding.calendarView
        calendarView.setOnDateChangeListener {
                calendarView, year, month, day ->
                    val toast: Toast = Toast.makeText(calendarView.context, "您选择了" + year + "年" + month + "月" + day + "日。", Toast.LENGTH_SHORT)
                    toast.show()

                    val cal: Calendar = Calendar.getInstance()
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
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}