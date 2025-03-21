package com.example.timetimetime

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class TimezoneFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TimezoneAdapter
    private val timezoneList = mutableListOf<TimezoneItem>()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_timezone, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewTimezones)
        val fabAddTimezone = view.findViewById<FloatingActionButton>(R.id.fabAddTimezone)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Sample Timezones
        timezoneList.apply {
            add(TimezoneItem("New York (EST)", getCurrentTime("America/New_York")))
            add(TimezoneItem("London (GMT)", getCurrentTime("Europe/London")))
            add(TimezoneItem("Tokyo (JST)", getCurrentTime("Asia/Tokyo")))
        }

        adapter = TimezoneAdapter(timezoneList) { selectedTimezone ->
            handleTimezoneClick(selectedTimezone)
        }
        recyclerView.adapter = adapter

        fabAddTimezone.setOnClickListener {
            val intent = Intent(requireActivity(), SelectTimezoneActivity::class.java)
            startActivity(intent)
        }

        // Start auto-updating time every second
        startUpdatingTime()

        return view
    }

    private fun handleTimezoneClick(timezone: TimezoneItem) {
        Toast.makeText(requireContext(), "Selected: ${timezone.name}", Toast.LENGTH_SHORT).show()
    }

    private fun startUpdatingTime() {
        handler.post(object : Runnable {
            override fun run() {
                timezoneList.forEachIndexed { index, timezoneItem ->
                    timezoneItem.time = getCurrentTime(getTimezoneID(timezoneItem.name))
                }
                adapter.notifyDataSetChanged()
                handler.postDelayed(this, 1000) // Update every second
            }
        })
    }

    private fun getCurrentTime(timezoneID: String): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone(timezoneID)
        return sdf.format(Date())
    }

    private fun getTimezoneID(name: String): String {
        return when {
            name.contains("New York") -> "America/New_York"
            name.contains("London") -> "Europe/London"
            name.contains("Tokyo") -> "Asia/Tokyo"
            name.contains("Sydney") -> "Australia/Sydney"
            name.contains("Berlin") -> "Europe/Berlin"
            name.contains("Dubai") -> "Asia/Dubai"
            else -> "UTC"
        }
    }
}
