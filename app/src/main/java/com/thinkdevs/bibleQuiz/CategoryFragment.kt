package com.thinkdevs.bibleQuiz

import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.thinkdevs.bibleQuiz.adapter.CategoryAdapter
import com.thinkdevs.bibleQuiz.model.Category
import com.thinkdevs.bibleQuiz.utility.LoadingDialog
import com.thinkdevs.bibleQuiz.utility.loadAds

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CategoryFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var database = FirebaseDatabase.getInstance()
    private var reference = database.reference
    private var categoryList: ArrayList<Category>?= null
    private var adapter:CategoryAdapter?= null
    private lateinit var mAdView:AdView
    private lateinit var toolbar: Toolbar
    private var dialog:LoadingDialog?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdView= view.findViewById(R.id.adView)

        loadAds(view,mAdView)
        dialog = LoadingDialog(activity!!)

        dialog!!.startLoadingDialog()

        recyclerView = view.findViewById(R.id.recycler_view)
        getToolbar(view)
        val layoutManage = LinearLayoutManager(activity)
        layoutManage.orientation = RecyclerView.VERTICAL
        recyclerView?.layoutManager = layoutManage

        getCategories()
        categoryList = ArrayList()
        adapter = CategoryAdapter(categoryList!!, this)
        println("Count Size" + categoryList!!.count())
        recyclerView?.adapter = adapter


    }

    private fun getToolbar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.title = "Categories"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        view.findViewById<Toolbar>(R.id.toolbar).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    private fun getCategories() {
        reference
            .child("categories")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    println("Data is here " + dataSnapshot.value.toString())
                    for (category in dataSnapshot.children) {
                        val model = category.getValue(Category::class.java)
                        categoryList!!.add(model!!)
                    }
                    adapter!!.notifyDataSetChanged()
                    dialog!!.dismissDialog()

                }

                override fun onCancelled(error: DatabaseError) {
                    dialog!!.dismissDialog()
                }


            })
    }
}