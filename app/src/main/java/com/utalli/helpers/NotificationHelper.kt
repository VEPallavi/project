package com.utalli.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.utalli.R
import com.utalli.activity.HomeActivity
import com.utalli.fragment.UpcomingToursFragment
import java.io.IOException
import java.net.URL


class NotificationHelper(var mContext: Context) {
    private var mNotificationManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null
    val NOTIFICATION_CHANNEL_ID = "10001"
    private var fragmentManager : FragmentManager ?= null
    private var yourFragment: UpcomingToursFragment? = null

    var ft : FragmentTransaction ?= null


    /**
     * Create and push the notification
     */
    fun createNotification(title: String, message: String, image: String?, notificationType: String) {
        /**Creates an explicit intent for an Activity in your app */
        var resultIntent: Intent? = null

      //  fragmentManager = mContext


           if(notificationType.equals("2")){
               Utils.showLog("FCM notification notificationHelper type userApp 2 : " + notificationType)
               resultIntent = Intent(mContext, HomeActivity::class.java)
               resultIntent.putExtra("from", "NotificationHelper")

             /*  var fragment = MyToursFragment()
               replaceFragment(fragment)*/
           }



        resultIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val rawBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app_icon)

        val resultPendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        var bmp: Bitmap? = null
        if (image != null && !image.equals("", ignoreCase = true)) {

            try {
                val `in` = URL(image).openStream()
                bmp = BitmapFactory.decodeStream(`in`)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        mBuilder = NotificationCompat.Builder(mContext)
        mBuilder!!.setSmallIcon(R.drawable.notifications_icon)
            .setLargeIcon(rawBitmap)

        mBuilder!!.setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setContentIntent(resultPendingIntent)

            .setAutoCancel(true)

        if (bmp != null) {
            mBuilder!!.setLargeIcon(bmp)
            mBuilder!!.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bmp))
        }
        mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 100, 100, 100)
            assert(mNotificationManager != null)
            mBuilder!!.setChannelId(NOTIFICATION_CHANNEL_ID)
            mNotificationManager!!.createNotificationChannel(notificationChannel)
        }
        assert(mNotificationManager != null)
        mNotificationManager!!.notify(0 /* Request Code */, mBuilder!!.build())
    }



    private fun replaceFragment(meFragment: Fragment) {
        val transaction = (mContext as AppCompatActivity).supportFragmentManager//mContext.supportFragmentManager.beginTransaction()
        ft = transaction.beginTransaction()
        ft!!.replace(R.id.frame_container, meFragment)
        ft!!.commitAllowingStateLoss();
    }

}
