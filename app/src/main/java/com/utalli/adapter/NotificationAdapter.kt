package com.utalli.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.utalli.R
import com.utalli.RobotoMediumTextView
import com.utalli.helpers.Utils
import com.utalli.models.Notification.NotificationResponse

class NotificationAdapter (var mContext : Context, var mNotificationList: ArrayList<NotificationResponse>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.NotificationViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_list_notification,parent,false)
        return NotificationViewHolder(view)
    }



    override fun getItemCount(): Int {
        return mNotificationList.count()
    }

    override fun onBindViewHolder(holder: NotificationAdapter.NotificationViewHolder, position: Int) {

        var notificationData: NotificationResponse = mNotificationList.get(position)
        holder.tv_notification_title.text = notificationData.getTitle()
        holder.tv_notificationTime.text = Utils.getHumanReadableTimeFromUTCString(notificationData.getNotificationTime()!!)
        holder.tv_notification_description.text = notificationData.getNotificationDetails()
        holder.bindNotificationView()
    }

    class NotificationViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var tv_notification_title: TextView
        var tv_notificationTime: TextView
        var tv_notification_description: TextView

        init {
            tv_notification_title = itemView.findViewById(R.id.tv_notification_title)
            tv_notificationTime = itemView.findViewById(R.id.tv_notificationTime)
            tv_notification_description = itemView.findViewById(R.id.tv_notification_description)
        }

        fun bindNotificationView(){

        }

    }


}