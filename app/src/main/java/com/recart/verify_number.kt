package com.recart

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.forgot_password.*
import kotlinx.android.synthetic.main.verify_number.*
import java.util.HashMap
import java.util.concurrent.TimeUnit

class verify_number : AppCompatActivity() {
    public var no=""
    private var mAuth: FirebaseAuth?=null
    lateinit var  mVerificationId:String
    private var code=""
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var PRIVATE_MODE=0
    private val PREF_NAME="PrefExpiry"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verify_number)
        mAuth= FirebaseAuth.getInstance()
        val sPref: SharedPreferences =getSharedPreferences(PREF_NAME,PRIVATE_MODE)
        mVerificationId=""
        var usernamea=sPref.getString("UserID","")
        var passworde=sPref.getString("password","")
        var first_namee=sPref.getString("fName","")
        var last_namee=sPref.getString("lName","")
        var dobe=sPref.getString("dob","")
        var mydb: FirebaseFirestore
        mydb=FirebaseFirestore.getInstance()
       phonee.setText("$usernamea")
        var  mobile = findViewById<View>(R.id.phonee) as EditText
        var otp =findViewById<View>(R.id.otpe) as EditText
        var  button = findViewById<View>(R.id.sendotp) as Button
        var    login = findViewById<View>(R.id.verify_number) as Button

        back_ver.setOnClickListener {
            Log.d("#999", "forget")
            val j = Intent(this, registration::class.java)
            Log.d("#999", "forget2")
            startActivity(j)
            finish()
            Log.d("#999", "forget3")
        }
        ver.setText("VERIFY NUMBER")
        login.setOnClickListener {
            Log.d("#999", "login")
            var   no = mobile.text.toString()
            var cd = ConnectionDetector(this)
            var isInternet = false
            isInternet = cd.isConnectingToInternet
            if (isInternet) {
            if (otp.text.toString().trim().length == 0)
                otp.setError("Please Enter OTP")
            else if (otp.text.toString().trim().length <6)
                otp.setError("Please Enter 6 Digit OTP")
            else if (mVerificationId.toString() =="")
                otp.setError("Please Click On SEND OTP Button")
            else {
                Log.d("#999", "login1 ")
                val credential = PhoneAuthProvider.getCredential(mVerificationId.toString(), otp.text.toString())
                Log.d("#999", "login2 ")
                signInWithPhoneAuthCredential(credential)
            }}
        else{
                Toast.makeText(this, "INTERNET Not present", Toast.LENGTH_LONG).show()

            }
        }
        button.setOnClickListener {
            Log.d("#999", "IN")
            no = mobile.text.toString()
            var cd = ConnectionDetector(this)
            var isInternet = false
            isInternet = cd.isConnectingToInternet
            if (isInternet) {
                Toast.makeText(this, "Phone Number Checking Please Wait", Toast.LENGTH_SHORT).show()
                Log.d("#999", "get")
                sendVerificationCode(no)
                Log.d("#999", "send otp")
            }
            else{
                Toast.makeText(this, "INTERNET Not present", Toast.LENGTH_LONG).show()

            }
        }
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(Credential: PhoneAuthCredential) {
                Log.d("#999", "mCallbacks")
                //Getting the code sent by SMS
                Toast.makeText(applicationContext, "OTP Sending Please Wait", Toast.LENGTH_SHORT).show()
                var code= Credential.getSmsCode()
                Log.d("#999", "mCallbacks2")
                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    Log.d("#999", "mCallbacks3")
                    otp.setText("$code")
                    //verifying the code
                    Log.d("#999", "mCallbacks3")
                }
                else{
                    otp.setError("Enter OTP")
                } }
            override fun onVerificationFailed(e: FirebaseException) {
                if (e is FirebaseAuthInvalidCredentialsException)
                    mobile.setError("Invalid Phone Number")
                else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    mobile.setError("Quota Exceeded Try Tomorrow")
                } }
            override  fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken ) {
                Log.d("#999", "oncodesent $s")
                //storing the verification id that is sent to the user
                mVerificationId = s
                var  mResendToken = forceResendingToken
            }
            override  fun onCodeAutoRetrievalTimeOut(s: String) {
                // called when the timeout duration has passed without triggering onVerificationCompleted
                super.onCodeAutoRetrievalTimeOut(s)
            } } }
    private fun sendVerificationCode(no: String) {
        Log.d("#999", "put1 $no")
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91" + no,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallbacks)
        Log.d("#999", "put2")
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        var mydb: FirebaseFirestore
        Toast.makeText(this, "OTP Verifing Please  Wait", Toast.LENGTH_SHORT).show()
        mydb=FirebaseFirestore.getInstance()
        Log.d("#999", "signin")
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful()) {
                    val sPref: SharedPreferences =getSharedPreferences(PREF_NAME,PRIVATE_MODE)
                    Log.d("#999", "signin")
                    var usernamea=sPref.getString("UserID","")
                    var passworde=sPref.getString("password","")
                    var first_namee=sPref.getString("fName","")
                    var last_namee=sPref.getString("lName","")
                    var dobe=sPref.getString("dob","")
                    var logine=sPref.getString("login","")
                    var branch=sPref.getString("branch","")
                    //verification successful we will start the profile activity
                    val user = HashMap<String, Any>()
                    user["UserID"] = usernamea.toString()
                    Log.d("#999","username ${usernamea.toString()} ")
                    // Log.d(TAG"#033, RNumber$strNumber")
                    user["Password"] = passworde.toString()
                    // Log.d(TAG, "#033 RId")
                    user["fName"] = first_namee.toString()
                    user["lName"] = last_namee.toString()
                    user["dob"] = dobe.toString()
                    user["image"] = ""
                    user["branch"] =branch.toString()

                    mydb.collection("Members").document(usernamea.toString()).set(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "data inserted successfully", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener { e -> Log.w("#999", "Error adding document", e) }
                    Toast.makeText(this,"SUCCESSFULLY REGISTERED\nWelcome To Login Page",Toast.LENGTH_SHORT).show()
                    sPref.edit().putString("login","registration").apply()
                    val intentq = Intent(this, home::class.java)
                    startActivity(intentq)
                    finish()
                } else {
                    Toast.makeText(this, "Somthing Went Wrong", Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    } } } }
    override fun onBackPressed() {
        startActivity(Intent(this,registration::class.java))
        super.onBackPressed()
        finish()
    }
}
