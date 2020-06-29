package com.utalli.network

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {


    @POST(ApiList.SIGNUP_URL)
    @FormUrlEncoded
    fun signupUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("mobile_no") mobile_no: String,
        @Field("password") password: String,
        @Field("dob") dob: String,
        @Field("gender") gender: String,
        @Field("otp") otp: String,
        @Field("device_token") device_token: String
    ): Call<JsonObject>

    @POST(ApiList.VERIFY_OTP_URL)
    @FormUrlEncoded
    fun verifySignupUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("mobile_no") mobile_no: String,
        @Field("password") password: String,
        @Field("dob") dob: String,
        @Field("gender") gender: String,
        @Field("otp") otp: String,
        @Field("device_token") device_token: String
    ): Call<JsonObject>


    @POST(ApiList.UPDATE_PROFILE_URL)
    @FormUrlEncoded
    fun updateProfile(
        @Header("x-access-token") token: String,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("contactno") mobile_no: String,
        @Field("dob") password: String,
        @Field("gender") dob: String,
        @Field("payment") gender: String,
        @Field("emry_contact") otp: String,
        @Field("u_address") device_token: String,
        @Field("id") id: Int
    ): Call<JsonObject>


    @POST(ApiList.LOGIN_URL) // + "{id}"
    @FormUrlEncoded
    fun loginUser(
        /* @Path ("id") id : String,*/
        @Field("mobile_no") mobile_no: String,
        @Field("password") password: String,
        @Field("device_token") device_token: String
    ): Call<JsonObject>


    @POST(ApiList.FORGOT_PASS_URL)
    @FormUrlEncoded
    fun forgotPassword(
        @Field("mobile_no") mobile_no: String
    ): Call<JsonObject>


    @POST(ApiList.RESET_PASS_URL)
    @FormUrlEncoded
    fun resetPassword(
        @Field("password") password: String,
        @Field("otp") otp: String,
        @Field("id") id: Int
    ): Call<JsonObject>


    @POST(ApiList.UPDATE_PASS_URL)
    @FormUrlEncoded
    fun updatePassword(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<JsonObject>


    /*  @POST(ApiList.UPDATE_PROFILE_URL)
      @FormUrlEncoded
      fun updateProfile(
          @Header("x-auth") token: String, @FieldMap params: HashMap<String, String>
      ): Call<JsonObject>*/

/*    @Multipart
    @POST(ApiList.UPDATE_PROFILE_PIC_URL)
    fun updateProfilePic(
        @Header("x-auth") token: String,
        @Part("user_id") userId: RequestBody,
        @Part profilePic: MultipartBody.Part
    ): Call<JsonObject>*/


    @GET(ApiList.GET_CREDENTIALS_URL)
    fun getCredentials(): Call<JsonObject>


    @POST(ApiList.SENT_PATIENT_REFERAL_URL)
    @FormUrlEncoded
    fun submitPatientReferal(
        @Header("x-auth") token: String,
        @Field("patient_name") patient_name: String,
        @Field("facility_name") facility_name: String,
        @Field("room_number") room_number: String,
        @Field("reason") reason: String,
        @Field("user_id") user_id: String

    ): Call<JsonObject>


    @POST(ApiList.GET_PROVIDERS_URL)
    @FormUrlEncoded
    fun getProviderList(
        @Header("x-auth") token: String,
        @Field("provider_status") provider_status: String
    ): Call<JsonObject>

    @GET(ApiList.GET_VIDEOS_URL)
    fun getVideosList(
        @Header("x-auth") token: String
    ): Call<JsonObject>

    @POST(ApiList.INVITE_VIDEO_CALLING_URL)
    @FormUrlEncoded
    fun inviteUser(
        @Header("x-auth") token: String,
        @Field("callee_id") calleeId: String,
        @Field("alert_message") alert_message: String,
        @Field("title") title: String
    ): Call<JsonObject>

    @POST(ApiList.REJECT_VIDEO_CALLING_URL)
    @FormUrlEncoded
    fun rejectVideoCall(
        @Header("x-auth") token: String,
        @Field("callee_id") calleeId: String,
        @Field("alert_message") alert_message: String,
        @Field("title") title: String
    ): Call<JsonObject>

    @POST(ApiList.GET_NOTIFICATIONS_URL)
    @FormUrlEncoded
    fun getNotifications(
        @Header("x-auth") token: String,
        @Field("user_id") user_id: String

    ): Call<JsonObject>

    @POST(ApiList.SEND_NOTIFICATION_URL)
    @FormUrlEncoded
    fun sendNotification(
        @Field("token") token: String,
        @Field("title") title: String,
        @Field("message") message: String

    ): Call<JsonObject>


    /*   @GET(ApiList.SEND_NOTIFICATION_URL + "{id}" )
       fun sendNotification(
           @Path("id") id: Int,
           @Header ("x-auth-token") token : String
       ): Call<JsonObject>*/


    @POST(ApiList.SEARCH_LOCATION)
    @FormUrlEncoded
    fun searchLocation(
        @Field("countryname") countryname: String
    ): Call<JsonObject>


    @POST(ApiList.GET_STATE_BY_COUNTRY)
    @FormUrlEncoded
    fun getStateByCountry(
        @Header("x-access-token") token: String,
        @Field("contryid") contryid: Int
    ): Call<JsonObject>

    @POST(ApiList.GET_CITY_BY_STATE_BY_COUNTRY)
    @FormUrlEncoded
    fun getCities(
        @Header("x-access-token") token: String,
        @Field("contryid") contryid: Int,
        @Field("stateId") stateId: Int
    ): Call<JsonObject>


    @POST(ApiList.GUIDE_SEARCH)
    @FormUrlEncoded
    fun guideSearch(
        @Field("coutryId") countryId: Int,
        @Field("stateId") stateList: Int,
        @Field("cityId") cityArray: ArrayList<Int>,
        @Field("dateOfArrival") dateOfArrival : String,
        @Field("dateOfDeparture") dateOfDeparture : String
    ): Call<JsonObject>


    @POST(ApiList.MY_PROFILE_DETAILS)
    @FormUrlEncoded
    fun myProfileUser(
        @Header("x-access-token") token: String,
        @Field("id") id: Int
    ): Call<JsonObject>


    @POST(ApiList.GUIDE_INFORMATION)
    @FormUrlEncoded
    fun guideDetails(
        @Header("x-access-token") token: String,
        @Field("guidId") guidId: Int
    ): Call<JsonObject>

    @Multipart
    @POST(ApiList.UPDATE_USER_PROFILE_IMAGE)
    fun updateProfilePic(
        @Part("id") id: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<JsonObject>

    @POST(ApiList.REQ_SEND_TO_GUIDE)
    @FormUrlEncoded
    fun sendTourReqToGuide(
        @Header("x-access-token") token: String,
        @Field("guideId") guideId : Int,
        @Field("requesttype") requesttype : Int,
        @Field("userId") userId : Int,
        @Field("startdate") startdate : String,
        @Field("enddate") enddate : String,
        @Field("stateId") stateId : String,
        @Field("poolId") poolId :String
    ):Call<JsonObject>

    @POST(ApiList.SEND_REQUEST_STATUS)
    @FormUrlEncoded
    fun sendRequestStatus(
        @Header("x-access-token") token : String,
        @Field("tourRequestId") tourRequestId: Int
    ):Call<JsonObject>


    @POST(ApiList.HELP_AND_SUPPORT)
    fun helpAndSupport(
        @Header("x-access-token") token : String
    ):Call<JsonObject>

    @POST(ApiList.ADD_PAYMENT_CARD)
    @FormUrlEncoded
    fun addPaymentCard(
        @Header("x-access-token") token : String,
        @Field("userId") userId : Int,
        @Field("cardnumber") cardnumber : String,
        @Field("cardholdername") cardholdername : String,
        @Field("cardcvv") cardcvv : String,
        @Field("validthrough") validthrough : String
    ):Call<JsonObject>

    @POST(ApiList.GET_CARD_DETAILS)
    @FormUrlEncoded
    fun getCardDetails(
        @Header("x-access-token") token: String,
        @Field("userId") userId : Int
    ): Call<JsonObject>


    @POST(ApiList.DELETE_CARD_DETAILS)
    @FormUrlEncoded
    fun deleteCardDetails(
        @Header("x-access-token") token : String,
        @Field("userId") userId : Int,
        @Field("cardId") cardId : Int
    ): Call<JsonObject>


    @POST(ApiList.UPCOMING_TOURS)
    @FormUrlEncoded
    fun getUpcomigTours(
        @Header("x-access-token") token  :String,
        @Field("userId") userId : Int,
        @Field("toursearchtype") toursearchtype : Int
    ): Call<JsonObject>

    @POST(ApiList.CURRENT_TOURS)
    @FormUrlEncoded
    fun getCurrentTours(
        @Header("x-access-token") token  :String,
        @Field("userId") userId : Int
    ): Call<JsonObject>


    @POST(ApiList.CANCEL_TOURS)
    @FormUrlEncoded
    fun cancelUpcomigTour(
        @Header("x-access-token") token  :String,
        @Field("guideId") userId : Int,
        @Field("tourRequestId") tourRequestId : Int
    ): Call<JsonObject>


    @POST(ApiList.PAYMENT_CLIENT_TOKEN)
    @FormUrlEncoded
    fun getPaymentClientToken(
        @Header("x-access-token") token  :String,
        @Field("userId") userId : Int,
        @Field("paymentBy") paymentBy : String
    ): Call<JsonObject>


    @POST(ApiList.PAYMENT_NONCE_TO_SERVER)
    @FormUrlEncoded
    fun sendPaymentNonceToServer(
        @Header("x-access-token") token : String,
        @Field("nonce") nonce : String,
        @Field("amount") amount : String,
        @Field("userId") userId : Int,
        @Field("paymentBy") paymentBy : String,
        @Field("guideId") guideId : Int,
        @Field("tourRequestId") tourId : Int,
        @Field("requesttype") requesttype : String,
        @Field("customerId") customerId : String
    ): Call<JsonObject>



    @POST(ApiList.UPDATE_DEVICE_TOKEN)
    @FormUrlEncoded
    fun updateDeviceToken(
        @Header("x-access-token") token: String,
        @Field("userId") id: Int,
        @Field("device_token") deviceToken: String,
        @Field("firebaseChatId") firebaseChatId :String
    ): Call<JsonObject>

    @POST(ApiList.GET_NEAR_BY_GUID)
    @FormUrlEncoded
    fun getNearByGuid(
        @Header("x-access-token") token: String,
        @Field("userId") userId : Int,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("coutryCode") countryCode: String
    ): Call<JsonObject>


    @POST(ApiList.USER_RATING_TO_GUIDE)
    @FormUrlEncoded
    fun userRatingToGuide(
        @Header("x-access-token") token: String,
        @Field("requestId") requestId :Int,
        @Field("ratingStatus") ratingStatus: String,
        @Field("rating") rating: Int,
        @Field("ratingComments") ratingComments: String
    ): Call<JsonObject>


    @POST(ApiList.GET_NOTIFICATION)
    @FormUrlEncoded
    fun getNOTIFICATION(
        @Header("x-access-token") token: String,
        @Field("userId") userId: Int
    ): Call<JsonObject>


    @POST(ApiList.PRIVACY_POLICY)
    fun privacyPolicy(
        @Header("x-access-token") token: String
    ): Call<JsonObject>

    @POST(ApiList.ABOUT_US)
    fun aboutUs(
        @Header("x-access-token") token: String
    ): Call<JsonObject>



}