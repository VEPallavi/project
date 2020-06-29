package com.utalli.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.utalli.R
import com.utalli.helpers.Utils
import com.utalli.network.ApiClient
import com.utalli.network.ApiService
import retrofit2.Call
import retrofit2.Response

class HomeViewModel : ViewModel() {
    var updateTokenResult: MutableLiveData<JsonObject>? = null

    fun updateDeviceToken(authToken: String, userId: String, deviceToken: String, firebaseChatId: String): MutableLiveData<JsonObject> {
        updateTokenResult = MutableLiveData()


        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.updateDeviceToken(authToken, userId.toInt(), deviceToken, firebaseChatId)

        call.enqueue(object : retrofit2.Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                //Utils.hideProgressDialog()
                //Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                //Utils.hideProgressDialog()
                updateTokenResult!!.value = response.body()
            }
        })

        return updateTokenResult!!


    }


}