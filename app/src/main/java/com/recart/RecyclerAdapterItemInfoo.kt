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
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import com.recart.*
import java.text.SimpleDateFormat
import java.util.*

//import com.recart.product_image
//import com.recart.update_item

//import com.hacktech19.product_image
//import com.hacktech19.update_item
//import com.hacktech19.home
class RecyclerAdapterItemInfoo(internal var datalist: List<ItemInfo>, internal var context: Context) : RecyclerView.Adapter<RecyclerAdapterItemInfoo.View_Holder>() {
    internal  var myDB: FirebaseFirestore=FirebaseFirestore.getInstance()
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_data_list_cart, parent, false)
        return View_Holder(view)
    }

    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        Log.d("Query", "rec")

        val item_name = datalist[position].item_name
        val shop_name = datalist[position].shop_name
        val prise = datalist[position].prise
        var userid=datalist[position].userid
        var type=datalist[position].type
        var status=datalist[position].status
        var food_image=datalist[position].food_image
        if (food_image != null) {
            Log.d("#999", "DocumentSnapshot data: " + food_image)
            if(!food_image!!.isEmpty()) {
                Picasso.get().load(food_image).into(holder.food_image); Log.d("#999", "img document")}
            else  Log.d("#999", "No such document")

        } else {
            Log.d("#999", "No such document")
        }


        if(!food_image!!.isEmpty()) {
            Picasso.get().load(food_image).into(holder.food_image); Log.d("#999", "img document")}
        Log.d("Query",item_name+shop_name+prise)
      //  val ExpiryDate = datalist[position].item_expirydate
     //   val PublishDate = datalist[position].item_publishdate
    //    val Type = datalist[position].item_type
      //  val user_id = datalist[position].item_user_id
     //   val image1 = datalist[position].image1
//        val food_image = datalist[position].food_image
        var id=item_name.toString()+shop_name.toString()
        val RID=datalist[position].RID
        var prise1=prise.toString().toInt()
        var increase= datalist[position].increase?.toInt()

        var temp= increase!!.toInt()/prise1.toInt()
        holder.quantity.text=temp.toString()
        holder.item_name.text = item_name
        holder.shop_name.text = shop_name
        holder.prise.text = (prise!!.toInt()*temp).toString()
       // holder.type.text = type

        Log.d("#0000", status)
if (status=="1") {

    //   holder.available.text = "Available:YES"
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
            prise1 = holder.prise.text.toString().toInt()

            var quantity1 = holder.quantity.text.toString().toInt() - 1
            holder.quantity.text = quantity1.toString()
            prise1 = prise1 - prise.toString().toInt()
            holder.prise.text = "$prise1"
            var strPayID = userid + "_" + item_name.toString() + "_" + shop_name.toString()

            if (prise != null) {
                var mydb: FirebaseFirestore
                mydb = FirebaseFirestore.getInstance()


                mydb.collection("cart").document(strPayID.toString()).update(
                    "increase",
                    prise1,
                    "total_amount",
                    holder.prise.text.toString(),
                    "quantity",
                    holder.quantity.text.toString()
                )
                    .addOnSuccessListener {
                        //  Toast.makeText(context, "value changed", Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener { e ->
                        Log.w("111", "Error adding document", e)
                        Toast.makeText(context, "not changed ", Toast.LENGTH_SHORT).show()
                    }


            }


        }
    }
    else{
            Toast.makeText(context, "INTERNET Not present", Toast.LENGTH_LONG)
                .show()

        }


    }
    holder.button_plus.setOnClickListener {
        var cd = ConnectionDetector(context)
        var isInternet = false
        isInternet = cd.isConnectingToInternet
        if (isInternet) {

        prise1 = holder.prise.text.toString().toInt()


        var quantity1 = holder.quantity.text.toString().toInt() + 1
        holder.quantity.text = quantity1.toString()
        prise1 = prise1 + prise.toString().toInt()
        holder.prise.text = "$prise1"
        if (prise != null) {
            var strPayID = userid + "_" + item_name.toString() + "_" + shop_name.toString()


            var mydb: FirebaseFirestore
            mydb = FirebaseFirestore.getInstance()


            mydb.collection("cart").document(strPayID.toString()).update(
                "increase",
                prise1,
                "total_amount",
                holder.prise.text.toString(),
                "quantity",
                holder.quantity.text.toString()

            )
                .addOnSuccessListener {
                    //  Toast.makeText(context, "value changed", Toast.LENGTH_SHORT).show()

                }.addOnFailureListener { e ->
                    Log.w("111", "Error adding document", e)
                    Toast.makeText(context, "not changed ", Toast.LENGTH_SHORT).show()
                }

        }
    }
        else{
            Toast.makeText(context, "INTERNET Not present", Toast.LENGTH_LONG)
                .show()

        }
    }
    holder.add_to_cart.setOnClickListener {
        var cd = ConnectionDetector(context)
        var isInternet = false
        isInternet = cd.isConnectingToInternet
        if (isInternet) {

            Toast.makeText(context, "Removing From Cart...", Toast.LENGTH_SHORT).show()
            var strPayID = userid + "_" + item_name.toString() + "_" + shop_name.toString()
            FirebaseFirestore.getInstance().collection("cart").document(strPayID!!).delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Removed From Cart", Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context, cart::class.java))

                        //updateUi(user_id);
                    } else {
                        Toast.makeText(context, "Faild To Removed From Cart", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
        else{
            Toast.makeText(context, "INTERNET Not present", Toast.LENGTH_LONG)
                .show()

        }

    }

    holder.buy.setOnClickListener {
        var cd = ConnectionDetector(context)
        var isInternet = false
        isInternet = cd.isConnectingToInternet
        if (isInternet) {

        var v = holder.prise.text.toString()
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
        var type = datalist[position].type
        val intent = Intent(context, payment_activity::class.java)
        intent.putExtra("id", order_id)
        intent.putExtra("amount", holder.prise.text.toString())
        intent.putExtra("item_name", item_name.toString())
        intent.putExtra("shop_name", shop_name.toString())
        intent.putExtra("quantity", holder.quantity.text.toString())
        intent.putExtra("image", food_image.toString())
        intent.putExtra("type", type.toString())
        intent.putExtra("flag", "1")
        context.startActivity(intent)

    }
        else
        {
            Toast.makeText(context, "INTERNET Not present", Toast.LENGTH_LONG)
                .show()

        }
    }

}
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
     //   internal var available: TextView
        //internal var image1: ImageView
        internal var buy: Button
        internal var food_image: ImageView

        init {
            this.item_name = view.findViewById<View>(R.id.item_data_list_item_name_cart) as TextView
            this.shop_name = view.findViewById<View>(R.id.item_data_list_shop_name_cart) as TextView
            this.prise = view.findViewById<View>(R.id.item_data_list_prise_cart) as TextView
            this.button_minus = view.findViewById<View>(R.id.item_data_list_button_minus_cart) as Button
            this.button_plus = view.findViewById<View>(R.id.item_data_list_button_plus_cart) as Button
            this.add_to_cart = view.findViewById<View>(R.id.item_data_list_delete) as TextView
            this.quantity = view.findViewById<View>(R.id.item_data_list_quantity_cart) as TextView
            //this.type = view.findViewById<View>(R.id.item_data_list_type_cart) as TextView
         //   this.available = view.findViewById<View>(R.id.item_data_list_status_cart) as TextView
            this.food_image = view.findViewById<View>(R.id.item_data_list_food_image_cart) as ImageView
            this.buy = view.findViewById<View>(R.id.item_data_list_cart_buy) as Button


        }
    }
}
