package com.recart

import android.app.SearchManager
import android.content.Context
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
//import android.widget.SearchView
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
import om.recart.RecyclerAdapters.RecyclerAdapterItemInfo
import androidx.appcompat.widget.SearchView
import om.recart.RecyclerAdapters.RecyclerAdapterItemInfooffer

import java.util.ArrayList

class activity_addnewitem : AppCompatActivity() {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"

    internal lateinit var btn: Button
    internal lateinit var mydB: FirebaseFirestore
    internal lateinit var recyclerView: RecyclerView
    internal lateinit var manager: RecyclerView.LayoutManager
    internal lateinit var datalist: ArrayList<ItemInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnewitem)
       // recyclerView = findViewById<View>(R.id.item_recycler_view) as RecyclerView
        recyclerView = findViewById<RecyclerView>(R.id.item_recycler_view)
        datalist = ArrayList()
        mydB = FirebaseFirestore.getInstance()
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
       back.setOnClickListener {
           back.animate().translationX(-100f).setDuration(2000).start()
            Log.d("#999", "forget")
            val j = Intent(this, home::class.java)
            Log.d("#999", "forget2")
            startActivity(j)
            finish()
            Log.d("#999", "forget3")
        }
        var typee= sPref.getString("type", "")
     type.setText("$typee")
        var cd = ConnectionDetector(this)
        var isInternet = false
        isInternet = cd.isConnectingToInternet
        if (!isInternet) {
            Toast.makeText(this, "INTERNET Not present", Toast.LENGTH_LONG).show()
        }



        Log.d("#999", "Out")
        if(intent.getIntExtra("offer",0)==1){
            mydB.collection("allitem").whereEqualTo("offer", sPref.getString("offer", "0")).get()
                    .addOnCompleteListener { task ->
                        Log.d("#999", "IN")
                        if (task.isSuccessful) {
                            Log.d("#999", "IN2")
                            for (doc in task.result!!) {
                                Log.d("Query", "Data Fetch")
                                val ulist = ItemInfo()
                                ulist.item_name = doc.data["title"]!!.toString()
                                ulist.shop_name = doc.data["shopid"]!!.toString()
                                ulist.prise = doc.data["price"]!!.toString()
                                ulist.preprise = doc.data["preprice"]!!.toString()
                                ulist.status = doc.data["status"]!!.toString()
                                var status = doc.data["status"]!!.toString()
                                ulist.food_image = doc.data["image"]!!.toString()
                                ulist.type = doc.data["type"]!!.toString()
                                ulist.userid= sPref.getString("UserID","")
                                if (status=="1") {
                                    datalist.add(ulist)
                                }
                            }
                            Log.d("#99", "Data set in class")
                            recyclerView.setHasFixedSize(true)
                            manager = LinearLayoutManager(this@activity_addnewitem)
                            recyclerView.layoutManager = manager

                            val recyclerAdapterItemInfo = RecyclerAdapterItemInfooffer(datalist, this@activity_addnewitem)
                            recyclerView.adapter = recyclerAdapterItemInfo
                        } }
        }
        else{
        mydB.collection("allitem").whereEqualTo("type", sPref.getString("type", "")).get()
            .addOnCompleteListener { task ->
            Log.d("#999", "IN")
            if (task.isSuccessful) {
                Log.d("#999", "IN2")
                for (doc in task.result!!) {
                    Log.d("Query", "Data Fetch")
                    val ulist = ItemInfo()
                    ulist.item_name = doc.data["title"]!!.toString()
                    ulist.shop_name = doc.data["shopid"]!!.toString()
                    ulist.prise = doc.data["price"]!!.toString()
                   ulist.status = doc.data["status"]!!.toString()
                   var status = doc.data["status"]!!.toString()
                    ulist.food_image = doc.data["image"]!!.toString()
                    ulist.type = doc.data["type"]!!.toString()
                    ulist.userid= sPref.getString("UserID","")
                    if (status=="1") {

                        datalist.add(ulist)
                    }


                }
                Log.d("#99", "Data set in class")


                    recyclerView.setHasFixedSize(true)
                    manager = LinearLayoutManager(this@activity_addnewitem)
                    recyclerView.layoutManager = manager

                    val recyclerAdapterItemInfo = RecyclerAdapterItemInfo(datalist, this@activity_addnewitem)
                    recyclerView.adapter = recyclerAdapterItemInfo
            }
        }}
    }




    override fun onBackPressed() {
        startActivity(Intent(this,home::class.java))
        super.onBackPressed()
        finish()
    }

}
