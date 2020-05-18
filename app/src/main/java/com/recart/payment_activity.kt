package com.recart


import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener
import com.shreyaspatil.EasyUpiPayment.model.PaymentApp
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails
import kotlinx.android.synthetic.main.about.view.*
import kotlinx.android.synthetic.main.payment.*
import java.text.SimpleDateFormat
import java.util.*
//import javax.swing.text.StyleConstants.setIcon


class payment_activity: AppCompatActivity(),PaymentStatusListener{
    internal lateinit var amountEt:TextView
    internal lateinit var noteEt:TextView
    internal lateinit var nameEt:EditText
    internal lateinit var upiIdEt:EditText
    internal lateinit var send:Button
    internal  var  UPI_PAYMENT = 0
    internal lateinit var mydB: FirebaseFirestore
    internal  var myDB: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"
    var amount=""
    var  wallet="0"
    var total_amount_pay=""
    var update_wallet="1"
    var update_wallet_balance=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment)
        paylayout.animate().translationYBy(2000f).setDuration(10).start()
        check_wallet.animate().translationYBy(2000f).setDuration(10).start()
        proceed.isVisible=false
        textpaymsg.isVisible=false
        Log.d("#999", "payment activity started")
        initializeViews()
        amountEt = findViewById(R.id.amount_et)
        noteEt = findViewById(R.id.note)
        var mydb: FirebaseFirestore
        mydb = FirebaseFirestore.getInstance()
        noteEt.setText("Order id:"+intent.getStringExtra("id"))
var total_a=0
      amount=   intent.getStringExtra("amount")
    var item_name=    intent.getStringExtra("item_name")
    var shop_name=     intent.getStringExtra("shop_name")
    var quantity=   intent.getStringExtra("quantity")
     var image=   intent.getStringExtra("image")
    var flag=    intent.getStringExtra("flag")
        var id=intent.getStringExtra("id")
         if (amount.toInt()<=100)
         {
             var total=(amount.toInt()*20)/100
             amountEt.setText("Total: ₹"+intent.getStringExtra("amount")+"+"+total)
             total_a=amount.toInt()+total
         }
         else  if ((amount.toInt()>100)&&(amount.toInt()<=250))
         {
             var total=(amount.toInt()*16)/100
             amountEt.setText("Total: ₹"+intent.getStringExtra("amount")+"+"+total)
             total_a=amount.toInt()+total
         }
     else  if ((amount.toInt()>250)&&(amount.toInt()<=350))
        {
            var total=(amount.toInt()*15)/100
            amountEt.setText("Total: ₹"+intent.getStringExtra("amount")+"+"+total)
            total_a=amount.toInt()+total
        }
         else  if ((amount.toInt()>350)&&(amount.toInt()<=500))
         {
             var total=(amount.toInt()*12)/100
             amountEt.setText("Total: ₹"+intent.getStringExtra("amount")+"+"+total)
             total_a=amount.toInt()+total
         }
         else  if ((amount.toInt()>500)&&(amount.toInt()<=750))
         {
             var total=(amount.toInt()*14)/100
             amountEt.setText("Total: ₹"+intent.getStringExtra("amount")+"+"+total)
             total_a=amount.toInt()+total
         }
         else  if ((amount.toInt()>750)&&(amount.toInt()<=1000))
         {
             var total=(amount.toInt()*13)/100
             amountEt.setText("Total: ₹"+intent.getStringExtra("amount")+"+"+total)
             total_a=amount.toInt()+total
         }
         else  if ((amount.toInt()>1000)&&(amount.toInt()<=1250))
         {
             var total=(amount.toInt()*12)/100
             amountEt.setText("Total: ₹"+intent.getStringExtra("amount")+"+"+total)
             total_a=amount.toInt()+total
         }
         else  if (amount.toInt()>1250)
         {
             var total=(amount.toInt()*10)/100
             amountEt.setText("Total: ₹"+intent.getStringExtra("amount")+"+"+total)
             total_a=amount.toInt()+total
         }
        else
         {
             Toast.makeText(this@payment_activity, "Somthing went wrong please try again later", Toast.LENGTH_LONG).show()
             val j = Intent(this, home::class.java)
             Log.d("#999", "forget2")
             startActivity(j)
         }
        amount=total_a.toString()
        Log.d("#total", "$amount")
        payment.setText("PAYMENT")
