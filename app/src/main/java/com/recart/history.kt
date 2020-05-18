package com.recart

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.Intent
import android.content.SharedPreferences
//import com.hacktech19.RecyclerAdapters.RecyclerAdapterItemInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.recart.Model.ItemInfo
import kotlinx.android.synthetic.main.activity_addnewitem.*
import kotlinx.android.synthetic.main.history.*
import kotlinx.android.synthetic.main.new_order.*
import kotlinx.android.synthetic.main.new_order.type_new
import om.recart.RecyclerAdapters.RecyclerAdapterItemInfo
import om.recart.RecyclerAdapters.RecyclerAdapterItemInfoo_history
import om.recart.RecyclerAdapters.RecyclerAdapterItemInfoo_new_order

import java.util.ArrayList

class history : AppCompatActivity() {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"

    internal lateinit var btn: Button
    internal lateinit var mydB: FirebaseFirestore
    internal lateinit var recyclerView: RecyclerView
    internal lateinit var manager: RecyclerView.LayoutManager
    internal lateinit var datalist: ArrayList<ItemInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history)
        recyclerView = findViewById<View>(R.id.item_recycler_view) as RecyclerView
        datalist = ArrayList()
        mydB = FirebaseFirestore.getInstance()
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        type_new.setText("HISTORY")
        back_history.setOnClickListener {
            Log.d("#999", "forget")
            val j = Intent(this, home::class.java)
            Log.d("#999", "forget2")
            startActivity(j)
            finish()
            Log.d("#999", "forget3")
        }

        var cd = ConnectionDetector(this)
        var isInternet = false
        isInternet = cd.isConnectingToInternet
        if (!isInternet) {
            Toast.makeText(this, "INTERNET Not present", Toast.LENGTH_LONG).show()
        }

        Log.d("#999", "Out")
        mydB.collection("history").whereEqualTo("UserID", sPref.getString("UserID", "")).get()
            .addOnCompleteListener { task ->
            Log.d("#999", "IN")
            if (task.isSuccessful) {
                Log.d("#999", "IN2")
                for (doc in task.result!!) {
                    Log.d("Query", "Data Fetch")
                    val ulist = ItemInfo()
                    ulist.item_name = doc.data["item_name"]!!.toString()
                    ulist.shop_name = doc.data["shop_name"]!!.toString()
                    ulist.prise = doc.data["prise"]!!.toString()
                   ulist.status = doc.data["status"]!!.toString()
                   ulist.quantity = doc.data["quantity"]!!.toString()
                    ulist.food_image= doc.data["image"]!!.toString()
                    ulist.order_date= doc.data["order_date"]!!.toString()
                    datalist.add(ulist)
                }
                Log.d("#99", "Data set in class")


                    recyclerView.setHasFixedSize(true)
                    manager = LinearLayoutManager(this@history)
                    recyclerView.layoutManager = manager

                    val recyclerAdapterItemInfo = RecyclerAdapterItemInfoo_history(datalist, this@history)
                    recyclerView.adapter = recyclerAdapterItemInfo



            }
        }



    }
    override fun onBackPressed() {
        startActivity(Intent(this,home::class.java))
        super.onBackPressed()
        finish()
    }
}
