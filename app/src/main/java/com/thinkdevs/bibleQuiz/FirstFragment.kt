package com.thinkdevs.bibleQuiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import java.util.*


class FirstFragment : Fragment() {

    private lateinit var mAdView: AdView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadAds(view)

        view.findViewById<ImageView>(R.id.start_btn).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        view.findViewById<ImageView>(R.id.bookmarks_btn).setOnClickListener {
            findNavController().navigate(R.id.bookmarkFragment)
        }
        view.findViewById<ImageView>(R.id.add_question).setOnClickListener {
           Toast.makeText(activity, "Coming soon", Toast.LENGTH_LONG).show()
        }

        view.findViewById<ImageView>(R.id.about_us).setOnClickListener {
            Toast.makeText(activity, "Coming soon", Toast.LENGTH_LONG).show()
        }

        view.findViewById<TextView>(R.id.hello).text = getGreetingMessage()
    }

    private fun loadAds(view: View) {
        mAdView = view.findViewById(R.id.adView)
        val request = AdRequest
            .Builder()
            .build()
        mAdView.loadAd(request)
    }

    fun getGreetingMessage():String{
        val c = Calendar.getInstance()
        return when (c.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..20 -> "Good Evening"
            in 21..23 -> "Good Night"
            else -> "Hello"
        }
    }
}