package com.example.timetimetime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimezoneAdapter(
    private var timezones: MutableList<TimezoneItem>,
    private val onTimezoneClick: (TimezoneItem) -> Unit
) : RecyclerView.Adapter<TimezoneAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTimezoneName: TextView = itemView.findViewById(R.id.tvTimezoneName)
        val tvTimezoneTime: TextView = itemView.findViewById(R.id.tvTimezoneTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timezone, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timezoneItem = timezones[position]
        holder.tvTimezoneName.text = timezoneItem.name
        holder.tvTimezoneTime.text = timezoneItem.time

        holder.itemView.setOnClickListener {
            onTimezoneClick(timezoneItem)
        }
    }

    override fun getItemCount(): Int = timezones.size

    fun updateList(newList: List<TimezoneItem>) {
        timezones.clear()
        timezones.addAll(newList)
        notifyDataSetChanged()
    }

}
