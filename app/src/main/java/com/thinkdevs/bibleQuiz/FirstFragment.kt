package com.thinkdevs.bibleQuiz

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.thinkdevs.bibleQuiz.utility.LoadingDialog
import com.thinkdevs.bibleQuiz.utility.donateUrl
import com.thinkdevs.bibleQuiz.utility.shareWithFriend
import java.util.*


class FirstFragment : Fragment() {

    private lateinit var mAdView: AdView
    private var dialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = LoadingDialog(activity!!)
        loadAds(view)

        view.findViewById<CardView>(R.id.start_btn).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        view.findViewById<CardView>(R.id.bookmarks_btn).setOnClickListener {
            findNavController().navigate(R.id.bookmarkFragment)
        }
        view.findViewById<CardView>(R.id.share).setOnClickListener {
            shareWithFriend(activity!!.applicationContext)
        }

        view.findViewById<CardView>(R.id.about_us).setOnClickListener {
            showCustomDialog()

        }

        view.findViewById<TextView>(R.id.hello).text = getGreetingMessage()
    }

    private fun loadAds(view: View) {
        mAdView = view.findViewById(R.id.adView)
        val request = AdRequest
            .Builder()
            .build()
        mAdView.loadAd(request)
    }

    private fun getGreetingMessage(): String {
        val c = Calendar.getInstance()
        return when (c.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..20 -> "Good Evening"
            in 21..23 -> "Good Night"
            else -> "Hello"
        }
    }

    private fun showCustomDialog() {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.about_us)
        dialog.setCancelable(true)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.findViewById<ImageView>(R.id.bt_close).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.btn_donate).setOnClickListener {
            dialog.dismiss()
            openDonate()
        }

        dialog.show()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes = lp

    }

    private fun openDonate() {
        val url = donateUrl
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}