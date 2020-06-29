package com.utalli.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.gson.JsonObject
import com.utalli.R
import com.utalli.viewModels.HelpAndSupportViewModel
import kotlinx.android.synthetic.main.activity_help_and_support.*
import kotlinx.android.synthetic.main.activity_webview.*

class HelpAndSupportActivity : AppCompatActivity(){
    var helpAndSupportViewModel : HelpAndSupportViewModel?= null
    var url : String = "http://3.13.3.42:8000/support_user"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_and_support)

        toolbar_helpSupport.title =""
        toolbar_helpSupport.setNavigationIcon(R.drawable.arrow_back_white)
        setSupportActionBar(toolbar_helpSupport)
        toolbar_helpSupport.setNavigationOnClickListener { finish() }


        initViews()

    }


    private fun initViews() {
   /*     helpAndSupportViewModel = ViewModelProviders.of(this).get(HelpAndSupportViewModel::class.java)

        helpAndSupportViewModel!!.helpAndSupport(this).observe(this, Observer {

            if(it!= null && it.has("status") && it.get("status").asString.equals("1")){
                if(it.has("data") && it.get("data") is JsonObject){

                    var dataObj = it.getAsJsonObject("data")
                    if(dataObj.has("email") && dataObj.has("helpcontact")){
                        tv_contactNumber.setText(dataObj.get("helpcontact").asString)
                        tv_emailId.setText(dataObj.get("email").asString)
                    }
                }
            }
        })*/

        webView_help_Support.getSettings().setJavaScriptEnabled(true);
        webView_help_Support.loadUrl(url);

    }


}