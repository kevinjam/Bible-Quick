package com.thinkdevs.bibleQuiz.utility

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import com.thinkdevs.bibleQuiz.R


class LoadingDialog constructor(private val activityb: Activity) {
    private var alertDialog: AlertDialog? = null
    fun startLoadingDialog() {
      if (activityb != null){
          val builder = AlertDialog.Builder(activityb)
          val inflater = activityb.layoutInflater
          builder.setView(inflater.inflate(R.layout.loading, null))
          builder.setCancelable(true)
          alertDialog = builder.create()
          alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
          alertDialog!!.show()
      }
    }

    fun dismissDialog() {
        alertDialog!!.dismiss()
    }



    fun startAboutDialog() {
        if (activityb != null) {
            val builder = AlertDialog.Builder(activityb)
            val inflater = activityb.layoutInflater
            builder.setView(inflater.inflate(R.layout.about_us, null))
            builder.setCancelable(true)
            val lp = WindowManager.LayoutParams()

            alertDialog = builder.create()
            lp.copyFrom(alertDialog!!.window!!.attributes)
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog!!.window!!.attributes = lp

            alertDialog!!.show()

            //https://ko-fi.com/R5R1BX7C
        }
    }

}