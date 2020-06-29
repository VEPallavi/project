package com.utalli.models.Dashboard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import android.R.attr.name
import android.R.id



class GuidListData {
    @SerializedName("id")
    @Expose
    private var id: Int? = null
    @SerializedName("name")
    @Expose
    private var name: String? = null
    @SerializedName("gender")
    @Expose
    private var gender: String? = null
    @SerializedName("lang")
    @Expose
    private var lang: String? = null
    @SerializedName("guid_profile_img")
    @Expose
    private var guidProfileImg: String? = null
    @SerializedName("guide_private_price")
    @Expose
    private var guidePrivatePrice: Int? = null
    @SerializedName("guide_pool_price")
    @Expose
    private var guidePoolPrice: Int? = null
    @SerializedName("guiderating")
    @Expose
    private var guiderating: Int? = null
    @SerializedName("booking_status")
    @Expose
    private var bookingStatus: Int? = null
    @SerializedName("distance")
    @Expose
    private var distance: Int? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getGender(): String? {
        return gender
    }

    fun setGender(gender: String) {
        this.gender = gender
    }

    fun getLang(): String? {
        return lang
    }

    fun setLang(lang: String) {
        this.lang = lang
    }

    fun getGuidProfileImg(): String? {
        return guidProfileImg
    }

    fun setGuidProfileImg(guidProfileImg: String) {
        this.guidProfileImg = guidProfileImg
    }

    fun getGuidePrivatePrice(): Int? {
        return guidePrivatePrice
    }

    fun setGuidePrivatePrice(guidePrivatePrice: Int?) {
        this.guidePrivatePrice = guidePrivatePrice
    }

    fun getGuidePoolPrice(): Int? {
        return guidePoolPrice
    }

    fun setGuidePoolPrice(guidePoolPrice: Int?) {
        this.guidePoolPrice = guidePoolPrice
    }

    fun getGuiderating(): Int? {
        return guiderating
    }

    fun setGuiderating(guiderating: Int?) {
        this.guiderating = guiderating
    }

    fun getBookingStatus(): Int? {
        return bookingStatus
    }

    fun setBookingStatus(bookingStatus: Int?) {
        this.bookingStatus = bookingStatus
    }

    fun getDistance(): Int? {
        return distance
    }

    fun setDistance(distance: Int?) {
        this.distance = distance
    }
}