var time="time"
        var off_time=""
       var  message=""
        mydb.collection("setting").document(time.toString()).get().addOnCompleteListener {
                task ->
            Log.d("#999", "IN")
            if (task.isSuccessful) {
                val doc=task.result
                Log.d("#999", "in reply")
                  off_time= doc!!.get("off_time").toString()
                  message= doc!!.get("message").toString()


            }
        }.addOnSuccessListener {
            val date = Date()
            val formatDateTime = SimpleDateFormat("HHmmss")
            var dateTime = formatDateTime.format(date)


                send.setOnClickListener(object:View.OnClickListener {
                    override fun onClick(view:View) {
                        //Getting the values from the EditTexts
                        var cd = ConnectionDetector(this@payment_activity)
                        var isInternet = false
                        isInternet = cd.isConnectingToInternet
                        if (isInternet) {
                            if (dateTime<off_time) {
                                send.setText("select app below")
                                send.isClickable=false
                                check_wallet.animate().translationYBy(-2000f).setDuration(100).start()
                                paylayout.animate().translationYBy(-2000f).setDuration(100).start()

                                 amount = amount
                                total_amount_pay=amount
                                textpaymsg.isVisible=true
                                textpaymsg.setText("You need to pay ₹$amount")
                                val note = intent.getStringExtra("id")
                                Log.d("#999", "$amount + $note")
                                val name = "ReCart"
                                val upiId = "recart@apl"
                                googlepay.setOnClickListener {
                                   payUsingUpi(amount, upiId, name, note,"com.google.android.apps.nbu.paisa.user")
                                   // payUsingUpi(amount, upiId, name, note,"in.org.npci.upiapp")
                                }
                                amazonpay.setOnClickListener {
                                    payUsingUpi(amount, upiId, name, note,"in.amazon.mShop.android.shopping")
                                }
                                paytm.setOnClickListener {
                                    payUsingUpi(amount, upiId, name, note,"net.one97.paytm")
                                }
                                bhimpay.setOnClickListener {
                                    payUsingUpi(amount, upiId, name, note,"in.org.npci.upiapp")
                                }
                                phonepe.setOnClickListener {
                                    pay(amount, upiId, name, note,"com.phonepe.app")
                                }
                          //      payUsingUpi(amount, upiId, name, note)
                            }
                            else{
                                Toast.makeText(this@payment_activity, "$message", Toast.LENGTH_LONG).show()
                            }
                        }
                        else{
                            Toast.makeText(this@payment_activity, "INTERNET Not present", Toast.LENGTH_LONG).show()
                        }
                    }
                })
        }
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        var cd = ConnectionDetector(this@payment_activity)
        var isInternet = false
        isInternet = cd.isConnectingToInternet
        if (isInternet) {

            Log.d("#0000", "login6 ")
            val docRef = myDB.collection("Members").document(sPref.getString("UserID", "").toString())
            Log.d("#0000", "login7 ")
            docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                Log.d("#0000", "login8 ")
                if (task.isSuccessful) {
                    Log.d("#0000", "login9 ")
                    val document = task.result
                      wallet = document!!.get("Wallet").toString()
                    if (wallet == null) {
                        myDB.collection("Members").document(sPref.getString("UserID", "").toString()).update(
                            "Wallet",
                            "0"
                        )
                            .addOnCompleteListener {
                                balancew.setText("(₹0)")
                                // Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_LONG).show()


                            }.addOnFailureListener { e ->
                                Log.w("111", "Error adding document", e)
                                // Toast.makeText(this, "Too Many Changes\nIf You Want To Change Then LOUGOUT And LOGIN Again ", Toast.LENGTH_LONG).show()
                            }
                    }
                    else{
                        balancew.setText("(₹$wallet)")
                    }

                    Log.d("#0000", "login10 ")
                } })  }
        else {
            if (!isInternet)
                Toast.makeText(this, "INTERNET Not Present||check your connection", Toast.LENGTH_SHORT).show()
        }
