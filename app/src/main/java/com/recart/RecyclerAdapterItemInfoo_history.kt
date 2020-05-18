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
class RecyclerAdapterItemInfoo_history(internal var datalist: List<ItemInfo>, internal var context: Context) : RecyclerView.Adapter<RecyclerAdapterItemInfoo_history.View_Holder>() {
    internal  var myDB: FirebaseFirestore=FirebaseFirestore.getInstance()
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item_data_list, parent, false)
        return View_Holder(view)
    }

    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        Log.d("Query", "rec")

        val item_name = datalist[position].item_name
        val quantity = datalist[position].quantity
        val shop_name = datalist[position].shop_name
        val prise = datalist[position].prise
        var status=datalist[position].status
        var food_image=datalist[position].food_image
        var order_date=datalist[position].order_date
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



        Log.d("#888", "jhjhv")

        holder.quantity.text=quantity
        holder.item_name.text = item_name
        holder.shop_name.text = shop_name
        holder.prise.text =prise
        holder.status.text =status
        holder.order_date.text =order_date
       // holder.type.text = type

        Log.d("#0000", status)


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
        var quantity: TextView
        internal var status: TextView
        internal var order_date: TextView
        internal var food_image: ImageView

        init {
            this.item_name = view.findViewById<View>(R.id.item_data_list_item_name_history) as TextView
            this.shop_name = view.findViewById<View>(R.id.item_data_list_shop_name_history) as TextView
            this.prise = view.findViewById<View>(R.id.item_data_list_prise_history) as TextView
            this.quantity = view.findViewById<View>(R.id.item_data_list_quantity_history) as TextView
            this.food_image = view.findViewById<View>(R.id.item_data_list_food_image_history) as ImageView
            this.status = view.findViewById<View>(R.id.item_data_list_status_history) as TextView
            this.order_date = view.findViewById<View>(R.id.date_order) as TextView


        }
    }
}
