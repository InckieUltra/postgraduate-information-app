package com.example.informationapplication.ui.middle

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.informationapplication.MainActivity
import com.example.informationapplication.R
import com.example.informationapplication.databinding.ActivityScheduleBinding
import java.util.*

class ScheduleActivity : AppCompatActivity() {
    private var _binding: ActivityScheduleBinding? = null
    private val binding get() = _binding!!

    private var initSchedule: Schedule? = null
    private val db: ScheduleDataHelper = ScheduleDataHelper(this, 1)
    private lateinit var dateTextView: TextView
    private lateinit var cancelButton: Button
    private lateinit var editButton: Button
    private lateinit var titleText: EditText
    private lateinit var contentText: EditText
    private lateinit var tagSelector: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        _binding = ActivityScheduleBinding.inflate(layoutInflater)
        dateTextView = binding.dateText
        cancelButton = binding.cancel
        editButton = binding.edit
        titleText = binding.titleText
        contentText = binding.contentText
        tagSelector = binding.tag
        val root: View = binding.root
        setContentView(root)

        // TODO fill initSchedule

        // show date text
        var dateText: String = "日期: "
        dateText += (intent.getIntExtra("year", 2022)).toString() + "年" +
                (intent.getIntExtra("month", 1)).toString() + "月" +
                intent.getIntExtra("date", 1).toString() + "日" +
                (when (intent.getIntExtra("day", -1)) {
                    1 -> " 星期一"
                    2 -> " 星期二"
                    3 -> " 星期三"
                    4 -> " 星期四"
                    5 -> " 星期五"
                    6 -> " 星期六"
                    0 -> " 星期日"
                    else -> ""
                })
        dateTextView.text = dateText

        // click cancel button
        cancelButton.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        when{
            intent.getBooleanExtra("isNew", true) -> editSchedule()
            else -> checkSchedule(initSchedule) // TODO show a schedule
        }
    }

    private fun editSchedule() {
        editButton.text = "确定"

        titleText.focusable = View.FOCUSABLE
        titleText.isFocusableInTouchMode = true
        titleText.requestFocus()

        contentText.focusable = View.FOCUSABLE
        contentText.isFocusableInTouchMode = true
        contentText.requestFocus()

        editButton.setOnClickListener {
            // done
            db.addOrUpdateSchedule(this.createNewSchedule())
            val intent: Intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun checkSchedule(schedule: Schedule?){
        editButton.text = "编辑"

        // TODO show a schedule
        titleText.setText(initSchedule!!.title)
        titleText.focusable = View.NOT_FOCUSABLE
        titleText.isFocusableInTouchMode = false

        contentText.setText(initSchedule!!.content)
        contentText.focusable = View.NOT_FOCUSABLE
        contentText.isFocusableInTouchMode = false
        //contentText.setText("test")

        editButton.setOnClickListener {
            // edit
            editSchedule()
        }
    }

    private fun createNewSchedule(): Schedule {
        var schedule: Schedule = Schedule()
        val cal = Calendar.getInstance()
        cal.set(intent.getIntExtra("year", 2022) - 1900,
            intent.getIntExtra("month", 1) - 1,
            intent.getIntExtra("date", 1))
        schedule.date = cal.time
        schedule.title = titleText.text.toString()
        schedule.content = contentText.text.toString()
        if (initSchedule != null) {
            schedule.id = initSchedule!!.id
        }
        schedule.tag = tagSelector.checkedRadioButtonId.toString()
        return schedule
    }
}