    package com.utalli.fragment

    import android.app.Activity
    import android.app.Activity.RESULT_OK
    import android.app.Dialog
    import android.content.Intent
    import android.graphics.Color
    import android.graphics.drawable.ColorDrawable
    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.view.Window
    import android.widget.TextView
    import android.widget.Toast
    import android.widget.Toast.LENGTH_SHORT
    import androidx.fragment.app.Fragment
    import androidx.lifecycle.Observer
    import androidx.lifecycle.ViewModelProviders
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.braintreepayments.api.dropin.DropInActivity
    import com.braintreepayments.api.dropin.DropInRequest
    import com.braintreepayments.api.dropin.DropInResult
    import com.google.gson.Gson
    import com.google.gson.JsonArray
    import com.google.gson.reflect.TypeToken
    import com.utalli.R
    import com.utalli.adapter.UpcomingTourAdapter
    import com.utalli.callBack.UpcomingTourCancelCallBack
    import com.utalli.helpers.Utils
    import com.utalli.models.UpcomingTourListModel
    import com.utalli.viewModels.MyToursViewModel
    import kotlinx.android.synthetic.main.dialog_payment.*
    import kotlinx.android.synthetic.main.dialog_payment.tv_OKAY
    import kotlinx.android.synthetic.main.dialog_payment_success.*
    import kotlinx.android.synthetic.main.fragment_upcoming_tours.*



    class UpcomingToursFragment : Fragment(), UpcomingTourCancelCallBack{

        var myToursViewModel : MyToursViewModel?= null
        var upcomingTourAdapter : UpcomingTourAdapter ? = null
        lateinit var upComingTourDataList : ArrayList<UpcomingTourListModel>
        var dialogPayment: Dialog? = null

        private val PATH_TO_SERVER = "PATH_TO_SERVER"         //"https://sandbox.braintreegateway.com/merchants/69dmbrq8b8fxmc6w/merchant_accounts";
        private var clientToken: String = ""
        private val BRAINTREE_REQUEST_CODE = 4949

        var amount : String =""
        var paymentBy : String ="user"
        var guide_Id : Int = 0
        var requesttype : String = ""
        var tourRequestId : Int =0
        var customerId : String = ""




        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_upcoming_tours, container, false)

            myToursViewModel = ViewModelProviders.of(activity!!).get(MyToursViewModel::class.java)
            getUpcomingToursData()
            getClientTokenFromServer()
            return view
        }


        private fun getClientTokenFromServer() {

            myToursViewModel!!.getPaymentClientToken(activity!!).observe(this, Observer {

                if(it != null  && it.has("status") && it.get("status").asString.equals("1")){
                    if(it.has("client_token")){
                       // Utils.showToast(activity!!, it.get("message").asString)
                        clientToken = it.get("client_token").asString
                        customerId = it.get("customer_id").asString
                    }
                }
            })

        /*    val androidClient = AsyncHttpClient()
            androidClient.get(PATH_TO_SERVER, object : TextHttpResponseHandler() {
                override fun onFailure(statusCode: Int, headers: Array<Header>, responseString: String, throwable: Throwable) {
                    Log.e("TAG", getString(R.string.token_failed) + responseString)
                }

                override fun onSuccess(statusCode: Int, headers: Array<Header>, responseToken: String) {
                    Log.e("TAG", "Client token: $responseToken")
                    clientToken = responseToken
                }
            })*/

        }


        override fun setUserVisibleHint(isVisibleToUser: Boolean) {
            super.setUserVisibleHint(isVisibleToUser)
            if (isVisibleToUser) {
                //getUpcomingToursData()
            } else {
                Log.e("@@"," UpcomingToursFragment  Gone")
            }
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            upComingTourDataList = ArrayList<UpcomingTourListModel>()
            upcomingTourAdapter = UpcomingTourAdapter(activity!!, upComingTourDataList, this)

               rv_my_tours.setHasFixedSize(true)
               rv_my_tours.layoutManager = LinearLayoutManager(activity)
               rv_my_tours.adapter = upcomingTourAdapter

        }

        private fun getUpcomingToursData() {

            myToursViewModel!!.getUpcomigTours(activity!!, 1).observe(this, Observer {

                if(it != null && it.has("status") && it.get("status").asString.equals("1")) {

                    if(it.has("data") && it.get("data") is JsonArray){

                        val type = object : TypeToken<List<UpcomingTourListModel>>() {}.type
                        var upComingTourList = Gson().fromJson<List<UpcomingTourListModel>>(it.get("data"), type)

                        if(upComingTourList != null && upComingTourList.size >0){

                            rv_my_tours.visibility = View.VISIBLE
                            cl_no_upcomingTourFound.visibility = View.GONE

                            upComingTourDataList.clear()
                            upComingTourDataList.addAll(upComingTourList)
                            upcomingTourAdapter!!.notifyDataSetChanged()
//                            upcomingTourAdapter = UpcomingTourAdapter(activity!!, upComingTourList, )
//                            rv_my_tours.adapter = upcomingTourAdapter
                        }
                        else
                        {
                            cl_no_upcomingTourFound.visibility = View.VISIBLE
                            rv_my_tours.visibility = View.GONE
                        }
                    }
                    else {
                        if (it.has("message"))
                            Utils.showToast(activity!!, it.get("message").asString)
                        else {
                            Utils.showToast(activity!!, getString(R.string.msg_common_error))
                        }
                    }
                } else {

                }
            })
        }



