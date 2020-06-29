package com.utalli.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.utalli.helpers.Utils
import com.utalli.network.ApiClient
import com.utalli.network.ApiService
import retrofit2.Call
import retrofit2.Response

class ForgetPassViewModel : ViewModel() {

    private var forgetPassResult: MutableLiveData<JsonObject>? = null



    fun forgetPass(mContext: Context, mobile_no: String) : MutableLiveData<JsonObject>{
        forgetPassResult = MutableLiveData()

        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.forgotPassword(mobile_no)

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject>{

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
               Utils.hideProgressDialog()
                forgetPassResult!!.value = response.body()
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

        })


      return forgetPassResult!!
    }




}