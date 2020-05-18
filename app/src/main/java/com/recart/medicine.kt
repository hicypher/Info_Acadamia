package com.recart

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.recart.edit_profile
import com.recart.R
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.change_image.*
import kotlinx.android.synthetic.main.change_image.*
import kotlinx.android.synthetic.main.change_image.*
import kotlinx.android.synthetic.main.change_image.change
import kotlinx.android.synthetic.main.medicine.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class medicine : AppCompatActivity() {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"


    var isChanged: Boolean? = false
    private var mainImageUri: Uri? = null
    internal lateinit var myDB: FirebaseFirestore
    var compressedUserImage: Bitmap? = null
    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.medicine)
        myDB = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference



        back_change_medicine.setOnClickListener {
            Log.d("#999", "forget")
            val j = Intent(this, activity_medicine::class.java)
            Log.d("#999", "forget2")
            startActivity(j)
            finish()
            Log.d("#999", "forget3")
        }

        medicine.setText("MEDICINE")
        medicine_image.setOnClickListener {
            /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this@image_activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this@image_activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                   // Snackbar.make(findViewById(R.id.setup_layout), "Please Grant permissions", Snackbar.LENGTH_SHORT).show()
                    ActivityCompat.requestPermissions(this@image_activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), READCODE)
                    ActivityCompat.requestPermissions(this@image_activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITECODE)

                } else {
                    bringImagePicker()
                }

            } else {*/
            bringImagePicker()
        }
        //}

        submit_m.setOnClickListener {
            var cd = ConnectionDetector(this)
            var isInternet = false
            isInternet = cd.isConnectingToInternet
            if (isInternet) {

                Toast.makeText(this, "Order Placing Please Wait....", Toast.LENGTH_SHORT).show()
                if (isChanged!!) {
                    val order_datet = Date()
                    val formatte_date = SimpleDateFormat("dd/MM/YY")
                    var order_date = formatte_date.format(order_datet)
                    val date = Date()
                    val formatterDate = SimpleDateFormat("dd-MM-YY")
                    var strDate = formatterDate.format(date)
                    val formatterDateTime = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                    var strDateTime = formatterDateTime.format(date)
                    val formatDateTime = SimpleDateFormat("HH:mm:ss")
                    var dateTime = formatDateTime.format(date)
                    var strPayID = strDate + dateTime
                    strPayID = strPayID.replace("[^a-zA-Z0-9]".toRegex(), "").toString()
                    var order_id = strPayID + sPref.getString("UserID", "").toString()
                    val newImageFile = File(mainImageUri!!.path!!)

                    try {
                        compressedUserImage = Compressor(this@medicine)
                            .setQuality(60)
                            .compressToBitmap(newImageFile)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    val baos = ByteArrayOutputStream()
                    compressedUserImage!!.compress(Bitmap.CompressFormat.JPEG, 60, baos)
                    val imageData = baos.toByteArray()

                    val image_path =
                        storageReference!!.child("medicine").child(order_id + "address" + ".jpg")
                    image_path.putBytes(imageData).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //Update Data on server Firestore database
                            Log.d("#999", "before firebase image uri")
                            image_path?.downloadUrl?.addOnSuccessListener(this@medicine) { uri ->
                                Log.d("#0", "success2")
                                val cart = HashMap<String, Any>()
                                cart["item_name"] = "Medicine"
                                //ADD REAL DATE
                                Log.d("#0", "success3")
                                cart["shop_name"] = "Medical Store"
                                //   cart["description"] = description.text!!.toString()
                                cart["prise"] = "Printed MRP"
                                Log.d("#0", "success4")
                                cart["image"] = uri!!.toString()
                                Log.d("#0", "success5")
                                cart["type"] = "Medicines"
                                Log.d("#0", "success6")
                                cart["UserID"] = sPref.getString("UserID", "").toString()
                                cart["quantity"] = medicine_amount.text!!.toString()
                                cart["search_history"] ="medical_store"+order_date.toString()

                                cart["status"] = "Order Placed."
                                cart["description"] = medicine_description.text!!.toString()
                                Log.d("#0", "success7")
                                cart["order id"] = order_id.toString()
                                cart["order_date"] = order_date.toString()
                                Log.d("#0", "success8")
                                //    var order=order_id+item_name!!.toString()+shop_name!!.toString()
                                myDB.collection("new_order").document(order_id.toString()).set(cart)
                                    .addOnSuccessListener {

                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(
                                            "#999",
                                            "Error adding document",
                                            e
                                        )
                                    }
                                Toast.makeText(
                                    this,
                                    "order placed...\ncheck status? go to new border.",
                                    Toast.LENGTH_LONG
                                ).show()
                                val j = Intent(this, home::class.java)
                                Log.d("#999", "forget2")
                                startActivity(j)
                            }

                        }
                    }

                } else {
                       if ((medicine_amount.text!!.toString()=="")||(medicine_description.text!!.toString()==""))
                       {
                           Toast.makeText(this, "Please enter atleast amount or description", Toast.LENGTH_LONG).show()

                       }
                    else{


                    val order_datet = Date()
                    val formatte_date = SimpleDateFormat("dd/MM/YY")
                    var order_date = formatte_date.format(order_datet)
                    val date = Date()
                    val formatterDate = SimpleDateFormat("dd-MM-YY")
                    var strDate = formatterDate.format(date)
                    val formatterDateTime = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                    var strDateTime = formatterDateTime.format(date)
                    val formatDateTime = SimpleDateFormat("HH:mm:ss")
                    var dateTime = formatDateTime.format(date)
                    var strPayID = strDate + dateTime
                    strPayID = strPayID.replace("[^a-zA-Z0-9]".toRegex(), "").toString()
                    var order_id = strPayID + sPref.getString("UserID", "").toString()
                    val cart = HashMap<String, Any>()
                    cart["item_name"] = "Medicine"
                    //ADD REAL DATE
                    Log.d("#0", "success3")
                    cart["shop_name"] = "Medical Store"
                    //   cart["description"] = description.text!!.toString()
                    cart["prise"] = "Printed MRP"
                    cart["search_history"] ="medical_store"+order_date.toString()
                    Log.d("#0", "success4")
                    cart["image"] = ""
                    Log.d("#0", "success5")
                    cart["type"] = "Medicines"
                    Log.d("#0", "success6")
                    cart["UserID"] = sPref.getString("UserID", "").toString()
                    cart["quantity"] = medicine_amount.text!!.toString()
                    cart["status"] = "Order Placed."
                    cart["description"] = medicine_description.text!!.toString()
                    Log.d("#0", "success7")
                    cart["order id"] = order_id.toString()
                    cart["order_date"] = order_date.toString()
                    Log.d("#0", "success8")
                    //    var order=order_id+item_name!!.toString()+shop_name!!.toString()
                    myDB.collection("new_order").document(order_id.toString()).set(cart)
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener { e -> Log.w("#999", "Error adding document", e) }
                    Toast.makeText(
                        this,
                        "order placed...\ncheck status? go to new border.",
                        Toast.LENGTH_LONG
                    ).show()
                    val j = Intent(this, home::class.java)
                    Log.d("#999", "forget2")
                    startActivity(j)
                    finish()
                       }
                }
            }
            else{
                Toast.makeText(this, "INTERNET Not present", Toast.LENGTH_LONG).show()

            }
        }




    }
    override fun onBackPressed() {
        startActivity(Intent(this,activity_medicine::class.java))
        super.onBackPressed()
        finish()
    }
            public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    val result = CropImage.getActivityResult(data)
                    if (resultCode == Activity.RESULT_OK) {

                        mainImageUri = result.uri
                        medicine_image.setImageURI(mainImageUri)
                        isChanged = true

                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        val error = result.error
                        //    Snackbar.make(findViewById(R.id.setup_layout),error.toString(),Snackbar.LENGTH_SHORT).show();

                    }
                }
            }

            private fun bringImagePicker() {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(3,4)
                    .start(this@medicine)
            }

            companion object {
            private val READCODE = 1
            private val WRITECODE = 2
        }



    }