//        {
//            "status": 0,
//            "message": {}
//        }



        override fun upComingTourListener(itemDetails: UpcomingTourListModel, type: String) {

            if(type.equals("CANCEL")){
                setTourCancelRequest( itemDetails.guideId,itemDetails.id, itemDetails)
            }
            else if(type.equals("PAY_NOW")){
                amount = itemDetails.tourPrice
                guide_Id = itemDetails.guideId
                requesttype = itemDetails.requesttype
                tourRequestId = itemDetails.id

                setPaymentGateway()
            }

        }


        private fun setTourCancelRequest( guideId: Int, tourRequestId: Int, itemDetails : UpcomingTourListModel) {
            myToursViewModel!!.cancleUpcomingTour(activity!! ,guideId, tourRequestId).observe(this, Observer {
                if(it != null && it.has("status") && it.get("status").asString.equals("1")) {
                    Utils.showToast(activity!!, it.get("message").asString)
                    upComingTourDataList.remove(itemDetails)
                    upcomingTourAdapter!!.notifyDataSetChanged()
                }
            })
        }



        private fun setPaymentGateway() {
            if(!clientToken.equals("")){
                val dropInRequest1 = DropInRequest().clientToken(clientToken)
                startActivityForResult(dropInRequest1.getIntent(activity), BRAINTREE_REQUEST_CODE)
            }
        }




         override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            Log.v("resultCode","resultCode=="+resultCode);
            if (requestCode == BRAINTREE_REQUEST_CODE) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val result = data!!.getParcelableExtra<DropInResult>(DropInResult.EXTRA_DROP_IN_RESULT)
                        val paymentMethodNonce = result.paymentMethodNonce!!.nonce
                        Log.e("PayNonce", "UPcomingTourFargment PayNonce==" + paymentMethodNonce)

                        // Send paymentMethodNonce to your server here..
                        sendPaymentNonceToServer(paymentMethodNonce)
                    }

                    Activity.RESULT_CANCELED -> Log.v("Cancelled", "Cancelled!!")

                    else -> {
                        Log.v("Error", "Error!! ")

                        val error = data!!.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception
                    }
                }
            }
        }



        fun sendPaymentNonceToServer(paymentNonce: String) {

            myToursViewModel!!.sendPaymentNonceToServer(activity!!,paymentNonce, amount, paymentBy, guide_Id, tourRequestId, requesttype, customerId).observe(this, Observer {

                if(it != null && it.has("status") && it.get("status").asString.equals("1")){

                    if(it.has("data")){

                        var dataObj = it.getAsJsonObject("data")

                        if( dataObj.get("paymentStatus").asBoolean.equals(false)){
                            dialogPayment = Dialog(activity!!)
                            dialogPayment!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialogPayment!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dialogPayment!!.setContentView(R.layout.dialog_payment)

                            dialogPayment!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                            var tv_payment = dialogPayment!!.findViewById<TextView>(R.id.tv_payment)
                            var tv_OKAY = dialogPayment!!.findViewById<TextView>(R.id.tv_OKAY)

                            //iv_title.setImageResource(R.drawable.failure)
                           // tv_success_failure.setText("Failure !")
                           // tv_payment.setText("Your transaction could not be processed. Please try again")

                            dialogPayment!!.show()

                          //  Utils.showToast(activity!! , "Your transaction could not be processed. Please try again")

                            tv_OKAY.setOnClickListener {
                                dialogPayment!!.dismiss()
                            }
                        }

                        else if(dataObj.get("paymentStatus").asBoolean.equals(true)){
                            Utils.showToast(activity!! , it.get("message").asString)
                            Utils.showLog(it.get("message").asString)

                            dialogPayment = Dialog(activity!!)
                            dialogPayment!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                            dialogPayment!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dialogPayment!!.setContentView(R.layout.dialog_payment_success)
                            dialogPayment!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                            var tv_payment_success = dialogPayment!!.findViewById<TextView>(R.id.tv_payment_success)
                            var tv_OKAY = dialogPayment!!.findViewById<TextView>(R.id.tv_OKAY)

                           // iv_title.setImageResource(R.drawable.success)
                          //  tv_success_failure.setText("Success !")
                            tv_payment_success.setText(" "+"Your Payment is successful and your transaction id is " + dataObj.get("transactionId").asString)

                            dialogPayment!!.show()

                            tv_OKAY.setOnClickListener {

                                dialogPayment!!.dismiss()

                                getUpcomingToursData()

                              //  upcomingTourAdapter!!.notifyDataSetChanged()
                            }

                        }
                    }
                }
            })
        }










    }