package com.utalli.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GuideStateListModel() : Parcelable {
    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("guidID")
    @Expose
    private var guidID: Int? = null

    @SerializedName("countryId")
    @Expose
    private var countryId: Int? = null

    @SerializedName("countryCode")
    @Expose
    private var countryCode: String? = null

    @SerializedName("country_name")
    @Expose
    private var countryName: String? = null

    @SerializedName("stateId")
    @Expose
    private var stateId: Int? = null

    @SerializedName("statename")
    @Expose
    private var statename: String? = null

    @SerializedName("cityId")
    @Expose
    private var cityId: Int? = null

    @SerializedName("cityName")
    @Expose
    private var cityName: String? = null


    @SerializedName("latitude")
    @Expose
    private var latitude: String? = null

    @SerializedName("longitude")
    @Expose
    private var longitude: String? = null

    @SerializedName("createdAt")
    @Expose
    private var createdAt: Any? = null

    @SerializedName("updatedAt")
    @Expose
    private var updatedAt: Any? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        guidID = parcel.readValue(Int::class.java.classLoader) as? Int
        countryId = parcel.readValue(Int::class.java.classLoader) as? Int
        countryCode = parcel.readString()
        countryName = parcel.readString()
        stateId = parcel.readValue(Int::class.java.classLoader) as? Int
        statename = parcel.readString()
        cityId = parcel.readValue(Int::class.java.classLoader) as? Int
        cityName = parcel.readString()
        latitude = parcel.readString()
        longitude = parcel.readString()
    }

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getGuidID(): Int? {
        return guidID
    }

    fun setGuidID(guidID: Int?) {
        this.guidID = guidID
    }

    fun getCountryId(): Int? {
        return countryId
    }

    fun setCountryId(countryId: Int?) {
        this.countryId = countryId
    }

    fun getCountryCode(): String? {
        return countryCode
    }

    fun setCountryCode(countryCode: String) {
        this.countryCode = countryCode
    }

    fun getCountryName(): String? {
        return countryName
    }

    fun setCountryName(countryName: String) {
        this.countryName = countryName
    }

    fun getStateId(): Int? {
        return stateId
    }

    fun setStateId(stateId: Int?) {
        this.stateId = stateId
    }

    fun getStatename(): String? {
        return statename
    }

    fun setStatename(statename: String) {
        this.statename = statename
    }


    fun getCityId(): Int? {
        return cityId
    }

    fun setCityId(cityId: Int?) {
        this.cityId = cityId
    }

    fun getCityname(): String? {
        return cityName
    }

    fun setCityname(cityName: String) {
        this.cityName = cityName
    }



    fun getLatitude(): String? {
        return latitude
    }

    fun setLatitude(latitude: String) {
        this.latitude = latitude
    }

    fun getLongitude(): String? {
        return longitude
    }

    fun setLongitude(longitude: String) {
        this.longitude = longitude
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(guidID)
        parcel.writeValue(countryId)
        parcel.writeString(countryCode)
        parcel.writeString(countryName)
        parcel.writeValue(stateId)
        parcel.writeString(statename)
        parcel.writeValue(cityId)
        parcel.writeString(cityName)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GuideStateListModel> {
        override fun createFromParcel(parcel: Parcel): GuideStateListModel {
            return GuideStateListModel(parcel)
        }

        override fun newArray(size: Int): Array<GuideStateListModel?> {
            return arrayOfNulls(size)
        }
    }

}