package com.utalli.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class StateListData {

    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("name")
    @Expose
    private var name: String? = null

    @SerializedName("country_id")
    @Expose
    private var countryId: Int? = null

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

    fun getCountryId(): Int? {
        return countryId
    }

    fun setCountryId(countryId: Int?) {
        this.countryId = countryId
    }
}