package com.recart

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.recart.Model.ItemInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_addnewitem.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.edit_profile.*
import kotlinx.android.synthetic.main.forgot_password.*
import kotlinx.android.synthetic.main.nav_header_home.*
import om.recart.RecyclerAdapters.RecyclerAdapterItemInfo
import kotlinx.android.synthetic.main.activity_addnewitem.*
import java.text.SimpleDateFormat
import java.util.*

class home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var PRIVATE_MODE=0
    private val PREF_NAME="PrefExpiry"
    internal lateinit var mydB: FirebaseFirestore
    internal  var myDB: FirebaseFirestore = FirebaseFirestore.getInstance()
    public var exit="1"

public  var update="http://www.hicypher.blogspot.com"
public  var feedback="https://docs.google.com/forms/d/e/1FAIpQLSdO2KUL6XVJi3nQMnvlngIzbasV5r84Dz0S8IndmhZelFT3_g/viewform?embedded=true"
public  var share="Download ReCart \n  http://www.hicypher.blogspot.com"
    var ifpopup=0
    var popuptext="New offer AVAILABLE!!!"
    var popuptitle="New Notification!"
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("#0000", "home ")
        super.onCreate(savedInstanceState)
        Log.d("#0000", "home1 ")
        setContentView(R.layout.home)
        Log.d("#0000", "home2 ")

        var mydb: FirebaseFirestore
        mydb = FirebaseFirestore.getInstance()
