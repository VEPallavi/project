package com.utalli.viewModels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.utalli.R

import com.utalli.helpers.AppPreference
import com.utalli.helpers.Utils
import com.utalli.models.UpdateProfileRequestModel
import com.utalli.network.ApiClient
import com.utalli.network.ApiService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response


class MyProfileViewModel : ViewModel() {

    private var myprofileResult: MutableLiveData<JsonObject>? = null
    private var profilePicResult: MutableLiveData<JsonObject>? = null
    private var updateProfileResult: MutableLiveData<JsonObject>? = null
    var preference: AppPreference? = null


    fun myProfileUser(mContext: Context, id: Int): MutableLiveData<JsonObject> {
        myprofileResult = MutableLiveData()

        preference = AppPreference.getInstance(mContext)
        val token = preference!!.getAuthToken()

        var apiService = ApiClient.getClient().create(ApiService::class.java)
        var call = apiService.myProfileUser(token, id)
        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Utils.hideProgressDialog()

                if (response != null && response.body() != null) {
                    myprofileResult!!.value = response.body()
                } else {
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }


            }


        })

        return myprofileResult!!

    }


    fun updateProfilePic(mContext: Context, pic: MultipartBody.Part): MutableLiveData<JsonObject> {
        preference = AppPreference.getInstance(mContext)
        val userModel = preference!!.getUserData()


        profilePicResult = MutableLiveData()


        var apiService = ApiClient.getClient().create(ApiService::class.java)

        var id = RequestBody.create(MediaType.parse("multipart/form-data"), "" + preference!!.getId() as Int)


        var call = apiService.updateProfilePic(id, pic)

       // Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
             //   Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
           //     Utils.hideProgressDialog()
                if (response != null && response.body() != null) {
                    profilePicResult!!.value = response.body()
                }
                else {
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }
            }
        })


        return profilePicResult!!
    }


    fun updateProfile(mContext: Context, updateProfileRequestMode: UpdateProfileRequestModel): MutableLiveData<JsonObject> {
        preference = AppPreference.getInstance(mContext)
        val token = preference!!.getAuthToken()


        updateProfileResult = MutableLiveData()
        var apiService = ApiClient.getClient().create(ApiService::class.java)

        var call = apiService.updateProfile(
            token,
            updateProfileRequestMode.name,
            updateProfileRequestMode.email,
            updateProfileRequestMode.contactno,
            updateProfileRequestMode.dob,
            updateProfileRequestMode.gender,
            updateProfileRequestMode.payment,
            updateProfileRequestMode.emry_contact,
            updateProfileRequestMode.u_address,
            updateProfileRequestMode.id
        )

        Utils.showProgressDialog(mContext)

        call.enqueue(object : retrofit2.Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Utils.hideProgressDialog()
                Utils.showLog(t.message!!)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Utils.hideProgressDialog()

                if (response != null && response.body() != null) {
                    updateProfileResult!!.value = response.body()
                } else {
                    Utils.showToast(mContext, mContext.resources.getString(R.string.msg_common_error))
                }
            }

        })


        return updateProfileResult!!
    }


}