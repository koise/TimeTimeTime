package com.example.timetimetime

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.*

class SelectTimezoneActivity : AppCompatActivity() {

    private lateinit var timezoneAdapter: TimezoneAdapter
    private val timezoneList: MutableList<TimezoneItem> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var btnBack: ImageButton
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_select_timezone)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewTimezones)
        searchView = findViewById(R.id.searchView)
        btnBack = findViewById(R.id.btnBack)

        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        timezoneAdapter = TimezoneAdapter(timezoneList) { selectedTimezone ->
            showAddTimezoneDialog(selectedTimezone)
        }
        recyclerView.adapter = timezoneAdapter

        // Populate Timezones
        populateTimezoneList()

        // Start updating time dynamically
        startUpdatingTime()

        // Handle Search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTimezones(newText)
                return true
            }
        })

        // Handle Back Button
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun populateTimezoneList() {
        timezoneList.apply {
            add(TimezoneItem("New York (EST)", getCurrentTime("America/New_York")))
            add(TimezoneItem("London (GMT)", getCurrentTime("Europe/London")))
            add(TimezoneItem("Tokyo (JST)", getCurrentTime("Asia/Tokyo")))
            add(TimezoneItem("Sydney (AEST)", getCurrentTime("Australia/Sydney")))
            add(TimezoneItem("Berlin (CET)", getCurrentTime("Europe/Berlin")))
            add(TimezoneItem("Dubai (GST)", getCurrentTime("Asia/Dubai")))
        }
        timezoneAdapter.notifyDataSetChanged()
    }

    private fun getCurrentTime(timezone: String): String {
        val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone(timezone)
        return sdf.format(Calendar.getInstance().time)
    }

    private fun startUpdatingTime() {
        handler.post(object : Runnable {
            override fun run() {
                timezoneList.forEachIndexed { index, timezoneItem ->
                    timezoneItem.time = getCurrentTime(getTimezoneID(timezoneItem.name))
                }
                timezoneAdapter.notifyDataSetChanged()
                handler.postDelayed(this, 1000) // Update every second
            }
        })
    }

    private fun getTimezoneID(cityName: String): String {
        return when {
            cityName.contains("New York") -> "America/New_York"
            cityName.contains("London") -> "Europe/London"
            cityName.contains("Tokyo") -> "Asia/Tokyo"
            cityName.contains("Sydney") -> "Australia/Sydney"
            cityName.contains("Berlin") -> "Europe/Berlin"
            cityName.contains("Dubai") -> "Asia/Dubai"
            else -> "UTC"
        }
    }

    private fun showAddTimezoneDialog(selectedTimezone: TimezoneItem) {
        AlertDialog.Builder(this)
            .setTitle("Add Timezone")
            .setMessage("Do you want to add \"${selectedTimezone.name}\" at ${selectedTimezone.time}?")
            .setPositiveButton("Yes") { _, _ ->
                Toast.makeText(this, "\"${selectedTimezone.name}\" added!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun filterTimezones(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            timezoneList
        } else {
            timezoneList.filter { it.name.contains(query, ignoreCase = true) }
        }
        timezoneAdapter.updateList(filteredList)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null) // Stop updating when activity is destroyed
        super.onDestroy()
    }
}