var time="link"

        mydb.collection("setting").document(time.toString()).get().addOnCompleteListener {
                task ->
            Log.d("#999", "IN")
            if (task.isSuccessful) {
                val doc=task.result
                Log.d("#999", "in reply")
                feedback= doc!!.get("feedback_link").toString()
                share= doc!!.get("share").toString()
                update= doc!!.get("update").toString()
                ifpopup= doc!!.get("popup").toString().toInt()
                popuptext= doc!!.get("popuptext").toString()
                popuptitle= doc!!.get("popuptitle").toString()
            }
        }.addOnSuccessListener {
           if (ifpopup==1&&(intent.getBooleanExtra("isappstart",false))) {  val builder: AlertDialog.Builder = AlertDialog.Builder(this@home)
            builder.setTitle(popuptitle)
            builder.setIcon(R.drawable.smalloffer)
            builder.setMessage(popuptext)
                    .setCancelable(true)
                    .setPositiveButton("Check NOW\uD83D\uDE0D",
                            DialogInterface.OnClickListener { dialog, id -> startActivity(Intent(this, activity_addnewitem::class.java).putExtra("offer",1));finish() })
         //           .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel();startActivity(    Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:8009619425")).putExtra("sms_body", "I have a problem regarding $id:")) })
            val alert: AlertDialog = builder.create()
            alert.show()}
        }
        startService(Intent(this,service::class.java))

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        Log.d("#0000", "home3 ")
        setSupportActionBar(toolbar)
        Log.d("#0000", "home4")
        val sPref: SharedPreferences =getSharedPreferences(PREF_NAME,PRIVATE_MODE)
        Log.d("#0000", "home5 ")
        var usernameas=sPref.getString("UserID","")
        var first_nameas=sPref.getString("fName","")
        var last_nameas=sPref.getString("lName","")
        Log.d("#0000", "home6 ")
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        Log.d("#0000", "home7 ")
        val navView: NavigationView = findViewById(R.id.nav_view)
        val hView = navView.getHeaderView(0)
        var name = hView.findViewById(R.id.name_user) as TextView
        var textView = hView.findViewById(R.id.userid) as TextView
        var imageView = hView.findViewById(R.id.profile_image) as ImageView
        var imguri=""
        imguri=sPref.getString("image","").toString()
        if (imguri != null) {
            Log.d("#999", "DocumentSnapshot data: " + imguri+imguri)
            if(!imguri!!.isEmpty()) {
                Picasso.get().load(imguri).into(imageView); Log.d("#999", "img document")}
            else  Log.d("#999", "No such document")

        } else {
            Log.d("#999", "No such document")
        }


        if(!imguri!!.isEmpty()) {
            Picasso.get().load(imguri).into(imageView); Log.d("#999", "img document")}
        else  Log.d("#999", "No such document")

        Log.d("#0000", "home8 ")
        textView.setText("USERNAME : $usernameas")
        name.setText("NAME  :  $first_nameas  $last_nameas")
        veg_section.setOnClickListener {
            sPref.edit().putString("type","Veg").apply()

            val i = Intent(this, activity_addnewitem::class.java)
            startActivity(i)
            finish()
        }
        offer.setOnClickListener {
            sPref.edit().putString("offer","1").apply()
            sPref.edit().putString("type","Offers").apply()
            val i = Intent(this, activity_addnewitem::class.java).putExtra("offer",1)
            startActivity(i)
            finish()
        }
        non_veg_section.setOnClickListener {
            sPref.edit().putString("type","Non Veg").apply()

            val i = Intent(this, activity_addnewitem::class.java)
            startActivity(i)
            finish()
        }
        milk_product.setOnClickListener {
            sPref.edit().putString("type","Milk Product").apply()

            val i = Intent(this, activity_addnewitem::class.java)
            startActivity(i)
            finish()
        }
        medicine.setOnClickListener {
            sPref.edit().putString("type","Medicines").apply()

            val i = Intent(this, activity_medicine::class.java)
            startActivity(i)
            finish()
        }
        Fruits.setOnClickListener {
            sPref.edit().putString("type","Fruits").apply()

            val i = Intent(this, activity_addnewitem::class.java)
            startActivity(i)
            finish()
        }
        fast_food.setOnClickListener {
            sPref.edit().putString("type","Fast Food").apply()

            val i = Intent(this, activity_addnewitem::class.java)
            startActivity(i)
            finish()
        }
        bakery.setOnClickListener {
            sPref.edit().putString("type","Bakery").apply()

            val i = Intent(this, activity_addnewitem::class.java)
            startActivity(i)
            finish()
        }
        daily_product.setOnClickListener {
            sPref.edit().putString("type","Daily Products").apply()

            val i = Intent(this, activity_addnewitem::class.java)
            startActivity(i)
            finish()
        }
        others.setOnClickListener {
            sPref.edit().putString("type","Others").apply()

            val i = Intent(this, activity_addnewitem::class.java)
            startActivity(i)
            finish()
        }
        val fab: FloatingActionButton = findViewById(R.id.fab)
        //fab.animate().rotation(180f).start()
        fab.animate().rotation(1440f).setDuration(12000).start()
        fab.setOnClickListener { view ->
           // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //    .setAction("Action", null).show()
            Toast.makeText(this, "Send your problem/feedback", Toast.LENGTH_LONG).show()            //updateUi(user_id);
            val uri = Uri.parse("smsto:8009619425")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", "")
            startActivity(intent)
        }
        Log.d("#0000", "home9")
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        Log.d("#0000", "home10 ")
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }
    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (exit=="1"){
            Toast.makeText(this, "press BACK again to exit", Toast.LENGTH_LONG).show()
            exit="2"
            }
            else{
            super.onBackPressed()
            }
        } }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        val manager=getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchitem=menu?.findItem(R.id.search)
        val searchView=searchitem?.actionView as SearchView


        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("",false)
                searchitem.collapseActionView()

                Toast.makeText(this@home, "looking for $query ", Toast.LENGTH_SHORT).show()

                var m=query
                //    m = m?.substring(0, 1)?.toUpperCase() + m?.substring(1)?.toLowerCase();

                if (m.toString()!=""&&!m!!.isEmpty()) {
                    val intent = Intent(applicationContext, search::class.java)
                    intent.putExtra("search", m.toString()).putExtra("popup",0)

                    startActivity(intent)
                    finish()

                }


                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {


                //  popupWindow.dismiss()
                // Initialize a new layout inflater instance

                //Toast.makeText(this@home, "looking for $newText ", Toast.LENGTH_SHORT).show()

                if (newText.toString()!=""&&!newText!!.isEmpty()) {
                }
                return false
            }
        })

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
         when (item.itemId) {
            R.id.referesh -> {
                var cd = ConnectionDetector(this)
                var isInternet = false
                isInternet = cd.isConnectingToInternet
                if (isInternet) {


                val i = Intent(this, home::class.java)
                startActivity(i)
                finish()
            }
                else{
                    Toast.makeText(this, "INTERNET Not present", Toast.LENGTH_LONG).show()

                }
            }
             R.id.cart -> {
                 val i = Intent(this, cart::class.java)
                 startActivity(i)
                 finish()

             }
        }
        return true }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        var mydb: FirebaseFirestore
        mydb = FirebaseFirestore.getInstance()
        var cd = ConnectionDetector(this)
        var isInternet = false
        isInternet = cd.isConnectingToInternet
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.edit_profile -> {
                val i = Intent(this, edit_profile::class.java)

                startActivity(i)
                finish()
                // Handle the camera action
            }
            R.id.wallet -> {

                val i = Intent(this, wallet::class.java)
                startActivity(i)
                finish()
            }
            R.id.new_order -> {

                val i = Intent(this, new_order::class.java)
                startActivity(i)
                finish()
            }

            R.id.order_summery -> {

                val i = Intent(this, history::class.java)
                startActivity(i)
                finish()
            }

            R.id.logout -> {
                val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
                sPref.edit().clear().apply()
                Toast.makeText(this, "LOUGOUT SUCCSSFULLY", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,login::class.java))
                finish()
            }
            R.id.notice->{
                startActivity(Intent(this,webview::class.java).putExtra("link","http://gecazamgarh.ac.in/AllNotice.aspx"))
            }
            R.id.share -> {
                val intent=Intent()
                intent.action=Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT,"$share")
                intent.type="text/plain"
                startActivity(Intent.createChooser(intent,"share by:"))

            }
            R.id.about -> {
                startActivity(Intent(this,about::class.java))
                finish()
            }
            R.id.help -> {
                startActivity(Intent(this,contact::class.java))
                finish()

            }
            R.id.feedback -> {
                startActivity(Intent(this,webview::class.java).putExtra("link",feedback))

            }
            R.id.update ->{
                var max=2.92
                var current= sPref.getString("version","2.92")!!.toDouble()
                if (isInternet) {
                    mydb.collection("setting").document("version").get().addOnCompleteListener(
                        OnCompleteListener { task ->
                            if (task.isSuccessful) {
                                max = task.result!!.get("maximum").toString().toDouble()
                            }
                        }).addOnSuccessListener {
                        if (current == max) {
                            Toast.makeText(this,"You are on Latest version $current",Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                this,
                                "New version $max available",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW, 
                                    Uri.parse("$update")
                                )
                            )
                        }
                    }
                }
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    } }
