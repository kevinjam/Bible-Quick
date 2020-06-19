package com.thinkdevs.bibleQuiz.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.thinkdevs.bibleQuiz.R
import com.thinkdevs.bibleQuiz.SetFragment

class SetAdapter(
    var set: Int?,
    var category: String?,
    var type: String?,
    var navfragment: Fragment
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View
        view = if (convertView == null) {
            LayoutInflater.from(parent?.context).inflate(
                R.layout.set_items,
                parent, false
            )
        } else {
            convertView
        }

        view.setOnClickListener {

            println("SET CATEGORU " + category)
            println("SET CATEGORU++++ " + set)
            val bundle = Bundle()
            bundle.putString("category", category)
            bundle.putInt("setNo", position+1)
            navfragment
                .findNavController()
                .navigate(R.id.questionFragment, bundle)
        }
        view.findViewById<TextView>(R.id.content).text = (position + 1).toString()
        view.findViewById<TextView>(R.id.tv_desc).text = type

        return view
    }

    override fun getItem(position: Int): Any? {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return set!!
    }
}