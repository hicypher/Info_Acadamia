package com.recart

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
//import com.hacktech19.RecyclerAdapters.RecyclerAdapterItemInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.recart.Model.ItemInfo
import com.recart.payment_activity.Companion.isConnectionAvailable
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener
import com.shreyaspatil.EasyUpiPayment.model.PaymentApp
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails
import kotlinx.android.synthetic.main.about.*
import kotlinx.android.synthetic.main.activity_addnewitem.*
import kotlinx.android.synthetic.main.new_order.*
import kotlinx.android.synthetic.main.payment.*
import kotlinx.android.synthetic.main.wallet.*
import om.recart.RecyclerAdapters.RecyclerAdapterItemInfo
import java.text.SimpleDateFormat
import java.util.*

class wallet : AppCompatActivity(), PaymentStatusListener {
    internal  var  UPI_PAYMENT = 0
    private var PRIVATE_MODE=0
    private val PREF_NAME="PrefExpiry"
   public var status = ""
    internal  var myDB: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallet)

        back_wallet.setOnClickListener {
            Log.d("#999", "forget")
            val j = Intent(this, home::class.java)
            Log.d("#999", "forget2")
            startActivity(j)
            finish()
            Log.d("#999", "forget3")
        }

        type_wallet.setText("Wallet")
        val sPref: SharedPreferences =getSharedPreferences(PREF_NAME,PRIVATE_MODE)
        var usernamea=sPref.getString("UserID","")
        var cd = ConnectionDetector(this)
        var isInternet = false
        isInternet = cd.isConnectingToInternet
        if (!isInternet) {
            Toast.makeText(this, "INTERNET Not present", Toast.LENGTH_LONG).show()
        }
        if (isInternet) {

                Log.d("#0000", "login6 ")
                val docRef = myDB.collection("Members").document(usernamea.toString())
                Log.d("#0000", "login7 ")
                docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                    Log.d("#0000", "login8 ")
                    if (task.isSuccessful) {
                        Log.d("#0000", "login9 ")
                        val document = task.result
                      var  wallet = document!!.get("Wallet").toString()
                       if (wallet == null) {
                           myDB.collection("Members").document(usernamea.toString()).update(
                               "Wallet",
                               "0"
                           )
                               .addOnCompleteListener {
                                   balance.setText("0")
                                   // Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_LONG).show()


                               }.addOnFailureListener { e ->
                                   Log.w("111", "Error adding document", e)
                                   // Toast.makeText(this, "Too Many Changes\nIf You Want To Change Then LOUGOUT And LOGIN Again ", Toast.LENGTH_LONG).show()
                               }
                       }
                        else{
                           balance.setText("$wallet")
                       }

                        Log.d("#0000", "login10 ")
                       } })  }
        else {
            if (!isInternet)
                Toast.makeText(this, "INTERNET Not Present||check your connection", Toast.LENGTH_SHORT).show()
        }
        paylayoutw.isVisible=false
        amount.isEnabled=false
        amount.isVisible=false
        paylayoutw.animate().translationYBy(2000f).setDuration(10).start()
