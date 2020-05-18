package com.recart

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_contact.*
import java.text.SimpleDateFormat
import java.util.*

class contact : AppCompatActivity() {


    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"
    var min=0.0
   // var max=0.0
    var current=1.9


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        var mydb: FirebaseFirestore
        mydb = FirebaseFirestore.getInstance()
        var cd = ConnectionDetector(this)
        var isInternet = false
        isInternet = cd.isConnectingToInternet
        var username=sPref.getString("UserID","")
        back_contact.setOnClickListener {
            Log.d("#999", "forget")
            val j = Intent(this, home::class.java)
            Log.d("#999", "forget2")
            startActivity(j)
            finish()
            Log.d("#999", "forget3")
        }
        type.setText("HELP ?")

        val date = Date()
        val formatterDate = SimpleDateFormat("dd-MM-yyyy")
        var strDate = formatterDate.format(date)
        val formatterDateTime = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        var strDateTime = formatterDateTime.format(date)
        val formatDateTime = SimpleDateFormat("HH:mm:ss")
        var dateTime = formatDateTime.format(date)
        var strPayID =  strDate + dateTime
        strPayID = strPayID.replace("[^a-zA-Z0-9]".toRegex(), "").toString()
var set=1

        mydb.collection("contact").document(username.toString()).get().addOnCompleteListener {
            task ->
            Log.d("#999", "IN")
            if (task.isSuccessful) {
                val doc=task.result
                Log.d("#999", "in reply")
                var  reply= doc!!.get("reply").toString()
                textView.text="$reply".toString()

            }
        }.addOnSuccessListener {

            if (textView.text.toString()=="null"){
                 textView.text="HiCypher..happy to help!!!"
                set=1
            }
            else{
                set=2

            }

        }





        submit.setOnClickListener() {
            var cd = ConnectionDetector(this)
            var isInternet = false
            isInternet = cd.isConnectingToInternet
            if (isInternet) {

                Log.d("#999", "in reply1")
                Toast.makeText(this, "Sending.....", Toast.LENGTH_SHORT).show()

                Log.d("#999", "in reply2")
                if (set == 1) {
                    val map1 = HashMap<String, Any>()
                    map1["$strPayID"] = fullscreen_content.text.toString()
                    map1["UserID"] = username.toString()
                    map1["reply"] = "HiCypher..happy to help!!!"
                    Log.d("#999", "in reply3")
                    mydb.collection("contact").document(username.toString()).set(map1)
                        .addOnSuccessListener {
                            Log.d("#999", "in reply4")
                            Toast.makeText(this, "sent successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, home::class.java))

                        }.addOnFailureListener {
                            Toast.makeText(this, "not sent $username", Toast.LENGTH_SHORT).show()
                        }

                } else {

                    val map1 = HashMap<String, Any>()
                    map1["$strPayID"] = fullscreen_content.text.toString()
                    map1["UserID"] = username.toString()
                    Log.d("#999", "in reply3")
                    mydb.collection("contact").document(username.toString()).update(map1)
                        .addOnSuccessListener {
                            Log.d("#999", "in reply4")
                            Toast.makeText(this, "sent successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, home::class.java))
                            finish()

                        }.addOnFailureListener {
                            Toast.makeText(this, "not sent $username", Toast.LENGTH_SHORT).show()
                        }


                }
            }
            else{
                Toast.makeText(this, "INTERNET Not present", Toast.LENGTH_LONG).show()

            }

        }




    }
    override fun onBackPressed() {
        startActivity(Intent(this,home::class.java))
        super.onBackPressed()
        finish()
    }
}



