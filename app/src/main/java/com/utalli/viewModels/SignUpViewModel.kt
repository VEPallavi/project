package com.utalli.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.utalli.R
import com.utalli.helpers.Utils
import com.utalli.models.SignupRequestModel
import com.utalli.network.ApiClient
import com.utalli.network.ApiService
import okhttp3.internal.Util
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class SignUpViewModel : ViewModel() {
    private var signupResult : MutableLiveData<JsonObject>?= null
    private var verifyOTPResult: MutableLiveData<JsonObject>? = null



    public fun signupUser(mContext: Context, signupRequestModel: SignupRequestModel) : MutableLiveData<JsonObject>{
        if (signupResult == null) {
            signupResult = MutableLiveData()
        }

        var apiService = ApiClient.getClient().create(ApiService::class.java)

        var call = apiService.signupUser(
            signupRequestModel.name,
            signupRequestModel.email,
            signupRequestModel.mobile_no,
            signupRequestModel.password,
            signupRequestModel.dob,
            signupRequestModel.gender,
            signupRequestModel.otp,
            signupRequestModel.device_token
        )

        Utils.showProgressDialog(mContext)

        call.enqueue(object:retrofit2.Callback<JsonObject>{

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
              Utils.hideProgressDialog()

                if(response!=null && response.body()!=null){
                    signupResult!!.value = response.body()
                }else {
                   Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

        })


      return signupResult!!
    }



    fun verifyOTP(mContext: Context, signupRequestModel: SignupRequestModel): MutableLiveData<JsonObject> {

        if (verifyOTPResult == null)
            verifyOTPResult = MutableLiveData()

        var apiService = ApiClient.getClient().create(ApiService::class.java)

        var call = apiService.verifySignupUser(
            signupRequestModel.name,
            signupRequestModel.email,
            signupRequestModel.mobile_no,
            signupRequestModel.password,
            signupRequestModel.dob,
            signupRequestModel.gender,
            signupRequestModel.otp,
            signupRequestModel.device_token
        )
        Utils.showProgressDialog(mContext)


        call.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
               Utils.hideProgressDialog()

                if (response != null && response.body() != null){
                    verifyOTPResult!!.value = response.body()
                }
                else{
                    Log.e("TAG","Result Obtained verifyOtp ==="+response.body())
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }
            }

        })

        return verifyOTPResult!!
    }



}