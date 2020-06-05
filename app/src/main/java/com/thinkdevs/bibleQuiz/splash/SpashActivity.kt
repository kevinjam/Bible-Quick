package com.thinkdevs.bibleQuiz.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.thinkdevs.bibleQuiz.main.MainActivity
import com.thinkdevs.bibleQuiz.R
import com.thinkdevs.bibleQuiz.databinding.ActivitySpashBinding
import kotlinx.android.synthetic.main.activity_spash.*

class SpashActivity : AppCompatActivity() {

    var binding:ActivitySpashBinding?= null
     var forImg:Animation?= null
    var fromBottom:Animation?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding = DataBindingUtil.setContentView(this,R.layout.activity_spash)

        forImg = AnimationUtils.loadAnimation(this, R.anim.forimg)
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.frombottom)


        main_img.startAnimation(forImg)
        welcome_text.startAnimation(fromBottom)
        descriptionText.startAnimation(fromBottom)
        getstarted.startAnimation(fromBottom)

        getstarted.setOnClickListener {
            startActivity(Intent(this@SpashActivity, MainActivity::class.java))
        }


    }
}