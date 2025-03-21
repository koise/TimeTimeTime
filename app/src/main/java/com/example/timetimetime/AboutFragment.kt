package com.example.timetimetime

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardDeveloperInfo = view.findViewById<View>(R.id.cardDeveloperInfo)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)

        cardDeveloperInfo.setOnClickListener {
            val facebookUrl = "https://www.facebook.com/QuezNPatricia"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl))
            startActivity(intent)
        }

        tvEmail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:npenriquez0555ant@student.fatima.edu.ph")
            }
            startActivity(emailIntent)
        }
    }
}
