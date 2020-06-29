package com.utalli.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.utalli.R
import com.utalli.helpers.Utils
import com.utalli.viewModels.ForgetPassViewModel
import kotlinx.android.synthetic.main.activity_otp.*

class OTPActivity : AppCompatActivity(), View.OnClickListener {

    var idd : Int ?=null
    var OTP : String =" "
    var mobileNumber : String =""

    var forgetPassViewModel: ForgetPassViewModel? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        toolbar_otp.title = ""
        toolbar_otp.setNavigationIcon(R.drawable.arrow_back_black)
        setSupportActionBar(toolbar_otp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_otp.setNavigationOnClickListener { finish() }

        initViews()


    }

    private fun initViews() {
        forgetPassViewModel = ViewModelProviders.of(this).get(ForgetPassViewModel::class.java)

        idd = intent.getIntExtra("id",0)
        OTP = intent.getStringExtra("OTP")
        mobileNumber = intent.getStringExtra("mobileNumber")


        getEditText()


        tv_verify_btn.setOnClickListener(this)
        tv_resend_otp.setOnClickListener(this)

    }


    private fun getEditText() {

        et_otp_1.addTextChangedListener(object:TextWatcher {

            override fun onTextChanged(s:CharSequence, start:Int, before:Int, count:Int) {


            }
            override fun beforeTextChanged(s:CharSequence, start:Int, count:Int, after:Int) {

            }
            override fun afterTextChanged(s:Editable) {
                if (et_otp_1.getText().toString().length === 1) {
                    et_otp_2.requestFocus()
                }

            }
        })



        et_otp_2.addTextChangedListener(object: TextWatcher {

            override fun onTextChanged(s:CharSequence, start:Int, before:Int, count:Int) {

            }
            override fun beforeTextChanged(s:CharSequence, start:Int, count:Int, after:Int) {


            }
            override fun afterTextChanged(s:Editable) {

                if (et_otp_2.getText().toString().length === 1) {
                    et_otp_3.requestFocus()
                }
                else if(et_otp_2.text.toString().length == 0){
                    et_otp_1.requestFocus()
                }

            }
        })


        et_otp_3.addTextChangedListener(object : TextWatcher{

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(et_otp_3.text.toString().length == 1){
                    et_otp_4.requestFocus()
                }
                else if(et_otp_3.text.toString().length == 0){
                    et_otp_2.requestFocus()
                }
            }


        })


        et_otp_4.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }


            override fun afterTextChanged(s: Editable?) {

                if(et_otp_4.text.toString().length == 0){
                    et_otp_3.requestFocus()
                }

            }


        })

    }



    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_verify_btn -> {

                if(checkValidations()){
                    val intent = Intent(this, ResetPasswordActivity::class.java)
                    intent.putExtra("id",idd)
                    intent.putExtra("OTP", OTP)
                    Log.e("TAG"," otpp otp screen == "+OTP)
                    Log.e("TAG"," id otp screen == "+idd)
                    startActivity(intent)
                }

            }

            R.id.tv_resend_otp ->{

                if(Utils.isInternetAvailable(this)){

                    forgetPassViewModel!!.forgetPass(this,mobileNumber).observe(this, Observer {

                        if (it != null && it.has("status") && it.get("status").asString.equals("1")) {

                            if(it.has("data")){
                                var dataObject = it.getAsJsonObject("data")

                                if (dataObject.has("id") && dataObject.has("otp")) {
                                    OTP = ""
                                    Utils.showToast(this, it.get("message").asString)
                                    idd = dataObject.get("id").asInt
                                    OTP = dataObject.get("otp").asString

                                } else {

                                    Utils.showToast(this, getString(R.string.msg_common_error))
                                }

                            }

                        }


                    })

                }
                else {
                    Utils.showToast(this, resources.getString(R.string.msg_no_internet))
                }

            }

        }
    }


    fun checkValidations(): Boolean {

        if (!Utils.isInternetAvailable(this)) {
            Utils.showToast(this, resources.getString(R.string.msg_no_internet))
            return false
        } else if (et_otp_1.text!!.length == 0 || et_otp_2.text!!.length == 0 || et_otp_3.text!!.length == 0 || et_otp_4.text!!.length == 0) {
            Utils.showToast(this, resources.getString(R.string.msg_invalid_otp))
            return false
        }
        else if (!OTP.equals(et_otp_1.text!!.toString() + et_otp_2.text!!.toString() + et_otp_3.text!!.toString() + et_otp_4.text!!.toString())) {
            Utils.showToast(this, resources.getString(R.string.msg_invalid_otp))
            return false
        }

        return true

    }







}