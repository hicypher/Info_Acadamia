package om.recart.RecyclerAdapters

import android.content.Context
import android.content.Intent
import com.recart.Model.ItemInfo




import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage

import java.nio.charset.IllegalCharsetNameException

import android.app.Activity.RESULT_OK
import android.content.SharedPreferences
import android.os.Build
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.recart.*
import java.text.SimpleDateFormat
import java.util.*

//import com.recart.product_image
//import com.recart.update_item

//import com.hacktech19.product_image
//import com.hacktech19.update_item
//import com.hacktech19.home
class RecyclerAdapterItemInfo(internal var datalist: List<ItemInfo>, internal var context: Context) : RecyclerView.Adapter<RecyclerAdapterItemInfo.View_Holder>() {
    internal  var myDB: FirebaseFirestore=FirebaseFirestore.getInstance()
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_data_list, parent, false)
        return View_Holder(view)
    }

    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        val item_name = datalist[position].item_name
        val shop_name = datalist[position].shop_name
        val prise = datalist[position].prise
        var userid = datalist[position].userid
        var type = datalist[position].type
        var status = datalist[position].status
        var food_image = datalist[position].food_image

        if (food_image != null) {
            Log.d("#999", "DocumentSnapshot data: " + food_image)
            if (!food_image!!.isEmpty()) {
                Picasso.get().load(food_image).into(holder.food_image); Log.d(
                    "#999",
                    "img document"
                )
            } else Log.d("#999", "No such document")

        } else {
            Log.d("#999", "No such document")
        }


        if (!food_image!!.isEmpty()) {
            Picasso.get().load(food_image).into(holder.food_image); Log.d("#999", "img document")
        }
        //  val ExpiryDate = datalist[position].item_expirydate
        //   val PublishDate = datalist[position].item_publishdate
        //    val Type = datalist[position].item_type
        //  val user_id = datalist[position].item_user_id
        //   val image1 = datalist[position].image1
