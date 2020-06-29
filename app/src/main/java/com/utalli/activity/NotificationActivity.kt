package com.utalli.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.utalli.R
import com.utalli.adapter.NotificationAdapter
import com.utalli.fragment.CustomBottomSheetDialog
import com.utalli.helpers.AppPreference
import com.utalli.helpers.Utils
import com.utalli.models.Dashboard.NearByGuidResponse
import com.utalli.models.Notification.NotificationResponse
import com.utalli.viewModels.MyToursViewModel
import com.utalli.viewModels.NearMeViewModel
import com.utalli.viewModels.NotificationViewModel
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.activity_notification.iv_cancel
import kotlinx.android.synthetic.main.activity_notification.notification_list
import kotlinx.android.synthetic.main.fragment_near_me.*
import kotlinx.android.synthetic.main.layout_bottom_sheet.*


class NotificationActivity : AppCompatActivity(), View.OnClickListener {
    var notificationAdapter: NotificationAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager

    var notificationViewModel : NotificationViewModel?= null
    var notificationList: ArrayList<NotificationResponse>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_bottom_sheet)

        /*     var bottomSheetBehavior = BottomSheetBehavior.from(cl_bottom_sheet)

             bottomSheetBehavior.setPeekHeight(320);
             bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


             iv_notification_icon.setOnClickListener {
               *//*  if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }*//*

            val bottomSheetDialog = CustomBottomSheetDialog()
            bottomSheetDialog.show(getSupportFragmentManager(), "Custom Bottom Sheet")

        }

        iv_cancel.setOnClickListener(){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }


        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })*/

        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel::class.java)
        notificationList = ArrayList<NotificationResponse>()
        notificationAdapter = NotificationAdapter(this, notificationList!!)
        notification_list.setHasFixedSize(true)
        notification_list.layoutManager = LinearLayoutManager(this)
        notification_list.adapter = notificationAdapter
        getNotificationList()

        iv_cancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                finish()
            }
        })
    }

    private fun getNotificationList()
    {
        notificationViewModel!!.getNotificationList(this).observe(this, androidx.lifecycle.Observer {

                if (it!= null && it.has("status") && it.get("status").asString.equals("1"))
                {
                    if (it.has("data") && it.get("data") is JsonArray)
                    {
                        val listType = object : TypeToken<ArrayList<NotificationResponse>>() {}.type
                        val notificationResponse: ArrayList<NotificationResponse> = Gson().fromJson(it.get("data"), listType)
                        if (notificationResponse != null && notificationResponse.size > 0)
                        {
                            notification_list.visibility = View.VISIBLE
                            cl_noNotification.visibility = View.GONE
                            notificationList!!.addAll(notificationResponse)
                            notificationAdapter!!.notifyDataSetChanged()
                        }
                        else
                        {
                            notification_list.visibility = View.GONE
                            cl_noNotification.visibility = View.VISIBLE
                        }
                    }
                }
                else {
                    if (it!= null && it.has("message")){
                        Utils.showToast(this, it.get("message").asString)
                    }
                }
            })
    }


    override fun onClick(v: View?) {
        when (v?.id) {
        }
    }
}