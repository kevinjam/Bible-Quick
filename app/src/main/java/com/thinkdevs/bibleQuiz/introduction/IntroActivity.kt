package com.thinkdevs.bibleQuiz.introduction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.thinkdevs.bibleQuiz.R
import com.thinkdevs.bibleQuiz.adapter.IntroViewPagerAdapter
import com.thinkdevs.bibleQuiz.databinding.ActivityIntroBinding
import com.thinkdevs.bibleQuiz.main.MainActivity
import com.thinkdevs.bibleQuiz.model.ScreenItem


class IntroActivity : AppCompatActivity() {
    private var binding: ActivityIntroBinding? = null
    var position = 0
    var btnAnim: Animation? = null
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (restorePrefData()) {
            val mainActivity = Intent(applicationContext, MainActivity::class.java)
            startActivity(mainActivity)
            finish()
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)


        val mList: MutableList<ScreenItem> = ArrayList()
        mList.add(
            ScreenItem(
                "Be Smart",
                "Knowing a great deal is not the same as being smart;" +
                        " \n intelligence is not information alone but also judgment, the manner in which information is collected and used",
                R.drawable.ic_smart
            )
        )

        mList.add(
            ScreenItem(
                "Failure Is Not an Option",
                "I hated every minute of training, but I said, ‘Don’t quit. Suffer now and live the rest of your life as a champion",
                R.drawable.ic_winner
            )
        )

        mList.add(
            ScreenItem(
                "Knowledge is Power",
                "My people are destroyed for lack of knowledge: because thou hast rejected knowledge, I will also reject thee",
                R.drawable.ic_sunshine
            )
        )

        val introViewPagerAdapter = IntroViewPagerAdapter(this, mList)
        binding!!.screenViewpager.adapter = introViewPagerAdapter

        binding!!.tabIndicator.setupWithViewPager(binding?.screenViewpager)
        binding!!.btnNext.setOnClickListener {
            statisticFCM("click_btn_next")
            position = binding?.screenViewpager!!.currentItem
            if (position < mList.count()) {

                position++
                binding!!.screenViewpager.currentItem = position
            }

            if (position == mList.count() - 1) {
                loaddLastScreen()
            }

        }

        binding!!.tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == mList.count() - 1) {
                    loaddLastScreen()
                }
            }

        })

        binding!!.btnGetStarted.setOnClickListener {
            statisticFCM("click_btn_start")


            val mainActivity = Intent(applicationContext, MainActivity::class.java)
            startActivity(mainActivity)
            savePrefsData()
            finish()
        }

        binding!!.tvSkip.setOnClickListener {
            statisticFCM("click_btn_skip")
            binding?.screenViewpager!!.currentItem = mList.count()
        }
    }

    private fun statisticFCM(ItemNme: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, ItemNme)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    private fun restorePrefData(): Boolean {
        val pref = applicationContext.getSharedPreferences(
            "myPrefs",
            Context.MODE_PRIVATE
        )
        return pref.getBoolean("isIntroOpnend", false)
    }

    private fun savePrefsData() {
        val pref = applicationContext.getSharedPreferences(
            "myPrefs",
            Context.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("isIntroOpnend", true)
        editor.commit()
    }

    private fun loaddLastScreen() {
        binding!!.btnNext.visibility = View.INVISIBLE
        binding!!.btnGetStarted.visibility = View.VISIBLE
        binding!!.tvSkip.visibility = View.INVISIBLE
        binding!!.tabIndicator.visibility = View.INVISIBLE
        // TODO : ADD an animation the getstarted button
        // setup animation
        binding!!.btnGetStarted.animation = btnAnim
    }
}