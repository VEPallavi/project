package com.utalli.models.Dashboard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import android.R.id



class PopulardestinationList {
    @SerializedName("id")
    @Expose
    private var id: Int? = null
    @SerializedName("locationName")
    @Expose
    private var locationName: String? = null
    @SerializedName("TripImage")
    @Expose
    private var tripImage: String? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }

    fun getLocationName(): String? {
        return locationName
    }

    fun setLocationName(locationName: String) {
        this.locationName = locationName
    }

    fun getTripImage(): String? {
        return tripImage
    }

    fun setTripImage(tripImage: String) {
        this.tripImage = tripImage
    }
}