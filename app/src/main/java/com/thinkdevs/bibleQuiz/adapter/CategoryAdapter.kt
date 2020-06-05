package com.thinkdevs.bibleQuiz.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.thinkdevs.bibleQuiz.R
import com.thinkdevs.bibleQuiz.model.Category
import com.thinkdevs.bibleQuiz.utility.mColors
import java.util.*

class CategoryAdapter(
    var list: List<Category>,
    var categoryFragment: Fragment
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("onBindViewHolder" + list.count())

        holder.bindView(list[position], categoryFragment)

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var imageView: TextView
        private lateinit var title: TextView
        private lateinit var desc: TextView

        fun bindView(
            items: Category,
            categoryFragment: Fragment?
        ) {
            imageView = itemView.findViewById(R.id.image_view)
            desc = itemView.findViewById(R.id.desc)
            title = itemView.findViewById(R.id.title)
            desc.text = items.desc
            setData(items)

            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("title", items.name)
                bundle.putInt("sets", items.set!!)
                categoryFragment!!.findNavController().navigate(R.id.setFragment, bundle)
//
            }
        }

        private fun setData(model: Category) {
            val  shape = GradientDrawable()
            shape.shape = GradientDrawable.OVAL
            shape.setColor(Color.parseColor ("#"+mColors()[Random().nextInt(254)]))
            imageView.text = model.name!!.substring(0,1).toUpperCase()

            imageView.background = shape
            title.text = model.name


        }





    }

}