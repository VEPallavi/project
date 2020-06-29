package com.utalli.activity

import android.app.Application
//import com.sendbird.android.SendBird
import com.utalli.R

class UtaliiApplication: Application()
{

    override fun onCreate() {
        super.onCreate()

//        SendBird.init(getString(R.string.sendbird_app_id), this)
    }

}