var flag=1

            recharge.setOnClickListener {

                if (isInternet) {
                    if (flag==1) {
                        amount.isEnabled=true
                        amount.isVisible=true
                        recharge.setText("ok")
                        recharge.setTextSize(40f)
                        flag=2

                    }
                    else{
                     if (amount.text.toString()==""||amount.text.toString().toInt()<1||amount.text.toString()==null){
                         amount.setError("Minimum ₹1")
                         Toast.makeText(this, "Minimum amount should be ₹1", Toast.LENGTH_SHORT).show()
                     }
                        else {
                         paylayoutw.isVisible = true
                         recharge.setTextSize(20f)
                         recharge.setText("select app below")
                         recharge.isClickable = false
                         paylayoutw.animate().translationYBy(-2000f).setDuration(100).start()
                     }
                    }
            }
                else {
                    if (!isInternet)
                        Toast.makeText(this, "INTERNET Not Present||check your connection", Toast.LENGTH_SHORT).show()
                }
        }
        val date = Date()
        val formatterDate = SimpleDateFormat("dd-MM")
        var strDate = formatterDate.format(date)
        val formatterDateTime = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        var strDateTime = formatterDateTime.format(date)
        val formatDateTime = SimpleDateFormat("HH:mm:ss")
        var dateTime = formatDateTime.format(date)
        var strPayID = strDate + dateTime
        strPayID = strPayID.replace("[^a-zA-Z0-9]".toRegex(), "").toString()
        var order_id = strPayID + usernamea
        val name = "ReCart"
        val upiId = "recart@apl"
        val note="$order_id"

        googlepayw.setOnClickListener {
            if (amount.text.toString()==""||amount.text.toString().toInt()<1||amount.text.toString()==null){
                amount.setError("Minimum ₹1")
                Toast.makeText(this, "Minimum amount should be ₹1", Toast.LENGTH_SHORT).show()
            }
            else {
            payUsingUpi(amount.text.toString(), upiId, name, note,"com.google.android.apps.nbu.paisa.user")
            // payUsingUpi(amount, upiId, name, note,"in.org.npci.upiapp")
                }
        }
        amazonpayw.setOnClickListener {
            if (amount.text.toString()==""||amount.text.toString().toInt()<1||amount.text.toString()==null){
                amount.setError("Minimum ₹1")
                Toast.makeText(this, "Minimum amount should be ₹1", Toast.LENGTH_SHORT).show()
            }
            else {
            payUsingUpi(amount.text.toString(), upiId, name, note,"in.amazon.mShop.android.shopping")
                }
        }
        paytmw.setOnClickListener {
            if (amount.text.toString()==""||amount.text.toString().toInt()<1||amount.text.toString()==null){
                amount.setError("Minimum ₹1")
                Toast.makeText(this, "Minimum amount should be ₹1", Toast.LENGTH_SHORT).show()
            }
            else {
                payUsingUpi(amount.text.toString(), upiId, name, note, "net.one97.paytm")
            }
        }
        bhimpayw.setOnClickListener {
            if (amount.text.toString()==""||amount.text.toString().toInt()<1||amount.text.toString()==null){
                amount.setError("Minimum ₹1")
                Toast.makeText(this, "Minimum amount should be ₹1", Toast.LENGTH_SHORT).show()
            }
            else {
                payUsingUpi(amount.text.toString(), upiId, name, note, "in.org.npci.upiapp")
            }
        }
        phonepew.setOnClickListener {
            if (amount.text.toString()==""||amount.text.toString().toInt()<1||amount.text.toString()==null){
                amount.setError("Minimum ₹1")
                Toast.makeText(this, "Minimum amount should be ₹1", Toast.LENGTH_SHORT).show()
            }
            else {
                pay(amount.text.toString(), upiId, name, note, "com.phonepe.app")
            }
        }


    }
    internal fun payUsingUpi(amount:String, upiId:String, name:String, note:String,packagename:String) {
        Log.d("UPI", "select app 0")

        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build()
        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.setData(uri)
        upiPayIntent.setPackage(packagename)
        // will always show a dialog to user to choose an app
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")
        // check if intent resolves
        Log.d("UPI", "select app 2")

        if (null != chooser.resolveActivity(getPackageManager()))
        {
            startActivityForResult(chooser, UPI_PAYMENT)
        }
        else
        {
            Toast.makeText(this@wallet, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            UPI_PAYMENT -> if ((RESULT_OK === resultCode) || (resultCode == 11))
            {
                if (data != null)
                {
                    val trxt = data.getStringExtra("response")
                    Log.d("UPI", "onActivityResult: " + trxt)
                    val dataList = ArrayList<String>()
                    dataList.add(trxt)
                    upiPaymentDataOperation(dataList)
                }
                else
                {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"+data!!.getStringExtra("response"))
                    val dataList = ArrayList<String>()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            }
            else
            {
                Log.d("UPI", "onActivityResult: " + "Return data is null") //when user simply back without payment
                val dataList = ArrayList<String>()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }
    }
    private fun upiPaymentDataOperation(data:ArrayList<String>) {
        if (isConnectionAvailable(this@wallet))
        {
            var str = data.get(0)
            Log.d("UPI", "upiPaymentDataOperation: " + str)
            var paymentCancel = ""
            if (str == null) str = "discard"

            var approvalRefNo = ""
            val response = str.split(("&").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            for (i in response.indices)
            {
                val equalStr = response[i].split(("=").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                if (equalStr.size >= 2)
                {
                    if (equalStr[0].toLowerCase() == "Status".toLowerCase())
                    {
                        status = equalStr[1].toLowerCase()
                    }
                    else if (equalStr[0].toLowerCase() == "ApprovalRefNo".toLowerCase() || equalStr[0].toLowerCase() == "txnRef".toLowerCase())
                    {
                        approvalRefNo = equalStr[1]
                    }
                }
                else
                {
                    paymentCancel = "Payment cancelled by user."
                }
            }
            if (status == "success")
                rechargew()
            else if ("Payment cancelled by user." == paymentCancel)
            {
                Toast.makeText(this@wallet, "Payment cancelled by user.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this@wallet, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            Toast.makeText(this@wallet, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show()
        }
    }
    companion object {
        @SuppressLint("MissingPermission")
        fun isConnectionAvailable(context: Context):Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null)
            {
                val netInfo = connectivityManager.getActiveNetworkInfo()
                if ((netInfo != null && netInfo.isConnected()
                            && netInfo.isConnectedOrConnecting()
                            && netInfo.isAvailable()))
                {
                    return true
                }
            }
            return false
        }
    }
    private fun pay(amount:String, upiId:String, name:String, note:String,packagename:String) {
        val payeeVpa: String = upiId.toString()
        val payeeName: String = name.toString()
        val transactionId: String = note.toString()
        val transactionRefId: String = note.toString()
        val description: String = note.toString()
        val amount: String = amount.toString()
        // val paymentAppChoice = findViewById<RadioButton>(radioAppChoice.getCheckedRadioButtonId())
        // START PAYMENT INITIALIZATION
        val mEasyUpiPayment = EasyUpiPayment.Builder()
            .with(this)
            .setPayeeVpa(payeeVpa)
            .setPayeeName(payeeName)
            .setTransactionId(transactionId)
            .setTransactionRefId(transactionRefId)
            .setDescription(description)
            .setAmount(amount.toDouble().toString())
            .build()
        // Register Listener for Events
        mEasyUpiPayment.setPaymentStatusListener(this)
        mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.PHONE_PE)
        //    R.id.app_amazonpay -> mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.AMAZON_PAY)
        //  R.id.app_bhim_upi -> mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.BHIM_UPI)
        //R.id.app_google_pay -> mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.GOOGLE_PAY)
        // R.id.app_paytm -> mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.PAYTM)
        // Check if app exists or not
        if (mEasyUpiPayment.isDefaultAppExist) {
            onAppNotFound()
            return
        }
        // END INITIALIZATION
// START PAYMENT
        mEasyUpiPayment.startPayment()
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails) { // Transaction Completed
        Log.d("TransactionDetails", transactionDetails.toString())
        //  paymentdetails.setText(transactionDetails.toString())
    }
    override fun onTransactionSuccess() { // Payment Success
        rechargew()
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        paymentstatusw.setText("Order Placed Successfully")
    }
    override fun onTransactionSubmitted() { // Payment Pending
        Toast.makeText(this, "Pending | Submitted", Toast.LENGTH_SHORT).show()
        paymentstatusw.setText("Payment is pending, contact ReCart team")
    }
    override  fun onTransactionFailed() { // Payment Failed
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        paymentdetailsw.setText("Order not placed, try again contact ReCart team if psyment deducted from account")
        paymentstatusw.setText("Order not placed")
    }
    override fun onTransactionCancelled() { // Payment Cancelled by User
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        paymentdetailsw.setText("Payment Cancelled, try again OR contact ReCart team if payment deducted from account")
        paymentstatusw.setText("Order not placed")
    }
    override fun onAppNotFound() {
        Toast.makeText(this, "Payment App Not Found", Toast.LENGTH_SHORT).show()
    }
    fun rechargew()
    {
        Toast.makeText(this, "Please wait a moment", Toast.LENGTH_LONG).show()
        var total=balance.text.toString().toInt()+amount.text.toString().toInt()
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        myDB.collection("Members").document(sPref.getString("UserID", "").toString()).update(
            "Wallet",
            "$total"
        )
            .addOnCompleteListener {
                // Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_LONG).show()
                val order_datet = Date()
                val formatte_date = SimpleDateFormat("dd/MM/YY")
                var order_date = formatte_date.format(order_datet)
                var id=intent.getStringExtra("id")
                val date = Date()
                val formatterDate = SimpleDateFormat("dd-MM-yyyy")
                var strDate = formatterDate.format(date)
                val formatterDateTime = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                var strDateTime = formatterDateTime.format(date)
                val formatDateTime = SimpleDateFormat("HH:mm:ss")
                var dateTime = formatDateTime.format(date)
                var strPayID =  strDate + dateTime
                strPayID = strPayID.replace("[^a-zA-Z0-9]".toRegex(), "").toString()
                var order_id=strPayID+sPref.getString("UserID", "").toString()
                Log.d("#0", "success1")
                //Code to handle successful transaction here.
                Toast.makeText(this@wallet, "Transaction successful.", Toast.LENGTH_SHORT).show()
                // Log.d("UPI", "responseStr: " + approvalRefNo)

                Log.d("#0", "success2")
                val wallet = HashMap<String, Any>()
                wallet["UserID"] = sPref.getString("UserID", "").toString()
                wallet["status"] = "$status"
                wallet["Date"] =strDate.toString()
                wallet["Time"] =dateTime.toString()
                wallet["Amount"] =amount.text.toString()
                Log.d("#0", "success8")
                myDB.collection("Wallet").document(order_id.toString()).set(wallet)
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener { e -> Log.w("#999", "Error adding document", e) }
                Toast.makeText(this@wallet, "Recharge Successful", Toast.LENGTH_LONG).show()
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@wallet)
                builder.setTitle(R.string.app_name)
                builder.setIcon(R.mipmap.circlecroppedi)
                builder.setMessage("Your Wallet has been Recharge successfully")
                    .setCancelable(false)
                    .setPositiveButton("ok",
                        DialogInterface.OnClickListener { dialog, id -> finish();startActivity(
                            Intent(this,com.recart.wallet::class.java)
                        );walletnotification() })
                    .setNegativeButton("Help?",
                        DialogInterface.OnClickListener { dialog, id -> dialog.cancel();startActivity(
                            Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:8009619425")).putExtra("sms_body", "I have a problem regarding $id:")
                        ) })
                val alert: AlertDialog = builder.create()
                alert.show()


            }.addOnFailureListener { e ->
                Log.w("111", "Unexpected error || contact to ReCart support team", e)
                // Toast.makeText(this, "Too Many Changes\nIf You Want To Change Then LOUGOUT And LOGIN Again ", Toast.LENGTH_LONG).show()
            }
        Log.d("#0", "success")



    }
    fun walletnotification(){
        lateinit var notificationManager: NotificationManager

        lateinit var notificationChannel: NotificationChannel
        lateinit var builder: Notification.Builder
        val channelId="com.recart"
        val description="My Notification"


        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //  show.setOnClickListener {
        val intent = Intent(this,wallet::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                channelId,
                description,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(
                notificationChannel
            )

            builder = Notification.Builder(applicationContext, channelId)
                .setContentTitle("Wallet Recharge Successfully")
                .setContentText("tap for more details")
                .setSmallIcon(R.mipmap.circlecroppedi)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        } else {
            builder = Notification.Builder(applicationContext)
                .setContentTitle("Wallet Recharge Successfully")
                .setContentText("tap for more details")
                .setSmallIcon(R.mipmap.circlecroppedi)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }
        notificationManager.notify(0, builder.build())

    }
    override fun onBackPressed() {
        startActivity(Intent(this,home::class.java))
        super.onBackPressed()
        finish()
    }
}