//        val food_image = datalist[position].food_image
        val RID = datalist[position].RID
        var prise1 = prise.toString().toInt()
        holder.item_name.text = item_name
        holder.shop_name.text = shop_name
        holder.prise.text = prise
        //    holder.type.text = type

        //  holder.expirydate.text = ExpiryDate
        //     holder.publishdate.text = PublishDate
        //  holder.category.text = Type
        // Picasso.get().load(image1).into(holder.image1)
        // Picasso.get().load(image2).into(holder.image2)
        // Picasso.get().load(image3).into(holder.image3)
        //    holder.addimage.setOnClickListener {
        /*   val intent = Intent(context, product_image::class.java)

            val count = datalist[position].image_count
            intent.putExtra("UID", user_id)
            intent.putExtra("count", 1)
            context.startActivity(intent)*/

        // }
        // if (!image1!!.isEmpty())   Picasso.get().load(image1).into(holder.image1)
        //  if (!image2!!.isEmpty()) Picasso.get().load(image2).into(holder.image2)
        // if (!image3!!.isEmpty()) Picasso.get().load(image3).into(holder.image3)

        holder.button_minus.setOnClickListener {
            var cd = ConnectionDetector(context)
            var isInternet = false
            isInternet = cd.isConnectingToInternet
            if (isInternet) {
                if (holder.quantity.text.toString().toInt() <= 1) {

                } else {
                    var quantity1 = holder.quantity.text.toString().toInt() - 1
                    holder.quantity.text = quantity1.toString()
                    prise1 = prise1 - prise.toString().toInt()
                    holder.prise.text = "$prise1"

                }
            } else {
                Toast.makeText(context, "INTERNET Not present", Toast.LENGTH_LONG)
                    .show()

            }

        }
        holder.button_plus.setOnClickListener {
            var cd = ConnectionDetector(context)
            var isInternet = false
            isInternet = cd.isConnectingToInternet
            if (isInternet) {
                var quantity1 = holder.quantity.text.toString().toInt() + 1
                holder.quantity.text = quantity1.toString()
                prise1 = prise1 + prise.toString().toInt()
                holder.prise.text = "$prise1"

            } else {
                Toast.makeText(context, "INTERNET Not present", Toast.LENGTH_LONG)
                    .show()

            }
        }
        holder.add_to_cart.setOnClickListener {
            var cd = ConnectionDetector(context)
            var isInternet = false
            isInternet = cd.isConnectingToInternet
            if (isInternet) {

                var strPayID =
                    userid.toString()+"_"+item_name.toString()+"_"+shop_name.toString()
                Log.d("#888", strPayID)
                Toast.makeText(context, "Adding....", Toast.LENGTH_SHORT).show()
                val docRef = myDB.collection("cart").document(strPayID.toString())
                docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        var h = document!!.get("RID").toString()
                        if (h.toString() == strPayID.toString())
                            Toast.makeText(
                                context,
                                "Item Already In Cart",
                                Toast.LENGTH_SHORT
                            ).show()
                        else {
                            val cart = HashMap<String, Any>()
                            cart["item_name"] = item_name!!.toString()
                            //ADD REAL DATE
                            cart["shop_name"] = shop_name!!.toString()
                            //   cart["description"] = description.text!!.toString()
                            cart["prise"] = prise!!.toString()
                            cart["image"] = food_image.toString()
                            cart["increase"] = prise!!.toInt()
                            cart["type"] = type!!.toString()
                            cart["RID"] = strPayID
                            cart["UserID"] = "$userid"
                            cart["status"] = status!!.toString()
                            cart["total_amount"] = prise!!.toInt()
                            cart["quantity"] = "1"

                            myDB.collection("cart").document(strPayID.toString()).set(cart)
                                .addOnSuccessListener {
                                    val doc =
                                        myDB.collection("ItemInfo").document(strPayID.toString())
                                    doc.get().addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                        }
                                    }
                                }.addOnFailureListener { }
                            Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show()

                        }

                    }


                })


            } else {
                Toast.makeText(context, "INTERNET Not present", Toast.LENGTH_LONG)
                    .show()

            }

        }
        holder.buy.setOnClickListener {
            var cd = ConnectionDetector(context)
            var isInternet = false
            isInternet = cd.isConnectingToInternet
            if (isInternet) {

            val date = Date()
            val formatterDate = SimpleDateFormat("dd-MM")
            var strDate = formatterDate.format(date)
            val formatterDateTime = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            var strDateTime = formatterDateTime.format(date)
            val formatDateTime = SimpleDateFormat("HH:mm:ss")
            var dateTime = formatDateTime.format(date)
            var strPayID = strDate + dateTime
            strPayID = strPayID.replace("[^a-zA-Z0-9]".toRegex(), "").toString()
            var order_id = strPayID + userid
            val intent = Intent(context, payment_activity::class.java)
            intent.putExtra("amount", holder.prise.text.toString())
            intent.putExtra("item_name", item_name.toString())
            intent.putExtra("shop_name", shop_name.toString())
            intent.putExtra("quantity", holder.quantity.text.toString())
            intent.putExtra("image", food_image.toString())
            intent.putExtra("type", type.toString())
            intent.putExtra("flag", "1")
            intent.putExtra("id", order_id)


            context.startActivity(intent)
        }
else{
                Toast.makeText(context, "INTERNET Not present", Toast.LENGTH_LONG)
                    .show()

            }
    }
        ///////**********
        holder.food_image.setOnClickListener{
            if(!food_image.isNullOrEmpty()){
            // Initialize a new layout inflater instance
            val inflater:LayoutInflater = LayoutInflater.from(context) as LayoutInflater

            // Inflate a custom view using layout inflater
            val view = inflater.inflate(R.layout.activity_showimage,null)

            // Initialize a new instance of popup window
            val popupWindow = PopupWindow(
                view, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
            )

            // Set an elevation for the popup window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.elevation = 10.0F
            }


            // If API level 23 or higher then execute the code
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.TOP
                popupWindow.enterTransition = slideIn

                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.RIGHT
                popupWindow.exitTransition = slideOut

            }

            // Get the widgets reference from custom view
            val tv = view.findViewById<TextView>(R.id.tv)
                tv.setText("$item_name")
            val addimage = view.findViewById<ImageView>(R.id.addimage)
            val back1 = view.findViewById<LinearLayout>(R.id.back1)
            val back2 = view.findViewById<LinearLayout>(R.id.back2)
            val back3 = view.findViewById<RelativeLayout>(R.id.back3)
          //  val buttonPopup = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)


            // Set click listener for popup window's text view
            tv.setOnClickListener{
                // Change the text color of popup window's text view
            }

            if (!food_image!!.isEmpty()) {
                Picasso.get().load(food_image).into(addimage)
            }

            // Set a click listener for popup's button widget

                back1.setOnClickListener{
                // Dismiss the popup window
                popupWindow.dismiss()
            }
                back3.setOnClickListener{
                    // Dismiss the popup window
                    popupWindow.dismiss()
                }
                back2.setOnClickListener{
                // Dismiss the popup window
                popupWindow.dismiss()
            }



            // Set a dismiss listener for popup window
            popupWindow.setOnDismissListener {
          //      Toast.makeText(context,"Popup closed",Toast.LENGTH_SHORT).show()
            }
            // Finally, show the popup window on app
            popupWindow.showAtLocation(
                holder.itemView, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )
        }}

    }


    internal fun updateUi(user: String) {
        FirebaseFirestore.getInstance().collection("ItemInfo").document(user).addSnapshotListener(EventListener { documentSnapshot, e ->
            if (e != null) {
                return@EventListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {

            } else {
                Log.d("ONLINE", "Current data: null")
            }
        })
    }

    override fun getItemCount(): Int {
        return datalist.size
    }


    inner class View_Holder(view: View) : RecyclerView.ViewHolder(view) {

        var item_name: TextView
        var shop_name: TextView
        var prise: TextView
        var add_to_cart: TextView
        var quantity: TextView
    //   var type: TextView
        internal var button_minus: Button
        internal var button_plus: Button
       // internal var available: TextView
        //internal var image1: ImageView
        internal var buy: Button
        internal var food_image: ImageView

        init {
            this.item_name = view.findViewById<View>(R.id.item_data_list_item_name) as TextView
            this.shop_name = view.findViewById<View>(R.id.item_data_list_shop_name) as TextView
            this.prise = view.findViewById<View>(R.id.item_data_list_prise) as TextView
            this.button_minus = view.findViewById<View>(R.id.item_data_list_button_minus) as Button
            this.button_plus = view.findViewById<View>(R.id.item_data_list_button_plus) as Button
            this.add_to_cart = view.findViewById<View>(R.id.item_data_list_add_to_cart) as TextView
            this.quantity = view.findViewById<View>(R.id.item_data_list_quantity) as TextView
         //   this.type = view.findViewById<View>(R.id.item_data_list_type) as TextView
          //  this.available = view.findViewById<View>(R.id.item_data_list_status) as TextView
            this.food_image = view.findViewById<View>(R.id.item_data_list_food_image) as ImageView
            this.buy = view.findViewById<View>(R.id.item_data_list_buy) as Button


        }
    }
}
