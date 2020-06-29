package com.utalli.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.utalli.helpers.AppPreference
import com.utalli.helpers.Utils
import com.utalli.network.ApiClient
import com.utalli.network.ApiService
import retrofit2.Call
import retrofit2.Response

class NotificationViewModel : ViewModel() {

    var notificationResult: MutableLiveData<JsonObject>? = null

    fun getNotificationList(mContext: Context): MutableLiveData<JsonObject> {
        notificationResult = MutableLiveData()

        val authToken = AppPreference.getInstance(mContext).getAuthToken()
        val userId = AppPreference.getInstance(mContext).getId()

        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.getNOTIFICATION(authToken, userId)
        Utils.showProgressDialog(mContext)
        call.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Utils.hideProgressDialog()
                notificationResult!!.value = response.body()
            }
        })
        return notificationResult!!
    }
}