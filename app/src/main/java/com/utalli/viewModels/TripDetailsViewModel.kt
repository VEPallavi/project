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

class TripDetailsViewModel : ViewModel(){

    private var getCityResult: MutableLiveData<JsonObject>? = null
    private var getStateResult: MutableLiveData<JsonObject>? = null
    var preference: AppPreference? = null



    fun getCityData(mContext: Context, countryid:Int, stateId: Int) : MutableLiveData<JsonObject>{

        preference = AppPreference.getInstance(mContext)
        val token = preference!!.getAuthToken()

        getCityResult = MutableLiveData()

        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.getCities(token,countryid, stateId)

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
              //  Utils.hideProgressDialog()
                if(response !=null && response.body() !=null){
                    getCityResult!!.value = response.body()
                }else{
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }
            }
        })

      return  getCityResult!!
    }



    fun getStateData(mContext: Context, contryid:Int): MutableLiveData<JsonObject>{
        preference = AppPreference.getInstance(mContext)
        val token = preference!!.getAuthToken()

        getStateResult = MutableLiveData()

        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.getStateByCountry(token,contryid)

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Utils.hideProgressDialog()
                if(response !=null && response.body() !=null){
                    getStateResult!!.value = response.body()
                }else{
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }
            }
        })

        return  getStateResult!!


    }




}