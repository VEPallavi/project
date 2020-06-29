package com.utalli.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.braintreepayments.api.Json
import com.google.gson.JsonObject
import com.utalli.R
import com.utalli.helpers.AppPreference
import com.utalli.helpers.Utils
import com.utalli.network.ApiClient
import com.utalli.network.ApiService
import okhttp3.internal.Util
import retrofit2.Call
import retrofit2.Response

class MyToursViewModel : ViewModel(){

    private var currentToursResult : MutableLiveData<JsonObject> ?= null
    private var upComingToursResult : MutableLiveData<JsonObject> ?= null
    private var recentToursResult : MutableLiveData<JsonObject> ?= null
    private var cancelTourResult : MutableLiveData<JsonObject> ?= null
    private var paymentClientTokenResult : MutableLiveData<JsonObject> ?= null
    private var sendPaymentNonceToServerResult : MutableLiveData<JsonObject> ?= null

    var preference: AppPreference? = null

    fun getCurrentTours(mContext : Context): MutableLiveData<JsonObject>{
        preference = AppPreference.getInstance(mContext)
        val token = preference!!.getAuthToken()
        val userId = preference!!.getId()

        currentToursResult = MutableLiveData()

        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.getCurrentTours(token, userId)

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Utils.hideProgressDialog()

                if(response!= null && response.body()!= null){
                    currentToursResult!!.value = response.body()
                } else {
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }
            }
        })
        return currentToursResult!!
    }

    fun getUpcomigTours(mContext : Context, tourSearchType : Int) : MutableLiveData<JsonObject>{

        preference = AppPreference.getInstance(mContext)
        val token = preference!!.getAuthToken()
        val userId = preference!!.getId()


        upComingToursResult = MutableLiveData()

        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.getUpcomigTours(token, userId, tourSearchType)

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Utils.hideProgressDialog()
                if(response!= null && response.body()!= null){
                    upComingToursResult!!.value = response.body()
                } else {
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }
            }
        })
        return upComingToursResult!!
    }


    fun getRecentTours(mContext : Context, tourSearchType : Int): MutableLiveData<JsonObject>{
        preference = AppPreference.getInstance(mContext)
        val token = preference!!.getAuthToken()
        val userId = preference!!.getId()

        recentToursResult = MutableLiveData()

        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.getUpcomigTours(token, userId, tourSearchType)

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Utils.hideProgressDialog()

                if(response!= null && response.body()!= null){
                    recentToursResult!!.value = response.body()
                } else {
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }
            }
        })
        return recentToursResult!!
    }


    fun cancleUpcomingTour(mContext : Context, guideId: Int, tourRequestId: Int): MutableLiveData<JsonObject>{
        preference = AppPreference.getInstance(mContext)
        val token = preference!!.getAuthToken()
        val userId = preference!!.getId()

        cancelTourResult = MutableLiveData()
        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.cancelUpcomigTour(token, guideId, tourRequestId)

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Utils.hideProgressDialog()

                if(response != null  && response.body()!= null){
                    cancelTourResult!!.value = response.body()
                }
                else {
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }
            }

        })

        return cancelTourResult!!
    }


    fun getPaymentClientToken(mContext : Context): MutableLiveData<JsonObject>{
        preference = AppPreference.getInstance(mContext)
        val token = preference!!.getAuthToken()
        val userId = preference!!.getId()

        paymentClientTokenResult = MutableLiveData()
        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.getPaymentClientToken(token, userId, "user")

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Utils.hideProgressDialog()
                if(response != null && response.body()!= null){
                    paymentClientTokenResult!!.value = response.body()
                }
            }
        })




        return paymentClientTokenResult!!
    }


    fun sendPaymentNonceToServer(mContext : Context, nonce : String,
                                 amount : String, paymentBy : String,
                                 guide_Id: Int, tourRequestId: Int, requesttype:String, customerId : String): MutableLiveData<JsonObject>{

        preference = AppPreference.getInstance(mContext)
        val token = preference!!.getAuthToken()
        val userId = preference!!.getId()

        sendPaymentNonceToServerResult = MutableLiveData()

        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.sendPaymentNonceToServer(token,nonce, amount, userId, paymentBy, guide_Id, tourRequestId, requesttype, customerId)

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Utils.hideProgressDialog()
                if(response != null && response.body()!= null){
                    sendPaymentNonceToServerResult!!.value = response.body()
                }
            }
        })

        return sendPaymentNonceToServerResult!!
    }













}