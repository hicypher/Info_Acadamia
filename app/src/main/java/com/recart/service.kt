package com.recart

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore

class service : Service() {
    internal lateinit var player: MediaPlayer
    lateinit var notificationManager: NotificationManager

    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    val channelId="com.recart"
    val description="My Notification"

    override fun onBind(p0: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        object : CountDownTimer(864000000,300000) {
            override fun onFinish() {
            }
            override fun onTick(p0: Long) {
                var value=""
                var title=""
                var text=""
                var link=""
                var mydb: FirebaseFirestore
                mydb = FirebaseFirestore.getInstance()
                mydb.collection("setting").document("notification").get().addOnCompleteListener(
                    OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            value = task.result!!.get("value").toString()
                            title = task.result!!.get("title").toString()
                            text = task.result!!.get("text").toString()
                            link = task.result!!.get("link").toString()
                            //        max = task.result!!.get("maximum").toString().toDouble()
                        }
                    }).addOnCompleteListener {

                    if (value=="yes"){

                        notificationManager =
                            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        //  show.setOnClickListener {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("$link")
                        )
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
                                .setContentTitle("$title")
                                .setContentText("$text")
                                .setSmallIcon(R.mipmap.circlecroppedi)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)

                        } else {
                            builder = Notification.Builder(applicationContext)
                                .setContentTitle("$title")
                                .setContentText("$text")
                                .setSmallIcon(R.mipmap.circlecroppedi)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                        }
                        notificationManager.notify(0, builder.build())

                    }

                }

                //To change body of created functions use File | Settings | File Templates.

            }

        }.start()

        // player  = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
       //  player.isLooping=true
        // player.start()
         return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
//        player.stop()
    }
}