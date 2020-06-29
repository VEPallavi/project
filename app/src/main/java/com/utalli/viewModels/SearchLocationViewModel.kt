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

class SearchLocationViewModel : ViewModel(){

    private var searchLocationResult: MutableLiveData<JsonObject>? = null


    fun searchLocation(mContext: Context, countryname:String): MutableLiveData<JsonObject>{
      //  if (searchLocationResult == null) {
            searchLocationResult = MutableLiveData()
      //  }

        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.searchLocation(countryname)


        call.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response !=null && response.body() !=null){
                    searchLocationResult!!.value = response.body()
                }else{
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }
            }

        })


    return searchLocationResult!!

    }


}