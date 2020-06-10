package com.thinkdevs.bibleQuiz

import android.animation.Animator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thinkdevs.bibleQuiz.model.Question
import com.thinkdevs.bibleQuiz.utility.loadAds
import org.w3c.dom.Text


class QuestionFragment : Fragment() {

    private lateinit var question: TextView
    private lateinit var indicator: TextView
    private lateinit var bookmarks: FloatingActionButton
    private lateinit var optionsContainer: LinearLayout
    private lateinit var share: AppCompatButton
    private lateinit var next: AppCompatButton
    private var count: Int = 0
    private var questionList: ArrayList<Question>? = null
    private var position = 0
    private var score = 0
    private var database = FirebaseDatabase.getInstance()
    private var reference = database.reference
    private var setNo: Int? = null
    private var category: String? = null

    private var preference: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var gson: Gson? = null

    private var bookmarkList: ArrayList<Question>? = null
    private var matchedQuestionPosition: Int? = null

    private lateinit var mAdView: AdView
    private lateinit var dialog: Dialog


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_question, container, false)
        preference = activity?.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        editor = preference?.edit()
        gson = Gson()
        category = arguments?.getString("category")
        setNo = arguments?.getInt("setNo")

        mAdView = view.findViewById(R.id.adView)
        loadAds(view, mAdView)

        question = view.findViewById(R.id.questions)
        indicator = view.findViewById(R.id.no_indicator)
        bookmarks = view.findViewById(R.id.bookmark_action)
        optionsContainer = view.findViewById(R.id.option_container)
        share = view.findViewById(R.id.share_btn)
        next = view.findViewById(R.id.next_btn)

        questionList = ArrayList<Question>()
        getBookmarks()
        getQuestions()
        // questionList!![position].question?.let { playAnnim(question,0, it) }

        bookmarks.setOnClickListener {
            if (modelMatched()) {
                bookmarkList!!.removeAt(matchedQuestionPosition!!)
                bookmarks.setImageDrawable(activity?.getDrawable(R.drawable.ic_bookmark_border))
            } else {
                bookmarkList!!.add(questionList!![position])
                bookmarks.setImageDrawable(activity?.getDrawable(R.drawable.bookmarked))
            }
        }

        share.setOnClickListener {
            getShareContents()
        }

        return view
    }

    private fun getShareContents() {
        val body = questionList?.get(position)?.question +
                questionList?.get(position)?.optionA + "\n"
        questionList?.get(position)?.optionB + "\n"
        questionList?.get(position)?.optionC + "\n"
        questionList?.get(position)?.optionD
        var shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Christian Quiz Challenge")
        shareIntent.putExtra(Intent.EXTRA_TEXT, body)
        shareIntent = Intent.createChooser(shareIntent, "Share via")
        startActivity(shareIntent)
    }

    private fun playAnnim(view: View, value: Int, data: String) {
        view.animate()
            .alpha(value.toFloat())
            .scaleX(value.toFloat())
            .scaleY(value.toFloat())
            .setDuration(500)
            .setStartDelay(100)
            .setInterpolator(DecelerateInterpolator())
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationStart(animation: Animator?) {
                    if (value == 0 && count < 4) {
                        var option: String? = null
                        if (count == 0) {
                            option = questionList?.get(position)?.optionA.toString()
                        } else if (count == 1) {
                            option = questionList?.get(position)?.optionB.toString()

                        } else if (count == 2) {
                            option = questionList?.get(position)?.optionC.toString()

                        } else if (count == 3) {
                            option = questionList?.get(position)?.optionD.toString()

                        }
                        if (option != null) {
                            playAnnim(optionsContainer.getChildAt(count), 0, option)
                        }
                        count++
                    }
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (value == 0) {
                        try {
                            (view as TextView).text = data
                            indicator.text = "${position + 1}" + "/" + questionList!!.count()

                            if (modelMatched()) {
                                bookmarks.setImageDrawable(activity?.getDrawable(R.drawable.bookmarked))
                            } else {
                                bookmarks.setImageDrawable(activity?.getDrawable(R.drawable.ic_bookmark_border))
                            }

                        } catch (ex: ClassCastException) {
                            (view as AppCompatButton).text = data
                        }
                        view.tag = data
                        playAnnim(view, 1, data)
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

            })
    }

    private fun checkAnswer(selected: AppCompatButton) {
        enableOption(false)
        next.isEnabled = true
        next.alpha = 1F
        if (selected.text.toString() == questionList?.get(position)?.correctAns) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                selected.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            }
            //correct
            score++
        } else {
            //incorrec
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                selected.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#ff0000"))
                val correctAnswer =
                    optionsContainer.findViewWithTag<AppCompatButton>(questionList?.get(position)?.correctAns)
                correctAnswer.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#4CAF50"))

            }

        }
    }

    private fun enableOption(enable: Boolean) {
        for (i in 0..3) {
            optionsContainer.getChildAt(i).isEnabled = enable
            if (enable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    optionsContainer.getChildAt(i).backgroundTintList =
                        ColorStateList.valueOf(
                            Color.parseColor("#989898")
                        )
                }
            }
        }
    }

    private fun getQuestions() {
        reference
            .child("SETS")
            .child(category!!)
            .child("questions")
//            .orderByChild("setNo").equalTo(setNo.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    println("++++++Data is here " + dataSnapshot.value.toString())
                    for (question in dataSnapshot.children) {
                        val model = question.getValue(Question::class.java)
                        questionList!!.add(model!!)
                        println("CATEGORY +++++++++ " + model)
                    }
                    if (questionList!!.size > 0) {
                        for (i in 0..3) {
                            optionsContainer.getChildAt(i).setOnClickListener { view ->
                                checkAnswer(view as AppCompatButton)
                            }
                        }
                        playAnnim(question, 0, questionList!![position].question!!)
                        next.setOnClickListener {
                            next.isEnabled = false
                            next.alpha = 0.7F
                            position++
                            enableOption(true)
                            if (position == questionList!!.size) {
                                println("Bundle is " + score)
                                println("Bundle is " + questionList!!.count())
//                                var bundle = Bundle()
//                                bundle.putInt("score", score)
//                                bundle.putInt("total", questionList!!.size)
//                                findNavController().navigate(R.id.scoreFragment, bundle)

                                finishDialog(score, questionList!!.count())

                                return@setOnClickListener
                            }
                            count = 0
                            questionList?.get(position)?.question?.let { it1 ->
                                playAnnim(
                                    question,
                                    0,
                                    it1
                                )
                            }
                        }

                    } else {
//                        activity!!.finish()
                        Toast.makeText(activity, "no questions ", Toast.LENGTH_SHORT).show()
                    }

                    println("Data is here " + questionList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activity, error.message, Toast.LENGTH_SHORT).show()


                }


            })
    }

    override fun onPause() {
        super.onPause()
        storeBookmarks()
    }

    private fun getBookmarks() {
        var json = preference!!.getString(KEY_NAME, "")
        val type = object : TypeToken<List<Question>>() {}.type
        bookmarkList = gson!!.fromJson(json, type)

        if (bookmarkList == null) {
            bookmarkList = ArrayList()
        }
    }

    private fun modelMatched(): Boolean {
        var matched = false
        var i = 0
        for (model in bookmarkList!!) {
            if (model.question.equals(questionList?.get(position)?.question)
                && model.correctAns.equals(questionList?.get(position)?.correctAns)
                && model.setNo == questionList?.get(position)?.setNo
            ) {
                matched = true
                matchedQuestionPosition = i
            }
            i++

        }
        return matched

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

    private fun finishDialog(score: Int, questionList: Int) {
        dialog = Dialog(activity!!)
        dialog.setContentView(R.layout.finish_dialog)

        dialog.findViewById<TextView>(R.id.score).text = "$score on this quiz!"
        dialog.findViewById<TextView>(R.id.total).text = "You answered out of $questionList correct"



        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

}