package com.recart

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat.format
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.registration.*
import java.text.SimpleDateFormat
import java.util.*

class registration : AppCompatActivity() {
    private var PRIVATE_MODE=0
    private val PREF_NAME="PrefExpiry"
    internal lateinit var spin: Spinner
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        val sPref: SharedPreferences =getSharedPreferences(PREF_NAME,PRIVATE_MODE)
        var mydb: FirebaseFirestore
        mydb = FirebaseFirestore.getInstance()
        var format= SimpleDateFormat("dd/MM/YYYY", Locale.US)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)
        itemspinner()


        back_reg.setOnClickListener {
            Log.d("#999", "forget")
            val j = Intent(this, login::class.java)
            Log.d("#999", "forget2")
            startActivity(j)
            finish()
            Log.d("#999", "forget3")
        }

        reg.setText("REGISTRATION")
        acount.setOnClickListener {
            val i = Intent(this, login::class.java)
            startActivity(i)
            finish()
        }
        var check:Int=0
        val b = findViewById(R.id.calender) as ImageButton
        Log.d("#009","On create  Calender")
        b.setOnClickListener(){
            Log.d("#009","Start Program Calender")
            val now= Calendar.getInstance()
            val datepicker= DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
                val selDate=Calendar.getInstance()
                selDate.set(Calendar.YEAR,year)
                selDate.set(Calendar.MONTH,month)
                selDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                var date=format.format(selDate.time) as String
                var date_String : EditText
                dateTv.setText(date.toString())
                Toast.makeText(this,date, Toast.LENGTH_SHORT)
            },now.get(Calendar.YEAR)
                ,now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
            datepicker.show()
        }
        registration.setOnClickListener {
            var cd= ConnectionDetector(this)
            var isInternet= false
            isInternet= cd.isConnectingToInternet
            if (isInternet) {
                if (!android.util.Patterns.PHONE.matcher(et_username.text.toString()).matches())
                    et_username.setError("Enter valid Phone Number.")
                else if (et_username.text.toString().length < 10)
                    et_username.setError("Phone Number Must Be 10 Digit")
                else if (et_username.text.toString().length > 10)
                    et_username.setError("Phone Number Must Be 10 Digit")
                else if (et_password.text.toString().length < 6)
                    et_password.setError("Password Must Be Atleast 6 Digit Long")
                else if (et_password.text.toString() != et_password_comfirm.text.toString())
                    et_password_comfirm.setError("Passwords Does Not Match")
                else if (et_name_first.text.toString().trim().equals(""))
                    et_name_first.setError("Enter First Name")
                else if (et_name_last.text.toString().trim() == "")
                    et_name_last.setError("Enter Last Name")
                else if (dateTv.text.toString() == "") {
                    dateTv.setError("Select DOB Here")
                    Toast.makeText(this, "Entre Date Of Birth Please", Toast.LENGTH_LONG).show()
                }

                else {
                    Toast.makeText(this, "Data Checking Please Wait", Toast.LENGTH_LONG).show()
                    val docRef = mydb.collection("Members").document(et_username.text.toString())
                    docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            var h = document!!.get("UserID").toString()
                            if (h == et_username.text.toString()) {
                                et_username.setError("Phone Number Already Registered 'GO TO LOGIN'")
                                Toast.makeText(this, "Phone Number Already Registered 'GO TO LOGIN", Toast.LENGTH_LONG).show()
                            }
                         else {
                            val intek = Intent(applicationContext, verify_number::class.java)
                            sPref.edit().putString("UserID", et_username.text.toString()).apply()
                            sPref.edit().putString("password", et_password.text.toString()).apply()
                            sPref.edit().putString("fName", et_name_first.text.toString()).apply()
                            sPref.edit().putString("lName", et_name_last.text.toString()).apply()
                            sPref.edit().putString("dob", dateTv.text.toString()).apply()
                                sPref.edit().putString("login","").apply()
                                sPref.edit().putString("flag","").apply()
                                sPref.edit().putString("branch",spin.selectedItemPosition.toString()).apply()
                                Toast.makeText(this, "Please Verify Number For Registration", Toast.LENGTH_LONG).show()
                            startActivity(intek)
                                finish()
                            } } }) } }
            else {
                Toast.makeText(this, "INTERNET Not present", Toast.LENGTH_LONG).show()
            } } }
    override fun onBackPressed() {
        startActivity(Intent(this,login::class.java))
        super.onBackPressed()
        finish()
    }


    internal fun itemspinner() {
        val items = arrayOf("Branch(Year)","IT(I)","IT(II)","IT(III)","IT(IV)","ME(I)","ME(II)","ME(III)","ME(IV)","CE(I)","CE(II)","CE(III)","CE(IV)")
        spin = findViewById<View>(R.id.branch) as Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin.adapter = adapter

    }




}
