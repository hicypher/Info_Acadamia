package com.recart

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.edit_profile.*
import kotlinx.android.synthetic.main.registration.*
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import android.provider.SyncStateContract.Helpers.update
import android.view.View
import android.widget.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


class edit_profile : AppCompatActivity() {
    private var PRIVATE_MODE=0
    private val PREF_NAME="PrefExpiry"
    internal lateinit var spin: Spinner
    @RequiresApi(Build.VERSION_CODES.N)
    var isChanged: Boolean? = false
    override fun onCreate(savedInstanceState: Bundle?) {
        val sPref: SharedPreferences =getSharedPreferences(PREF_NAME,PRIVATE_MODE)
        var mydb: FirebaseFirestore
        mydb = FirebaseFirestore.getInstance()
        var format= SimpleDateFormat("dd/MM/YYYY", Locale.US)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)
        var check:Int=0
        var usernamea=sPref.getString("UserID","")
        var passworde=sPref.getString("password","")
        var first_namee=sPref.getString("fName","")
        var last_namee=sPref.getString("lName","")
        var dobe=sPref.getString("dob","")
        var conform_pass=sPref.getString("password","")
        var branch=sPref.getString("branch","")!!.toInt()
        et_usernameprofile.setText(" $usernamea")
        et_passwordprofile.setText("$passworde")
        et_name_firstprofile.setText("$first_namee")
        et_name_lastprofile.setText("$last_namee")
        dateTvprofile.setText("$dobe")
        et_password_comfirmprofile.setText("$passworde")

        itemspinner()
        spin.setSelection(branch)
        back_edit.setOnClickListener {
            Log.d("#999", "forget")
            val j = Intent(this, home::class.java)
            Log.d("#999", "forget2")
            startActivity(j)
            finish()
            Log.d("#999", "forget3")
        }

        edit_pr.setText("EDIT PROFILE")



        var imguri=""
        imguri=sPref.getString("image","").toString()
                if (imguri != null) {
                    Log.d("#999", "DocumentSnapshot data: " + imguri+imguri)
                    if(!imguri!!.isEmpty()) {
                        Picasso.get().load(imguri).into(imagec); Log.d("#999", "img document")}
                    else  Log.d("#999", "No such document")

                } else {
                    Log.d("#999", "No such document")
                }


        if(!imguri!!.isEmpty()) {
            Picasso.get().load(imguri).into(imagec); Log.d("#999", "img document")}
        else  Log.d("#999", "No such document")


        val b = findViewById(R.id.calenderprofile) as ImageButton
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
                dateTvprofile.setText(date.toString())
                Toast.makeText(this,date, Toast.LENGTH_SHORT)
            },now.get(Calendar.YEAR)
                ,now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
            datepicker.show()
        }
        save.setOnClickListener {
            var cd= ConnectionDetector(this)
            var isInternet= false
            isInternet= cd.isConnectingToInternet
            if (isInternet) {
                if (et_passwordprofile.text.toString().length < 6)
                    et_passwordprofile.setError("Password Must Be Atleast 6 Digit Long")
                else if (et_passwordprofile.text.toString() != et_password_comfirmprofile.text.toString())
                    et_password_comfirmprofile.setError("Passwords Does Not Match")
                else if (et_name_firstprofile.text.toString().trim().equals(""))
                    et_name_firstprofile.setError("Enter First Name")
                else if (et_name_lastprofile.text.toString().trim() == "")
                    et_name_lastprofile.setError("Enter Last Name")
                else if (dateTvprofile.text.toString() == "") {
                    dateTvprofile.setError("Select DOB Here")
                    Toast.makeText(this, "Entre Date Of Birth Please", Toast.LENGTH_LONG).show()
                } else {

                        Toast.makeText(this, "Data Updating Please Wait", Toast.LENGTH_LONG).show()

                        mydb.collection("Members").document(usernamea.toString()).update(
                            "UserID",
                            usernamea,
                            "Password",
                            et_passwordprofile.text.toString(),
                            "fName",
                            et_name_firstprofile.text.toString(),
                            "lName",
                            et_name_lastprofile.text.toString(),
                            "dob",
                            dateTvprofile.text.toString(),
                                "branch",
                                spin.selectedItemPosition.toString()
                        )
                            .addOnSuccessListener {
                                Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_LONG).show()
                                val intek = Intent(applicationContext, home::class.java)
                                sPref.edit().putString("UserID", et_usernameprofile.text.toString()).apply()
                                sPref.edit().putString("password", et_passwordprofile.text.toString()).apply()
                                sPref.edit().putString("fName", et_name_firstprofile.text.toString()).apply()
                                sPref.edit().putString("lName", et_name_lastprofile.text.toString()).apply()
                                sPref.edit().putString("dob", dateTvprofile.text.toString()).apply()
                                sPref.edit().putString("branch",spin.selectedItemPosition.toString()).apply()
                                sPref.edit().putString("flag", "update").apply()
                                startActivity(intek)
                                finish()

                            }.addOnFailureListener { e -> Log.w("111", "Error adding document", e)
                                    Toast.makeText(this, "Too Many Changes\nIf You Want To Change Then LOUGOUT And LOGIN Again ", Toast.LENGTH_LONG).show()}


                 /*       et_usernameprofile.setText(" $usernamea")
                        et_passwordprofile.setText("$passworde")
                        et_name_firstprofile.setText("$first_namee")
                        et_name_lastprofile.setText("$last_namee")
                        dateTvprofile.setText("$dobe")
                        et_password_comfirmprofile.setText("$passworde")
                        et_password_comfirmprofile.setText("$passworde")
                        spin.setSelection(branch)

*/


                } }
            else {
                Toast.makeText(this, "Check Your Connection", Toast.LENGTH_SHORT).show()
            } }

        imagec.setOnClickListener {

            val j = Intent(this, change_image::class.java)
            Log.d("#999", "forget2")
            startActivity(j)
          finish()

        }

    }
    override fun onBackPressed() {
        startActivity(Intent(this,home::class.java))
        super.onBackPressed()
        finish()
    }


    internal fun itemspinner() {
        val items = arrayOf("Branch(Year)","IT(I)","IT(II)","IT(III)","IT(IV)","ME(I)","ME(II)","ME(III)","ME(IV)","CE(I)","CE(II)","CE(III)","CE(IV)")
       // spin = findViewById<View>(R.id.branchedit) as Spinner
        spin = findViewById<Spinner>(R.id.branchedit)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin.adapter = adapter

    }

}
