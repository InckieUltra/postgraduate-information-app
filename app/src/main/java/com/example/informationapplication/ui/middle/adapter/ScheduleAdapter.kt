package com.example.informationapplication.ui.middle.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.informationapplication.R
import com.example.informationapplication.ui.middle.MiddleFragment
import com.example.informationapplication.ui.middle.ScheduleActivity
import com.example.informationapplication.ui.middle.entity.Schedule

class ScheduleAdapter(private val scheduleList: MutableList<Schedule>, private val activity: Fragment?): RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val scheduleTitle: TextView = view.findViewById(R.id.scheduleTitle)
        val scheduleContent: TextView = view.findViewById(R.id.scheduleContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
        val viewHolder: ViewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val schedule = scheduleList[position]
            val intent: Intent = Intent(parent.context, ScheduleActivity::class.java)
            intent.putExtra("id", schedule.id)
            intent.putExtra("title", schedule.title)
            intent.putExtra("content", schedule.content)
            intent.putExtra("year", schedule.date.year+3800)
            intent.putExtra("month", schedule.date.month)
            intent.putExtra("day", schedule.date.day)
            intent.putExtra("date", schedule.date.date)
            intent.putExtra("tag", schedule.tag)
            intent.putExtra("isNew", false)
            activity!!.startActivity(intent)
        }

        viewHolder.itemView.setOnLongClickListener {
            val position = viewHolder.bindingAdapterPosition
            val menu = PopupMenu(parent.context, it)
            menu.menuInflater.inflate(R.menu.schedule_menu, menu.menu)
            menu.setForceShowIcon(true)
            menu.setOnMenuItemClickListener {
                (activity as MiddleFragment).deleteSchedule(scheduleList[position])
                return@setOnMenuItemClickListener true
            }
            menu.show()
            return@setOnLongClickListener true
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule = scheduleList[position]
        holder.scheduleTitle.text = schedule.title
        holder.scheduleContent.text = schedule.content
    }

    override fun getItemCount(): Int = scheduleList.size

}