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
import java.util.concurrent.TimeUnit

class forget_password : AppCompatActivity() {
    public var no=""
    private var mAuth: FirebaseAuth?=null
    private var  mVerificationId=""
    private var code=""
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"
    var a = "asd"
    var b = ""
    var c = ""
    var d = ""
    var e = ""
    var f = ""
    var g = ""
    var h = "123"
    var i = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("#999", "forget password2")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password)
        Log.d("#999", "forget password")
        mAuth= FirebaseAuth.getInstance()
        var  mobile = findViewById<View>(R.id.phone) as EditText
        var otp =findViewById<View>(R.id.otp) as EditText
        var  button = findViewById<View>(R.id.send_otp) as Button
        var    login = findViewById<View>(R.id.verify) as Button
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        var mydb: FirebaseFirestore=FirebaseFirestore.getInstance()
        Log.d("#999", "forget password1")

        back_forget.setOnClickListener {
            Log.d("#999", "forget")
            val j = Intent(this, com.recart.login::class.java)
            Log.d("#999", "forget2")
            startActivity(j)
            finish()
            Log.d("#999", "forget3")
        }

        forget_pass.setText("FORGET PASSWORD")



        login.setOnClickListener {
            Log.d("#999", "login")
            var   no = mobile.text.toString()
            if (otp.text.toString().trim().length == 0)
                otp.setError("Please Enter OTP")
            else if (otp.text.toString().trim().length <6)
                otp.setError("Please Enter 6 Digit OTP")
            else if (mVerificationId.toString() =="")
                otp.setError("Please Enter Mobile No. And Click SEND OTP Button")
            else {
                Log.d("#999", "login1 ")
                val credential = PhoneAuthProvider.getCredential(mVerificationId.toString(), otp.text.toString())
                Log.d("#999", "login2 ")
                signInWithPhoneAuthCredential(credential)
            } }
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(Credential: PhoneAuthCredential) {
                Log.d("#999", "mCallbacks")
                //Getting the code sent by SMS
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
                    mobile.setError("Invalid phone number")
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

            } }
        button.setOnClickListener {
            mydb = FirebaseFirestore.getInstance()
            Log.d("#00", "firebase ")
            var cd = ConnectionDetector(this)
            var isInternet = false
            isInternet = cd.isConnectingToInternet
            if (isInternet) {
                if (phone.text.toString().trim().equals(""))
                    mobile.setError("Please Enter Phone Number")
                else if(phone.text.toString().length<10)
                    mobile.setError("Phone Number Must Be 10 Digit")
                else if(phone.text.toString().length>10)
                    mobile.setError("Phone Number Must Be 10 Digit")
                else {
                    Toast.makeText(this, "Phone Number Checking Please Wait", Toast.LENGTH_SHORT).show()
                    val docRef = mydb.collection("Members").document(phone.text.toString())
                    docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            a = document!!.get("Password").toString()
                            h = document!!.get("UserID").toString()
                            b = document!!.get("fName").toString()
                            c = document!!.get("lName").toString()
                            d = document!!.get("dob").toString()
                            f = document!!.get("image").toString()
                            g = document!!.get("publishdate").toString()
                            i = document!!.get("branch").toString()
                            if (h != phone.text.toString())
                                mobile.setError("Phone Number Not Registered 'GO TO SIGNUP'")
                            else {

                                Toast.makeText(this, "OTP Sending Please Wait", Toast.LENGTH_SHORT).show()
                                no = mobile.text.toString()

                                sendVerificationCode(no)
                            } } }) } }
            else {
                if (!isInternet)
                    Toast.makeText(this, "INTERNET Not Present", Toast.LENGTH_SHORT).show()
            } } }
    override fun onBackPressed() {
        startActivity(Intent(this,login::class.java))
        super.onBackPressed()
        finish()
    }
    private fun sendVerificationCode(no: String) {
        Log.d("#999", "put1 $no")
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91" + no,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallbacks
        )
        Log.d("#999", "put2")
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        Log.d("#999", "signin")
        Toast.makeText(this, "OTP Verifing Please  Wait", Toast.LENGTH_SHORT).show()
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        var mydb: FirebaseFirestore=FirebaseFirestore.getInstance()
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful()) {
                    Log.d("#999", "signin")
                    //verification successful we will start the profile activity
                    Toast.makeText(this, " Data Inserting Please Wait", Toast.LENGTH_SHORT).show()
                    mydb = FirebaseFirestore.getInstance()
                    Log.d("#00", "firebase ")
                    val docRef = mydb.collection("Members").document(phone.text.toString())
                    docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            a = document!!.get("Password").toString()
                            h = document!!.get("UserID").toString()
                            b = document!!.get("fName").toString()
                            c = document!!.get("lName").toString()
                            d = document!!.get("dob").toString()
                            f = document!!.get("image").toString()
                            g = document!!.get("publishdate").toString()
                            i = document!!.get("branch").toString()
                        } })
                    Toast.makeText(this, "LOGIN SUCCESSFULL\nGo to 'EDIT PROFILE' and change password", Toast.LENGTH_LONG).show()
                    val intek = Intent(applicationContext, home::class.java)
                    sPref.edit().putString("UserID", phone.text.toString()).apply()
                    sPref.edit().putString("password", a.toString()).apply()
                    sPref.edit().putString("fName", b.toString()).apply()
                    sPref.edit().putString("lName", c.toString()).apply()
                    sPref.edit().putString("dob", d.toString()).apply()
                    sPref.edit().putString("branch", i.toString()).apply()
                    startActivity(intek)
                    finish()
                } else {
                    //verification unsuccessful.. display an error message
                    Toast.makeText(this, "Somthing Went Wrong", Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Wrong OTP", Toast.LENGTH_SHORT).show()
                    } } } } }
