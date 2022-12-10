package com.example.informationapplication.ui.middle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.informationapplication.R

class ScheduleAdapter(private val scheduleList: List<Schedule>): RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val scheduleTitle: TextView = view.findViewById(R.id.scheduleTitle)
        val scheduleContent: TextView = view.findViewById(R.id.scheduleContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule = scheduleList[position]
        holder.scheduleTitle.text = schedule.title
        holder.scheduleContent.text = schedule.content
    }

    override fun getItemCount(): Int = scheduleList.size

}