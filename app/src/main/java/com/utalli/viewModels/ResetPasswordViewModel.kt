package com.utalli.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.utalli.R
import com.utalli.helpers.Utils
import com.utalli.network.ApiClient
import com.utalli.network.ApiService
import retrofit2.Call
import retrofit2.Response

class ResetPasswordViewModel : ViewModel(){

    private var resetPasswordResult: MutableLiveData<JsonObject>? = null



    fun resetPassword(mContext: Context,  password: String, otp: String, id:Int) : MutableLiveData<JsonObject>{

        resetPasswordResult = MutableLiveData()

        var apiService = ApiClient.getClient().create(ApiService::class.java)

        var call = apiService.resetPassword(password,otp,id)

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Utils.hideProgressDialog()
                resetPasswordResult!!.value = response.body()
            }

        })


        return resetPasswordResult!!

    }




}