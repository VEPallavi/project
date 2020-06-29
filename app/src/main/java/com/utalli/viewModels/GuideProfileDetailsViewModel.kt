package com.utalli.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.utalli.R
import com.utalli.helpers.AppPreference
import com.utalli.helpers.Utils
import com.utalli.network.ApiClient
import com.utalli.network.ApiService
import retrofit2.Call
import retrofit2.Response

class GuideProfileDetailsViewModel : ViewModel(){

    private var guideDetailsResult : MutableLiveData<JsonObject>? = null
    private var tourReqToGuideResult : MutableLiveData<JsonObject>? = null
    private var requestStatusResult : MutableLiveData<JsonObject> ?= null
    var preference : AppPreference ?= null


    fun guideDetails(mContext: Context, guidId: Int): MutableLiveData<JsonObject>{

        preference = AppPreference.getInstance(mContext)
        val token = preference!!.getAuthToken()

        guideDetailsResult = MutableLiveData()

        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.guideDetails(token,guidId)

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Utils.hideProgressDialog()

                if(response != null && response.body()!= null){
                    guideDetailsResult!!.value = response.body()
                } else{
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }
            }

        })

        return guideDetailsResult!!
    }



    fun sendTourReqToGuide(mContext: Context, guideId: Int, requesttype : Int, userId : Int
                           , tourStartDate: String, tourEndDate: String, selectedStatesId:String, poolId : String) : MutableLiveData<JsonObject>{

        preference = AppPreference.getInstance(mContext)
        val token = preference!!.getAuthToken()

        tourReqToGuideResult = MutableLiveData()
        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.sendTourReqToGuide(token,guideId,requesttype,userId, tourStartDate,tourEndDate,selectedStatesId,poolId)

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Utils.hideProgressDialog()
                if(response != null && response.body()!= null){
                    tourReqToGuideResult!!.value = response.body()
                    Log.e("TAG", "requestToGuide response  ==== ");
                }
                else {
                    Log.e("TAG", "requestToGuide response not  ==== ");
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }
            }
        })

         return tourReqToGuideResult!!
    }


    fun sendCancelRequestStatus(mContext: Context, tourRequestId: Int) : MutableLiveData<JsonObject>{

        preference = AppPreference.getInstance(mContext)
        val token = preference!!.getAuthToken()

        requestStatusResult = MutableLiveData()
        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.sendRequestStatus(token, tourRequestId)

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Utils.hideProgressDialog()
                if(response != null && response.body()!= null){
                    requestStatusResult!!.value = response.body()
                }
                else{
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }
            }
        })

        return requestStatusResult!!
    }


}