/*var check=1
        checkboxw.setOnClickListener {
            if(check==1) {
                balancew.setText("abc")
                check=2
            }
            else
            {
                balancew.setText("qwe")
                check=1
            }
        }*/


    }
    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            when (view.id) {
                R.id.checkboxw -> {
                    if (checked) {
                        if (wallet == "0") {
                            checkboxw.isChecked=false
                            Toast.makeText(
                                this,
                                "Your wallet balance is 0 || To place the order pay amount using below app",
                                Toast.LENGTH_LONG
                            ).show()

                        } else {
                            if (wallet.toInt() >= amount.toString().toInt()) {
                                textpaymsg.isVisible = false
                                proceed.isVisible = true
                                paylayout.isVisible = false
                                var new = wallet.toInt() - amount.toString().toInt()
                                balancew.setText("(₹$wallet-₹$amount)")
                                update_wallet_balance = new.toString()
                                update_wallet = "2"
                                proceed.setOnClickListener {
                                    placefinalorder()
                                }


                            } else {
                                var new = amount.toString().toInt() - wallet.toInt()
                                balancew.setText("(₹0)")
                                proceed.isVisible = false
                                paylayout.isVisible = true
                                textpaymsg.isVisible = true
                                textpaymsg.setText("You need to pay ₹$new")
                                amount = new.toString()
                                update_wallet_balance = "0"
                                update_wallet = "2"
                            }
                        }
                        // Put some meat on the sandwich
                    } else {
                        if (wallet == "0") {
                            checkboxw.isChecked=false
                            Toast.makeText(this, "Your wallet balance is 0 || To place the order pay amount using below app", Toast.LENGTH_LONG).show()

                        }
                        else{
                        proceed.isVisible = false
                        paylayout.isVisible = true
                        textpaymsg.isVisible = true
                        textpaymsg.setText("You need to pay ₹$total_amount_pay")
                        balancew.setText("(₹$wallet)")
                        amount = total_amount_pay
                        update_wallet = "1"
                        // Remove the meat
                    }
                }
                }
                // TODO: Veggie sandwich
            }
        }
    }

    internal fun initializeViews() {

        send = findViewById(R.id.send)

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
            Toast.makeText(this@payment_activity, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show()
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
        if (isConnectionAvailable(this@payment_activity))
        {
            var str = data.get(0)
            Log.d("UPI", "upiPaymentDataOperation: " + str)
            var paymentCancel = ""
            if (str == null) str = "discard"
            var status = ""
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
            placefinalorder()
            else if ("Payment cancelled by user." == paymentCancel)
            {
                Toast.makeText(this@payment_activity, "Payment cancelled by user.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this@payment_activity, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            Toast.makeText(this@payment_activity, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show()
        }
    }
    companion object {
        @SuppressLint("MissingPermission") 
        fun isConnectionAvailable(context:Context):Boolean {
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
       placefinalorder()
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        paymentstatus.setText("Order Placed Successfully")
    }
    override fun onTransactionSubmitted() { // Payment Pending
        Toast.makeText(this, "Pending | Submitted", Toast.LENGTH_SHORT).show()
        paymentstatus.setText("Payment is pending, contact ReCart team")
    }
    override  fun onTransactionFailed() { // Payment Failed
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
        paymentdetails.setText("Order not placed, try again contact ReCart team if psyment deducted from account")
        paymentstatus.setText("Order not placed")
    }
    override fun onTransactionCancelled() { // Payment Cancelled by User
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        paymentdetails.setText("Payment Cancelled, try again OR contact ReCart team if payment deducted from account")
        paymentstatus.setText("Order not placed")
    }
    override fun onAppNotFound() {
        Toast.makeText(this, "Payment App Not Found", Toast.LENGTH_SHORT).show()
    }
    fun placefinalorder()
    {
        Log.d("#0", "success")
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        if (update_wallet=="2") {
            myDB.collection("Members").document(sPref.getString("UserID", "").toString()).update(
                "Wallet",
                "$update_wallet_balance"
            )
                .addOnCompleteListener {
                    // Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_LONG).show()


                }.addOnFailureListener { e ->
                    Log.w("111", "Error adding document", e)
                    // Toast.makeText(this, "Too Many Changes\nIf You Want To Change Then LOUGOUT And LOGIN Again ", Toast.LENGTH_LONG).show()
                }
        }
        val order_datet = Date()
        val formatte_date = SimpleDateFormat("dd/MM/YY")
        var order_date = formatte_date.format(order_datet)
        var amount=   intent.getStringExtra("amount")
        var item_name=    intent.getStringExtra("item_name")
        var shop_name=     intent.getStringExtra("shop_name")
        var quantity=   intent.getStringExtra("quantity")
        var image=   intent.getStringExtra("image")
        var type=   intent.getStringExtra("type")
        var flag=    intent.getStringExtra("flag")
        var id=intent.getStringExtra("id")
        val date = Date()
        val formatterDate = SimpleDateFormat("dd-MM")
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
        Toast.makeText(this@payment_activity, "Transaction successful.", Toast.LENGTH_SHORT).show()
       // Log.d("UPI", "responseStr: " + approvalRefNo)
        if (flag=="1"){
            Log.d("#0", "success2")
            val cart = HashMap<String, Any>()
            cart["item_name"] = item_name!!.toString()
            //ADD REAL DATE
            Log.d("#0", "success3")
            cart["shop_name"] = shop_name!!.toString()
            //   cart["description"] = description.text!!.toString()
            cart["prise"] = amount!!.toString()
            Log.d("#0", "success4")
            cart["image"] = image!!.toString()
            Log.d("#0", "success5")
            cart["type"] = type!!.toString()
            cart["description"] =item_name!!.toString()
            cart["search_history"] =  shop_name!!.toString()+order_date.toString()
            Log.d("#0", "success6")
            cart["UserID"] =  sPref.getString("UserID", "").toString()
            cart["quantity"] = quantity!!.toString()
            cart["status"] = "Order Placed."
            Log.d("#0", "success7")
            cart["order id"] =order_id.toString()
            cart["order_date"] =order_date.toString()
            Log.d("#0", "success8")
            var order=order_id+item_name!!.toString()+shop_name!!.toString()
            myDB.collection("new_order").document(order.toString()).set(cart)
                .addOnSuccessListener {

                }
                .addOnFailureListener { e -> Log.w("#999", "Error adding document", e) }
            Toast.makeText(this@payment_activity, "order placed...\nGo to New Order for checking status", Toast.LENGTH_LONG).show()
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@payment_activity)
            builder.setTitle(R.string.app_name)
            builder.setIcon(R.mipmap.circlecroppedi)
            builder.setMessage("Your Order has been placed successfully")
                .setCancelable(false)
                .setPositiveButton("ok",
                    DialogInterface.OnClickListener { dialog, id -> finish();startActivity(
                        Intent(this,home::class.java)
                    );orderplacenotification() })
                .setNegativeButton("Help?",
                    DialogInterface.OnClickListener { dialog, id -> dialog.cancel();startActivity(
                        Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:8009619425")).putExtra("sms_body", "I have a problem regarding $id:")
                    ) })
            val alert: AlertDialog = builder.create()
            alert.show()
        }
        else {
            Log.d("#999", "Data Fetch")

            mydB = FirebaseFirestore.getInstance()
            Log.d("#999", "Data Fetch1")

            mydB.collection("cart").whereEqualTo("UserID", sPref.getString("UserID", ""))
                .get()
                .addOnCompleteListener { task ->
                    Log.d("#999", "IN")
                    if (task.isSuccessful) {
                        Log.d("#999", "IN2")
                        for (doc in task.result!!) {
                            Log.d("#999", "Data Fetch3")
                            val cart = HashMap<String, Any>()
                            Log.d("#999", "Data Fetch4")
                            var status = doc.data["status"]!!.toString()
                            if (status=="1"){
                                cart["item_name"] = doc.data["item_name"]!!.toString()
                                cart["UserID"] = doc.data["UserID"]!!.toString()
                                cart["shop_name"] = doc.data["shop_name"]!!.toString()
                                cart["image"] = doc.data["image"]!!.toString()
                                cart["type"] = doc.data["type"]!!.toString()
                                cart["search_history"] = doc.data["shop_name"]!!.toString()+order_date.toString()
                                cart["prise"] = doc.data["total_amount"]!!.toString()
                                cart["description"] =  doc.data["item_name"]!!.toString()
                                cart["quantity"] = doc.data["quantity"]!!.toString().toInt()
                                cart["status"] ="Order Placed. "
                                cart["order id"] =order_id.toString()
                                cart["order_date"] =order_date.toString()
                                Log.d("#999", "Data Fetch6")
                                var order=order_id+doc.data["item_name"]!!.toString()+doc.data["shop_name"]!!.toString()
                                //        increase+= doc.data["increase"]!!.toString().toInt()
                                myDB.collection("new_order").document(order.toString()).set(cart)
                                    .addOnSuccessListener {
                                        Log.d("#999", "Data Fetch7")

                                    }
                                    .addOnFailureListener { e -> Log.w("#999", "Error adding document", e) }
                            } }
                        Toast.makeText(this@payment_activity, "order placed...\nGo to New Order for checking status", Toast.LENGTH_LONG).show()
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@payment_activity)
                        builder.setTitle(R.string.app_name)
                        builder.setIcon(R.mipmap.circlecroppedi)
                        builder.setMessage("Your Order has been placed successfully")
                            .setCancelable(false)
                            .setPositiveButton("ok",
                                DialogInterface.OnClickListener { dialog, id -> finish();startActivity(
                                    Intent(this,home::class.java)
                                );orderplacenotification() })
                            .setNegativeButton("Help?",
                                DialogInterface.OnClickListener { dialog, id -> dialog.cancel();startActivity(
                                    Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:8009619425")).putExtra("sms_body", "I have a problem regarding $id:")

                                ) })
                        val alert: AlertDialog = builder.create()
                        alert.show()

                    }
                }
        }
    }
    fun orderplacenotification(){
        lateinit var notificationManager: NotificationManager

        lateinit var notificationChannel: NotificationChannel
        lateinit var builder: Notification.Builder
        val channelId="com.recart"
        val description="My Notification"


        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //  show.setOnClickListener {
        val intent = Intent(this,new_order::class.java)
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
                .setContentTitle("Order Placed Successfully")
                .setContentText("tap for more details")
                .setSmallIcon(R.mipmap.circlecroppedi)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        } else {
            builder = Notification.Builder(applicationContext)
                .setContentTitle("Order Placed Successfully")
                .setContentText("tap for more details")
                .setSmallIcon(R.mipmap.circlecroppedi)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }
        notificationManager.notify(0, builder.build())

    }
}