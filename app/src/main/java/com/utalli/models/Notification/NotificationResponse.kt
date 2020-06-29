package com.utalli.models.Notification

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationResponse {

    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("UserId")
    @Expose
    private var userId: Int? = null

    @SerializedName("title")
    @Expose
    private var title: String? = null

    @SerializedName("notification_details")
    @Expose
    private var notificationDetails: String? = null

    @SerializedName("notification_time")
    @Expose
    private var notificationTime: String? = null


    @SerializedName("notificationTo")
    @Expose
    private var notificationTo: Boolean? = null



    @SerializedName("createdAt")
    @Expose
    private var createdAt: Any? = null

    @SerializedName("updatedAt")
    @Expose
    private var updatedAt: Any? = null


    fun getId(): Int? {
        return id
    }
    fun setId(id: Int?) {
        this.id = id
    }




    fun getUserId(): Int? {
        return userId
    }

    fun setUserId(userId: Int?) {
        this.userId = userId
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun getNotificationDetails(): String? {
        return notificationDetails
    }

    fun setNotificationDetails(notificationDetails: String) {
        this.notificationDetails = notificationDetails
    }

    fun getNotificationTime(): String? {
        return notificationTime
    }

    fun setNotificationTime(notificationTime: String) {
        this.notificationTime = notificationTime
    }




    fun getNotificationTo(): Boolean? {
        return notificationTo
    }

    fun setNotificationTo(notificationTo: Boolean) {
        this.notificationTo = notificationTo
    }


    fun getCreatedAt(): Any? {
        return createdAt
    }

    fun setCreatedAt(createdAt: Any) {
        this.createdAt = createdAt
    }

    fun getUpdatedAt(): Any? {
        return updatedAt
    }

    fun setUpdatedAt(updatedAt: Any) {
        this.updatedAt = updatedAt
    }
}