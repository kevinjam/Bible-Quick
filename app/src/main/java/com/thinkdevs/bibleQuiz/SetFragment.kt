package com.thinkdevs.bibleQuiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdView
import com.thinkdevs.bibleQuiz.adapter.SetAdapter
import com.thinkdevs.bibleQuiz.utility.loadAds


class SetFragment : Fragment() {

    private var gridview: GridView? = null
    private lateinit var mAdView: AdView
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_set, container, false)
        val title = arguments?.getString("title")
        val type = arguments?.getString("type")
        val sets = arguments?.getInt("sets")

        mAdView = view.findViewById(R.id.adView)
        loadAds( view,mAdView)
        getToolbar(view)
        gridview = view.findViewById(R.id.gridview)
        val adapter = SetAdapter(sets!!, title,type,this)
        gridview?.adapter = adapter
        return view
    }

    private fun getToolbar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.title = "Choose Question set"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        view.findViewById<Toolbar>(R.id.toolbar).setOnClickListener {
            findNavController().navigate(R.id.action_setFragment_to_SecondFragment)
        }
    }
}