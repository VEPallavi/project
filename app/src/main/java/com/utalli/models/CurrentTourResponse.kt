package com.utalli.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CurrentTourResponse {
    @SerializedName("id")
    @Expose
    private var id: Int? = null

    @SerializedName("guideId")
    @Expose
    private var guideId: Int? = null





    @SerializedName("UserId")
    @Expose
    private var userId: Int? = null
    @SerializedName("tourReqId")
    @Expose
    private var tourReqId: Int? = null

    @SerializedName("startdate")
    @Expose
    private var startdate: String? = null

    @SerializedName("enddate")
    @Expose
    private var enddate: String? = null

    @SerializedName("requesttype")
    @Expose
    private var requesttype: String? = null

    @SerializedName("tourPrice")
    @Expose
    private var tourPrice: String? = null

    @SerializedName("tourdays")
    @Expose
    private var tourdays: Int? = null
    @SerializedName("tourStatus")
    @Expose
    private var tourStatus: Int? = null
    @SerializedName("tourdates")
    @Expose
    private var tourdates: String? = null
    @SerializedName("tour_mem_id")
    @Expose
    private var tourMemId: String? = null
    @SerializedName("TourNotes")
    @Expose
    private var tourNotes: String? = null
    @SerializedName("createdAt")
    @Expose
    private var createdAt: String? = null
    @SerializedName("updatedAt")
    @Expose
    private var updatedAt: String? = null
    @SerializedName("guide_info")
    @Expose
    private var guideInfo: GuideInfo? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?) {
        this.id = id
    }






    fun getGuideId(): Int? {
        return guideId
    }

    fun setGuideId(guideId: Int?) {
        this.guideId = guideId
    }

    fun getUserId(): Int? {
        return userId
    }

    fun setUserId(userId: Int?) {
        this.userId = userId
    }

    fun getTourReqId(): Int? {
        return tourReqId
    }

    fun setTourReqId(tourReqId: Int?) {
        this.tourReqId = tourReqId
    }

    fun getStartdate(): String? {
        return startdate
    }

    fun setStartdate(startdate: String) {
        this.startdate = startdate
    }

    fun getEnddate(): String? {
        return enddate
    }

    fun setEnddate(enddate: String) {
        this.enddate = enddate
    }

    fun getRequesttype(): String? {
        return requesttype
    }

    fun setRequesttype(requesttype: String?) {
        this.requesttype = requesttype
    }

    fun getTourPrice(): String? {
        return tourPrice
    }

    fun setTourPrice(tourPrice: String) {
        this.tourPrice = tourPrice
    }

    fun getTourdays(): Int? {
        return tourdays
    }

    fun setTourdays(tourdays: Int?) {
        this.tourdays = tourdays
    }

    fun getTourStatus(): Int? {
        return tourStatus
    }

    fun setTourStatus(tourStatus: Int?) {
        this.tourStatus = tourStatus
    }

    fun getTourdates(): String? {
        return tourdates
    }

    fun setTourdates(tourdates: String) {
        this.tourdates = tourdates
    }

    fun getTourMemId(): String? {
        return tourMemId
    }

    fun setTourMemId(tourMemId: String) {
        this.tourMemId = tourMemId
    }

    fun getTourNotes(): String? {
        return tourNotes
    }

    fun setTourNotes(tourNotes: String) {
        this.tourNotes = tourNotes
    }

    fun getCreatedAt(): String? {
        return createdAt
    }

    fun setCreatedAt(createdAt: String) {
        this.createdAt = createdAt
    }

    fun getUpdatedAt(): String? {
        return updatedAt
    }

    fun setUpdatedAt(updatedAt: String) {
        this.updatedAt = updatedAt
    }

    fun getGuideInfo(): GuideInfo? {
        return guideInfo
    }

    fun setGuideInfo(guideInfo: GuideInfo) {
        this.guideInfo = guideInfo
    }
}

class GuideInfo {
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
    private var guid_profile_img: String?= null


    @SerializedName("firebaseChatId")
    @Expose
    private var firebaseChatId: String? = null


    fun setfirebaseChatId(firebaseChatId: String?) {
        this.firebaseChatId = firebaseChatId
    }
    fun getfirebaseChatId(): String? {
        return firebaseChatId
    }


    fun getGuide_profile_img(): String? {
        return guid_profile_img
    }

    fun setGuide_profile_img(guid_profile_img: String) {
        this.guid_profile_img = guid_profile_img
    }

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
}