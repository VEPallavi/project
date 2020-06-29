package com.utalli.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.utalli.R
import com.utalli.helpers.AppPreference
import com.utalli.helpers.Utils
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity(), View.OnClickListener {


    var preference: AppPreference? = null
    private val SPLASH_TIME_OUT: Long = 500


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        preference = AppPreference.getInstance(this)


        tv_getStarted.setOnClickListener(this)
        tv_learnMore.setOnClickListener(this)


        Handler().postDelayed(Runnable {

            if((preference!!.getAuthToken()).equals("")){

                var transition = Slide(Gravity.BOTTOM)
                transition.setDuration(500)
                TransitionManager.beginDelayedTransition(rl_splash_main, transition)
                tv_getStarted.visibility = View.VISIBLE
                tv_learnMore.visibility = View.VISIBLE
            } else{

                if(!Utils.isInternetAvailable(this)){
                    Utils.showToast(this, resources.getString(R.string.msg_no_internet))
                    //finish()
                }else{
                    val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, SPLASH_TIME_OUT)


    }

    override fun onClick(view: View?) {
        when (view?.id){
            R.id.tv_getStarted ->{
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

            R.id.tv_learnMore->{
                var intent = Intent(this,WebViewActivity::class.java)
                intent.putExtra("ScreenType","Learn More")
                intent.putExtra("Url","http://192.168.6.24:8000/api/v1/utalli_learn_more")
                startActivity(intent)
            }
        }

    }




}

