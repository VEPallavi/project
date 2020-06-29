package com.utalli.viewModels

import android.content.Context
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

class HelpAndSupportViewModel : ViewModel(){

    private var helpAndSupportResult : MutableLiveData<JsonObject>? = null
    var preference : AppPreference?= null



    fun helpAndSupport(mContext: Context) : MutableLiveData<JsonObject>{

        preference = AppPreference.getInstance(mContext)
        val token = preference!!.getAuthToken()

        helpAndSupportResult = MutableLiveData()

        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.helpAndSupport(token)

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
               Utils.hideProgressDialog()
                if(response!= null && response.body()!= null){
                    helpAndSupportResult!!.value = response.body()

                } else {
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }


            }

        })




        return helpAndSupportResult!!
    }






}