package com.recart

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.Intent
import android.content.SharedPreferences
import com.recart.Model.ItemInfo

//import com.hacktech19.RecyclerAdapters.RecyclerAdapterItemInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import om.recart.RecyclerAdapters.RecyclerAdapterItemInfo
import com.recart.R
import kotlinx.android.synthetic.main.activity_addnewitem_cart.*
import om.recart.RecyclerAdapters.RecyclerAdapterItemInfoo
//import com.recart.activity_edititem
//import com.recart.Model.ItemInfo
//import om.hacktech19.RecyclerAdapters.RecyclerAdapterItemInfoo
//import om.recart.RecyclerAdapters.RecyclerAdapterItemInfo

import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
//import android.R
import androidx.fragment.app.FragmentActivity
import java.text.SimpleDateFormat
import java.util.*


class cart : AppCompatActivity() {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"
    internal lateinit var mydB: FirebaseFirestore
    internal lateinit var recyclerView: RecyclerView
    internal lateinit var manager: RecyclerView.LayoutManager
    internal lateinit var datalist: ArrayList<ItemInfo>
public var total=0
public var order_id=""
 var increase:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnewitem_cart)
       // btn = findViewById<View>(R.id.floating_btn) as Button
        recyclerView = findViewById<View>(R.id.item_recycler_view) as RecyclerView
        datalist = ArrayList()
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
//        total_price.setText(increase)

        mydB = FirebaseFirestore.getInstance()
        Log.d("#999", "Out")
        // Read from the database


        back_cart.setOnClickListener {
            Log.d("#999", "forget")
            val j = Intent(this, home::class.java)
            Log.d("#999", "forget2")
            startActivity(j)
            finish()
            Log.d("#999", "forget3")
        }

        cart_type.setText("CART")







    /*   var firebaseDatabase= FirebaseDatabase.getInstance()

        val ref =firebaseDatabase.getReference()
        ref.child("cart").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
              val childern=dataSnapshot.children


                childern.forEach{
                    var x=it.toString()
                    Log.d("#9990", "$x")
                }
            }

        })*/



        var cd = ConnectionDetector(this)
        var isInternet = false
        isInternet = cd.isConnectingToInternet
        if (!isInternet) {
            Toast.makeText(this, "INTERNET Not present", Toast.LENGTH_LONG).show()
        }










        mydB.collection("cart").whereEqualTo("UserID", sPref.getString("UserID", "")).get()
                            .addOnCompleteListener { task ->
                                Log.d("#999", "IN")
                                if (task.isSuccessful) {
                                    Log.d("#999", "IN2")
                                    for (doc in task.result!!) {
                                        Log.d("Query", "Data Fetch")
                                        val ulist = ItemInfo()
                                        ulist.item_name = doc.data["item_name"]!!.toString()
                                        ulist.userid = doc.data["UserID"]!!.toString()
                                        ulist.shop_name = doc.data["shop_name"]!!.toString()
                                        ulist.status = doc.data["status"]!!.toString()
                                        var status = doc.data["status"]!!.toString()
                                        ulist.prise = doc.data["prise"]!!.toString()
                                        ulist.type = doc.data["type"]!!.toString()
                                        ulist.food_image = doc.data["image"]!!.toString()
                                        ulist.increase= doc.data["increase"]!!.toString().toInt()
                                        //        increase+= doc.data["increase"]!!.toString().toInt()
                                        Log.d("#999",status+"$increase" )
                                        var abs=doc.data["item_name"]!!.toString()+doc.data["shop_name"]!!.toString()
                                        var strPayID =
                                            doc.data["UserID"]!!.toString()+"_"+doc.data["item_name"]!!.toString()+"_"+doc.data["shop_name"]!!.toString()

                                        mydB.collection("allitem").document(abs.toString()).get().addOnCompleteListener {
                                                task ->
                                            Log.d("#999", "IN")
                                            if (task.isSuccessful) {
                                                val doc=task.result
                                                Log.d("#999", "in reply")
                                                status= doc!!.get("status").toString()

                                            }
                                        }.addOnCompleteListener {

                                            mydB.collection("cart").document(strPayID.toString()).update(
                                                "status",
                                                status
                                            ).addOnCompleteListener {
                                                if (status=="1") {
                                                    datalist.add(ulist)
                                                }
                                            }

                                        }






                                    }
                        Log.d("#99", "Data set in class")
                        recyclerView.setHasFixedSize(true)
                       manager = LinearLayoutManager(this@cart)
                       recyclerView.layoutManager = manager
                        Log.d("Query", "Data Fetch2")
                        val recyclerAdapterItemInfo = RecyclerAdapterItemInfoo(datalist, this@cart)
                        Log.d("Query", "Data Fetch3")
                        recyclerView.adapter = recyclerAdapterItemInfo
                        Log.d("Query", "Data Fetch4")

                    }
                }




        object : CountDownTimer(86400000,1) {
            override fun onFinish() {

                    intent = Intent(applicationContext, cart::class.java)

                    startActivity(intent)
                    finish()


            }

            override fun onTick(p0: Long) {
                increase=0

                //To change body of created functions use File | Settings | File Templates.
                mydB.collection("cart").whereEqualTo("UserID", sPref.getString("UserID", "")).get()
                    .addOnCompleteListener { task ->
                        Log.d("#999", "IN")
                        if (task.isSuccessful) {
                            Log.d("#999", "IN2")
                            increase=0
                            for (doc in task.result!!) {
                                Log.d("Query", "Data Fetch")
                                val ulist = ItemInfo()
                                ulist.increase= doc.data["increase"]!!.toString().toInt()
                               var status= doc.data["status"]!!.toString()
                                if (status=="1") {
                                    increase += doc.data["increase"]!!.toString().toInt()
                                    Log.d("#999", "$increase")
                                }
                            }
                            total_price.setText("$increase")

                        }
                    }.addOnCompleteListener {
                        total_buy.setOnClickListener {
                            var cd = ConnectionDetector(this@cart)
                            var isInternet = false
                            isInternet = cd.isConnectingToInternet
                            if (isInternet) {

                                val date = Date()
                                val formatterDate = SimpleDateFormat("dd-MM")
                                var strDate = formatterDate.format(date)
                                val formatterDateTime = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                                var strDateTime = formatterDateTime.format(date)
                                val formatDateTime = SimpleDateFormat("HH:mm:ss")
                                var dateTime = formatDateTime.format(date)
                                var strPayID = strDate + dateTime
                                strPayID = strPayID.replace("[^a-zA-Z0-9]".toRegex(), "").toString()
                                var order_id = strPayID + sPref.getString("UserID", "")
                                Log.d("#999", "after buy all$increase")
                                Handler().postDelayed({
                                    val intent = Intent(this@cart, payment_activity::class.java)
                                    intent.putExtra("amount", total_price.text.toString())
                                    intent.putExtra("id", order_id)
                                    intent.putExtra("flag", "2")
                                    this@cart.startActivity(intent)
                                },1000)
                            }
                            else{

                                Toast.makeText(this@cart, "INTERNET Not present", Toast.LENGTH_LONG).show()


                            }
                        }
                  }
                                   }


      }.start()
        Log.d("#999", "value=$increase")


    }

    override fun onBackPressed() {
        startActivity(Intent(this,home::class.java))
        super.onBackPressed()
        finish()
    }



    }

