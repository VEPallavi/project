package com.utalli

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.utalli.helpers.AppPreference
import com.utalli.helpers.NotificationHelper
import com.utalli.helpers.Utils
import org.json.JSONObject
import java.lang.Exception


class MessagingService : FirebaseMessagingService() {


    var preference: AppPreference? = null


    override fun onNewToken(token: String?) {
        super.onNewToken(token)

        Utils.showLog(token!!)


    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        Utils.showLog("From: " + remoteMessage!!.getFrom())



        try {
            val params = remoteMessage.data
            val messageObject = JSONObject(params)

            Utils.showLog("FCM notification: " + messageObject.toString())
            Log.e("TAG", "<<<<<<  FCM notification ==   "+messageObject.toString())


            if (messageObject != null && messageObject.has("type")) {

                val notificationType = messageObject.optString("type")
                val title = messageObject.getString("title")
                val message = messageObject.getString("message")
                val url = messageObject.optString("image")

                if(notificationType.equals("2")){
                    var helper = NotificationHelper(this )
                    helper.createNotification(title, message, url, notificationType)
                    Utils.showLog("FCM notification notificationType messagingService userApp 2 : ")
                }

            }


        } catch (e: Exception) {
            Log.e("TAG", "<<<<<<   FCM notification Exception  ==   "+ e)
            e.printStackTrace()
        }

    }

}