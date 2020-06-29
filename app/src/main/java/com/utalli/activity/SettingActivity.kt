package com.utalli.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.utalli.R
import com.utalli.helpers.AppPreference
import com.utalli.helpers.Utils
import com.utalli.viewModels.SettingsViewModel
import kotlinx.android.synthetic.main.activity_settings.*

class SettingActivity : AppCompatActivity(), View.OnClickListener{

    var settingsViewModel : SettingsViewModel?= null
    var preference : AppPreference ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar.title = ""
        toolbar.setNavigationIcon(R.drawable.arrow_back_white)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        initView()
    }



    private fun initView() {

        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        preference = AppPreference.getInstance(this)

        tv_helpSupport.setOnClickListener(this)
        tv_privacyPolicy.setOnClickListener(this)
        tv_aboutUs.setOnClickListener(this)


        if (!preference!!.getUserNotificationStatus().equals("")) {
            notification_switch!!.isChecked = preference!!.getUserNotificationStatus().equals("1")
        } else {
            notification_switch!!.isChecked = true
        }


        notification_switch.setOnCheckedChangeListener { buttonView, value ->
            if (value)
            {
                preference!!.setUserNotificationStatus("1")
            }
            else
            {
                preference!!.setUserNotificationStatus("0")
            }
        }


    }


    override fun onClick(v: View?) {
        when(v?.id){

            R.id.tv_helpSupport ->{
                var intent = Intent(this,HelpAndSupportActivity::class.java)
                startActivity(intent)
            }

            R.id.tv_privacyPolicy ->{
                var intent = Intent(this,WebViewActivity::class.java)
                intent.putExtra("ScreenType","Privacy Policy")
                intent.putExtra("Url","http://3.13.3.42:8000/policy_user")
                startActivity(intent)
            }

            R.id.tv_aboutUs ->{
                var intent = Intent(this,WebViewActivity::class.java)
                intent.putExtra("ScreenType","About Us")
                intent.putExtra("Url","http://3.13.3.42:8000/aboutus_user")
                startActivity(intent)
            }
        }
    }

}