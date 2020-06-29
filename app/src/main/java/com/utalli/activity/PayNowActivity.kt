package com.utalli.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.utalli.R

class PayNowActivity : AppCompatActivity() {

    private val PATH_TO_SERVER = "PATH_TO_SERVER"         //"https://sandbox.braintreegateway.com/merchants/69dmbrq8b8fxmc6w/merchant_accounts";
    private var clientToken: String? = null
    private val BRAINTREE_REQUEST_CODE = 4949




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_now)

    //    getClientTokenFromServer()


    }






}