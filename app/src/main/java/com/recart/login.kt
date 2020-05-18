package com.recart

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.BigTextStyle
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.login.*

class login : AppCompatActivity() {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        var mydb: FirebaseFirestore
        mydb = FirebaseFirestore.getInstance()
        var a = "asd"
        var b = ""
        var c = ""
        var d = ""
        var h = "123"
        var f=""
        var g=""
        var un = findViewById<EditText>(R.id.username)
        var pass = findViewById<EditText>(R.id.password)
        login.setOnClickListener {
            mydb = FirebaseFirestore.getInstance()
            Log.d("#00", "firebase ")
            var cd = ConnectionDetector(this)
            var isInternet = false
            isInternet = cd.isConnectingToInternet
            if (isInternet) {
                if (username.text.toString().trim().equals(""))
                    un.setError("Please Enter Phone Number")
                else if(username.text.toString().length<10)
                    username.setError("Phone Number Must Be 10 Digit")
                else if(username.text.toString().length>10)
                    username.setError("Phone Number Must Be 10 Digit")
                else if (password.text.toString().trim().length == 0)
                    pass.setError("Please Enter Password")
                else if (password.text.toString().length <6)
                    pass.setError("Password Should Be 6 Digit")
                else {
                    Toast.makeText(this, "Data Checking Please Wait", Toast.LENGTH_SHORT).show()
                    Log.d("#0000", "login6 ")
                    val docRef = mydb.collection("Members").document(username.text.toString())
                    Log.d("#0000", "login7 ")
                    docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                        Log.d("#0000", "login8 ")
                        if (task.isSuccessful) {
                            Log.d("#0000", "login9 ")
                            val document = task.result
                            a = document!!.get("Password").toString()
                            h = document!!.get("UserID").toString()
                            b = document!!.get("fName").toString()
                            c = document!!.get("lName").toString()
                            d = document!!.get("dob").toString()
                            f = document!!.get("image").toString()
                            g = document!!.get("branch").toString()
                            Log.d("#0000", "login10 ")
                            if (h != username.text.toString())
                                un.setError("Invalid Phone Number")
                            else if (password.text.toString() != a.toString())
                                pass.setError("Password Does Not Match")
                            else {
                                Toast.makeText(this, "Data Inserting Please Wait", Toast.LENGTH_SHORT).show()
                                mydb = FirebaseFirestore.getInstance()
                                Log.d("#00", "firebase ")
                                val docRef = mydb.collection("Members").document(username.text.toString())
                                docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                                    if (task.isSuccessful) {
                                        val document = task.result
                                        a = document!!.get("Password").toString()
                                    } })


                                    startService(Intent(this,service::class.java))







                                Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show()
                                sPref.edit().putString("UserID",username.text.toString()).apply()
                                sPref.edit().putString("password", a.toString()).apply()
                                sPref.edit().putString("fName", b.toString()).apply()
                                sPref.edit().putString("lName", c.toString()).apply()
                                sPref.edit().putString("dob", d.toString()).apply()
                                sPref.edit().putString("login","registration").apply()
                                sPref.edit().putString("type","").apply()
                                sPref.edit().putString("image", f.toString()).apply()
                                sPref.edit().putString("branch", g.toString()).apply()
                                val l = Intent(this, home::class.java).putExtra("isappstart",true)
                                startActivity(l)
                                finish()
                            } } }) } }
            else {
                if (!isInternet)
                    Toast.makeText(this, "INTERNET Not Present", Toast.LENGTH_SHORT).show()
            } }
        signup.setOnClickListener {
            val i = Intent(this, registration::class.java)
            startActivity(i)
            finish()
        }
        forget.setOnClickListener {
            Log.d("#999", "forget")
            val j = Intent(this, forget_password::class.java)
            Log.d("#999", "forget2")
            startActivity(j)
            finish()
            Log.d("#999", "forget3")
        } }


}
