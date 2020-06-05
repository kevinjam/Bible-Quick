package com.thinkdevs.bibleQuiz

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thinkdevs.bibleQuiz.adapter.BookmarkAdapter
import com.thinkdevs.bibleQuiz.model.Question
import com.thinkdevs.bibleQuiz.utility.loadAds

/**
 * A fragment representing a list of Items.
 */
class BookmarkFragment : Fragment() {

    private var recyclerView: RecyclerView? = null

    private var preference: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var gson: Gson? = null

    private var bookmarkList: ArrayList<Question>? = null
    private var matchedQuestionPosition: Int? = null
    private lateinit var mAdView: AdView
    private lateinit var toolbar: Toolbar



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookmark_list, container, false)
        var toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        // Set the adapter
        mAdView= view.findViewById(R.id.adView)
        preference = activity?.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        editor = preference?.edit()
        gson = Gson()
        getToolbar(view)
        loadAds(view, mAdView)
        getBookmarks()

        recyclerView = view.findViewById(R.id.bookmark_recycler)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView?.layoutManager = layoutManager

        val adapter = BookmarkAdapter(bookmarkList!!)
        recyclerView?.adapter = adapter
        return view
    }

    private fun getToolbar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.title = "Bookmarks"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        view.findViewById<Toolbar>(R.id.toolbar).setOnClickListener {
            findNavController().navigate(R.id.action_bookmarkFragment_to_FirstFragment)
        }
    }

    private fun getBookmarks() {
        var json = preference!!.getString(KEY_NAME, "")
        val type = object : TypeToken<List<Question>>() {}.getType()
        bookmarkList = gson!!.fromJson(json, type)

        if (bookmarkList == null) {
            bookmarkList = ArrayList()
        }
    }



    override fun onPause() {
        super.onPause()
        storeBookmarks()
    }

    private fun storeBookmarks() {
        val json = gson?.toJson(bookmarkList)
        editor?.putString(KEY_NAME, json)
        editor?.commit()
    }

    companion object {

        private var FILE_NAME: String = "QUIZZER"
        private var KEY_NAME: String = "QUESTIONS"
    }

}