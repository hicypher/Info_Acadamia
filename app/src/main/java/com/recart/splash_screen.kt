package com.recart

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class splash_screen : AppCompatActivity() {


    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"
    var min=0.0
   // var max=0.0
    var current=2.92

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        var mydb: FirebaseFirestore
        mydb = FirebaseFirestore.getInstance()
        var cd = ConnectionDetector(this)
        var isInternet = false
        isInternet = cd.isConnectingToInternet
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        var usernamea=sPref.getString("login","")
        sPref.edit().putString("version","2.92").apply()



        object : CountDownTimer(1000,1000) {
            override fun onFinish() {
                if (isInternet) {
                    mydb.collection("setting").document("version").get().addOnCompleteListener(
                        OnCompleteListener { task ->
                            if (task.isSuccessful) {
                                min = task.result!!.get("minimum").toString().toDouble()
                        //        max = task.result!!.get("maximum").toString().toDouble()
                            }
                        }).addOnCompleteListener {
                    if (current >= min) {
                        if (usernamea == "") {
                            intent = Intent(this@splash_screen, login::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            intent = Intent(this@splash_screen, home::class.java).putExtra("isappstart",true)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        var time="link"
                        var update_splash="http://www.hicypher.blogspot.com"
                        mydb.collection("setting").document(time.toString()).get().addOnCompleteListener {
                                task ->
                            Log.d("#999", "IN")
                            if (task.isSuccessful) {
                                val doc=task.result
                                Log.d("#999", "in reply")
                                update_splash= doc!!.get("update_splash").toString()
                            }
                        }.addOnCompleteListener {

                            Toast.makeText(
                                this@splash_screen,
                                "please update ReCart",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("$update_splash")
                                )
                            )

                        }




                    }
                }
                }

                else {
                    if (!isInternet)

                        Toast.makeText(applicationContext, "Check Your Connection(INTERNET) and RESTART APP", Toast.LENGTH_LONG).show()
                }
            }
            override fun onTick(p0: Long) {
                //To change body of created functions use File | Settings | File Templates.

            }

        }.start()
    }
}