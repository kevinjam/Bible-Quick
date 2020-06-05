package com.thinkdevs.bibleQuiz.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.thinkdevs.bibleQuiz.R
import com.thinkdevs.bibleQuiz.model.Question


class BookmarkAdapter(
    private val questionList: ArrayList<Question>
) : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bookmarks, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews( questionList[position])
    }

    override fun getItemCount(): Int = questionList.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var questionView: TextView?= null
        var answer: TextView?= null
        var deleteBtn: ImageView?= null

        fun bindViews(model: Question) {
            questionView = itemView.findViewById(R.id.question_txt)
            answer = itemView.findViewById(R.id.answer)
            deleteBtn = itemView.findViewById(R.id.deleted)
            questionView?.text = model.question
            answer?.text = model.correctAns

            deleteBtn?.setOnClickListener {
                questionList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)

            }
        }
    }
}