package com.thinkdevs.bibleQuiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdView
import com.thinkdevs.bibleQuiz.utility.loadAds


class ScoreFragment : Fragment() {

    private lateinit var scored :TextView
    private lateinit var totalView :TextView
    private lateinit var mAdView: AdView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_score, container, false)

        var score = arguments?.getInt("score")
        var total = arguments?.getInt("total")

        mAdView = view.findViewById(R.id.adView)
        loadAds(view, mAdView)

        scored = view.findViewById(R.id.score)
        totalView = view.findViewById(R.id.total)

        scored.text = score.toString()
        totalView.text ="OUT OF ${total}"

        view.findViewById<Button>(R.id.done).setOnClickListener {
            findNavController().navigate(R.id.action_scoreFragment_to_FirstFragment)
        }


        return view
    }
}