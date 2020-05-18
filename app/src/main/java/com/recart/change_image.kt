package com.recart

import android.app.Activity
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class change_image : AppCompatActivity() {
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
        setContentView(R.layout.change_image)
        myDB = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        var imguri=""
        imguri=sPref.getString("image","").toString()
        if (imguri != null) {
            Log.d("#999", "DocumentSnapshot data: " + imguri+imguri)
            if(!imguri!!.isEmpty()) {
                Picasso.get().load(imguri).into(edit); Log.d("#999", "img document")}
            else  Log.d("#999", "No such document")

        } else {
            Log.d("#999", "No such document")
        }


        if(!imguri!!.isEmpty()) {
            Picasso.get().load(imguri).into(edit); Log.d("#999", "img document")}
        else  Log.d("#999", "No such document")



        back_change_image.setOnClickListener {
            Log.d("#999", "forget")
            val j = Intent(this, edit_profile::class.java)
            Log.d("#999", "forget2")
            startActivity(j)
            finish()
            Log.d("#999", "forget3")
        }

        change.setText("CHANGE IMAGE")
        edit.setOnClickListener {
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

        save.setOnClickListener {
            var cd = ConnectionDetector(this)
            var isInternet = false
            isInternet = cd.isConnectingToInternet
            if (isInternet) {
                Toast.makeText(this, "IMAGE UPLOADING PLEASE WAIT....", Toast.LENGTH_SHORT).show()
                if (isChanged!!) {

                    val newImageFile = File(mainImageUri!!.path!!)

                    try {
                        compressedUserImage = Compressor(this@change_image)
                            .setQuality(60)
                            .compressToBitmap(newImageFile)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    val baos = ByteArrayOutputStream()
                    compressedUserImage!!.compress(Bitmap.CompressFormat.JPEG, 60, baos)
                    val imageData = baos.toByteArray()

                    val image_path =
                        storageReference!!.child("User_profile_photo")
                            .child(sPref.getString("UserID", "") + "address" + ".jpg")
                    image_path.putBytes(imageData).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //Update Data on server Firestore database
                            Log.d("#999", "before firebase image uri")
                            image_path?.downloadUrl?.addOnSuccessListener(this@change_image) { uri ->
                                myDB.collection("Members")
                                    .document(sPref.getString("UserID", "").toString()).update(
                                    "image",
                                    uri.toString()

                                ).addOnCompleteListener { task ->
                                    Log.d("#999", "before condition to dashboard")
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT)
                                            .show()
                                        sPref.edit().putString("image", uri.toString()).apply()
                                        Log.d("#999", "condition to dashboard")
                                        val i = Intent(this@change_image, edit_profile::class.java)
                                        startActivity(i)
                                        finish()
                                        Log.d("#999", "intent to dashboard")

                                        //  Log.i("CHECK",intentThatStartedThisActivity);
                                        // sendToMain(intentThatStartedThisActivity);

                                    }


                                }
                                // else {
                                //  Snackbar.make(findViewById(R.id.setup_layout), task.exception!!.message, Snackbar.LENGTH_SHORT).show()
                                // setupProgress.setVisibility(View.INVISIBLE)

                                //  }
                            }

                        }
                    }

                } else {
                    Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "INTERNET Not present", Toast.LENGTH_LONG).show()

            }

        }








    }
    override fun onBackPressed() {
        startActivity(Intent(this,edit_profile::class.java))
        super.onBackPressed()
        finish()
    }
            public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    val result = CropImage.getActivityResult(data)
                    if (resultCode == Activity.RESULT_OK) {

                        mainImageUri = result.uri
                        edit.setImageURI(mainImageUri)
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
                    .setAspectRatio(1, 1)
                    .start(this@change_image)
            }

            companion object {
            private val READCODE = 1
            private val WRITECODE = 2
        }



    }

