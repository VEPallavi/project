package com.utalli.activity

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.utalli.R
import com.utalli.helpers.Utils
import com.utalli.viewModels.ResetPasswordViewModel
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity(), View.OnClickListener {
    var showPassword: Boolean = false
    var resetPasswordViewModel : ResetPasswordViewModel ?= null
    var otp: String = ""
    var idd : Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        toolbar_recoveryPass.title = ""
        toolbar_recoveryPass.setNavigationIcon(R.drawable.arrow_back_black)
        setSupportActionBar(toolbar_recoveryPass)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_recoveryPass.setNavigationOnClickListener { finish() }


        initViews()

    }

    private fun initViews() {
        resetPasswordViewModel = ViewModelProviders.of(this).get(ResetPasswordViewModel::class.java)

        idd = intent.getIntExtra("id",0)
        otp = intent.getStringExtra("OTP")

        Log.e("TAG"," otpp ResetPasswordActivity == "+otp)
        Log.e("TAG"," id ResetPasswordActivity == "+idd)

        iv_password_toggle.setOnClickListener(this)
        iv_reTypePassword_toggle.setOnClickListener(this)

        tv_save.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_save -> {

                reSetPassword()

            }

            R.id.iv_password_toggle ->{
                if (showPassword) {
                    et_password.transformationMethod = PasswordTransformationMethod.getInstance()

                    iv_password_toggle.setImageResource(R.drawable.eye_hide)
                } else {

                    et_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    iv_password_toggle.setImageResource(R.drawable.eye)
                }
                et_password.setSelection(et_password.text!!.length)
                showPassword = !showPassword
            }
            R.id.iv_reTypePassword_toggle ->{
                if (showPassword) {
                    et_retype_password.transformationMethod = PasswordTransformationMethod.getInstance()

                    iv_reTypePassword_toggle.setImageResource(R.drawable.eye_hide)
                } else {

                    et_retype_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    iv_reTypePassword_toggle.setImageResource(R.drawable.eye)
                }
                et_retype_password.setSelection(et_retype_password.text!!.length)
                showPassword = !showPassword
            }

        }

    }



     fun reSetPassword() {
        if(checkValidations()){

            resetPasswordViewModel!!.resetPassword(this,et_password.text.toString(),otp,idd)
                .observe(this, Observer {

                  if(it != null && it.has("status") && it.get("status").asString.equals("1")){

                      Utils.showToast(this, it.get("message").asString)

                   //   Handler().postDelayed(Runnable {
                          val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                          startActivity(intent)
                          finish()
                   //   }, 2000)

                  }
                  else {

                      if (it!= null && it.has("status")){
                          Utils.showToast(this, it.get("message").asString)
                          Log.e("TAG","message status 0 Login  === "+it.get("message").asString)
                      }
                  }


            })

        }




    }


    public fun checkValidations() : Boolean{

        if (!Utils.isInternetAvailable(this)) {
            Utils.showToast(this, resources.getString(R.string.msg_no_internet))
            return false
        }
        else if(et_password.text!!.length == 0){
            Utils.showToast(this, resources.getString(R.string.msg_empty_pass))
            return false
        }
        else if(et_password.text!!.length < 6){
            Utils.showToast(this, resources.getString(R.string.msg_invalid_pass))
            return false
        }
        else if(!(et_password.text.toString()).equals(et_retype_password.text.toString())){
            Utils.showToast(this, resources.getString(R.string.msg_not_same_pass))
            return false
        }


        return true
    }




}