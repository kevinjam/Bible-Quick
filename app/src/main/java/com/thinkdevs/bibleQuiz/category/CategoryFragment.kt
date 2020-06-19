package com.thinkdevs.bibleQuiz.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
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
import com.thinkdevs.bibleQuiz.R
import com.thinkdevs.bibleQuiz.adapter.CategoryAdapter
import com.thinkdevs.bibleQuiz.model.Category
import com.thinkdevs.bibleQuiz.utility.LoadingDialog
import com.thinkdevs.bibleQuiz.utility.categories
import com.thinkdevs.bibleQuiz.utility.loadAds

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CategoryFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var database = FirebaseDatabase.getInstance()
    private var reference = database.reference
    private var categoryList: ArrayList<Category>? = null
    private var adapter: CategoryAdapter? = null
    private lateinit var mAdView: AdView
    private lateinit var toolbar: Toolbar
    private var dialog: LoadingDialog? = null
    private var isEmpty: RelativeLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdView = view.findViewById(R.id.adView)

        loadAds(view, mAdView)
        dialog = LoadingDialog(activity!!)

        dialog!!.startLoadingDialog()

        isEmpty = view.findViewById(R.id.lyt_nothing_found)
        recyclerView = view.findViewById(R.id.recycler_view)

        getToolbar(view)
        val layoutManage = LinearLayoutManager(activity)
        layoutManage.orientation = RecyclerView.VERTICAL
        recyclerView?.layoutManager = layoutManage
        getCategories(view)
        categoryList = ArrayList()
        adapter = CategoryAdapter(categoryList!!, this)
        recyclerView?.adapter = adapter


    }

    private fun getToolbar(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.title = categories
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        view.findViewById<Toolbar>(R.id.toolbar).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    private fun getCategories(view: View) {
        reference
            .child(categories)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (category in dataSnapshot.children) {
                        val model = category.getValue(Category::class.java)
                        categoryList!!.add(model!!)
                    }
                    if (categoryList!!.count() != 0) {
                        recyclerView!!.visibility = View.VISIBLE
                        isEmpty?.visibility = View.GONE

                    } else {
                        recyclerView!!.visibility = View.GONE
                        isEmpty?.visibility = View.VISIBLE
                        view.findViewById<TextView>(R.id.title_empty).text ="No Category Found"

                    }
                    adapter!!.notifyDataSetChanged()
                    dialog!!.dismissDialog()

                }

                override fun onCancelled(error: DatabaseError) {
                    dialog!!.dismissDialog()
                }


            })

        reference.keepSynced(true)

    }
}