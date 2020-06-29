package com.utalli.fragment

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.animation.AlphaAnimation


import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout


import com.utalli.adapter.HomeListGuideAdapter
import com.utalli.helpers.Utils
import kotlinx.android.synthetic.main.fragment_near_me.*

import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.transitionseverywhere.TransitionManager
import com.utalli.R
import android.os.Handler
import android.view.*
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.client.Firebase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.utalli.activity.*
import com.utalli.helpers.AppConstants
import com.utalli.helpers.AppPreference
import com.utalli.models.Dashboard.DashboardArrayList
import com.utalli.models.Dashboard.GuidListData
import com.utalli.models.Dashboard.NearByGuidResponse
import com.utalli.models.Dashboard.PopulardestinationList
import com.utalli.models.UserModel
import com.utalli.viewModels.NearMeViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.my_profile_activity.*
import java.util.HashMap
import kotlin.collections.ArrayList
import com.utalli.activity.HomeActivity as HomeActivity


class NearMeFragment : Fragment(), View.OnClickListener {

    private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.6f
    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
    private val ALPHA_ANIMATIONS_DURATION = 200

    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true
    private var nearMeViewModel: NearMeViewModel? = null
    var user: UserModel? = null
    private var mLocation: Location? = null

    var mHomeDashboardAdapterModel:DashboardArrayList? = null
    var nearByGuidListAdapter: HomeListGuideAdapter? = null
    var isCall: Boolean = false

    var dialogRating: Dialog? = null

    var rating1 : String =""
    var isRated : Boolean ?= null
    var tourRequestId : Int ?= null

    var guideName : String?= null
    var guideProfilePic : String ?= null

    var mContext : Activity ?= null


    override fun onAttach(context: Activity) {
        super.onAttach(context)
        mContext = context
    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar_nearMe.title = ""
        activity!!.setActionBar(toolbar_nearMe)
        collapsing_toolbar.title = "hello hello"
        shimmer_view_container.visibility = View.VISIBLE
        shimmer_view_container.startShimmer()
        Handler().postDelayed(Runnable {
            shimmer_view_container?.visibility = View.GONE
            shimmer_view_container?.stopShimmer()
            rv_guide_list?.setHasFixedSize(true)
            rv_guide_list?.layoutManager = LinearLayoutManager(activity)
            rv_guide_list?.visibility = View.VISIBLE

        }, 1500)

        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {

                Log.e("TAG", "vertical offsett === " + verticalOffset)


                var maxScroll = appBarLayout!!.getTotalScrollRange()
                var percentage = (Math.abs(verticalOffset)).toFloat() / (maxScroll).toFloat()

                Log.e("TAG", "vertical offsett maxScroll === " + maxScroll)
                Log.e("TAG", "vertical offsett percentage === " + percentage)


                if (verticalOffset == 0) {

                    TransitionManager.beginDelayedTransition(appBarLayout)
                    profile_Pic_toolbar.visibility = View.GONE
                    iv_notification_toolbar.visibility = View.GONE
                    cl_collapsingToolbar_items.visibility = View.VISIBLE

                    /*  val animFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
                      profile_Pic_toolbar.startAnimation(animFadeIn)*/
                } else if (Math.abs(verticalOffset) == appBarLayout!!.getTotalScrollRange()) {

                    TransitionManager.beginDelayedTransition(appBarLayout)
                    cl_collapsingToolbar_items.visibility = View.GONE
                    profile_Pic_toolbar.visibility = View.VISIBLE
                    iv_notification_toolbar.visibility = View.VISIBLE
                }
            }
        })

        et_location.text = AppPreference.getInstance(activity!!).getUserLastLocation()
        iv_notification.setOnClickListener(this)
        iv_notification_toolbar.setOnClickListener(this)
        profile_Pic.setOnClickListener(this)
        profile_Pic_toolbar.setOnClickListener(this)
        tv_searchSecond_toolbar.setOnClickListener(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_near_me, container, false)
        nearMeViewModel = ViewModelProviders.of(this).get(NearMeViewModel::class.java)
        mHomeDashboardAdapterModel = DashboardArrayList()


        registerReceiver()
        receiveCurrentLocation()

        return view
    }

    private fun receiveCurrentLocation()
    {
        mLocation =  (activity as HomeActivity).receivedLocation()
        if (mLocation != null)
        {
            isCall = false;
            getNearbyGuides()

        }
        else
        {
            isCall = true;
        }
    }

    private fun setupGuidList()
    {
        nearByGuidListAdapter = HomeListGuideAdapter(activity!!, mHomeDashboardAdapterModel)
        rv_guide_list.adapter = nearByGuidListAdapter
        nearByGuidListAdapter!!.setItemClickListener(object : HomeListGuideAdapter.ListItemClickListener {
            override
            fun listItemClickListener(guidProfileModel: GuidListData) {
                if (guidProfileModel != null)
                {
                    var intent = Intent(activity, GuideProfileDetailsActivity::class.java)
                    intent.putExtra("IsComingFrom", AppConstants.GUIDE_PROFILE_SEE_FROM_HOME)
                    intent.putExtra("guideId", guidProfileModel.getId()!!.toInt())
                    startActivity(intent)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        user = AppPreference.getInstance(activity!!).getUserData() as UserModel
        if (user != null) {
            Glide.with(this)
                .load(user!!.profile_img)
                .apply(RequestOptions().placeholder(R.mipmap.ic_profile_placeholder).error(R.mipmap.ic_profile_placeholder))
                .into(profile_Pic_toolbar)

            Glide.with(this)
                .load(user!!.profile_img)
                .apply(RequestOptions().placeholder(R.mipmap.ic_profile_placeholder).error(R.mipmap. ic_profile_placeholder))
                .into(profile_Pic)
        }
    }

    private fun registerReceiver() {
        val filter = IntentFilter()
        filter.addAction("LOCATION_UPDATED")
        LocalBroadcastManager.getInstance(activity!!).registerReceiver(mReciever, filter)
    }


    var mReciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (et_location != null) {
                et_location!!.text = AppPreference.getInstance(activity!!).getUserLastLocation()
                if (pb_location != null && pb_location.visibility == View.VISIBLE) {
                    pb_location.visibility = View.INVISIBLE
                }
            }
            mLocation = intent!!.getParcelableExtra("location")
            if (isCall)
            {
                getNearbyGuides()
                isCall = false
            }
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.iv_notification -> startActivity(Intent(activity, NotificationActivity::class.java))
            R.id.iv_notification_toolbar -> startActivity(Intent(activity, NotificationActivity::class.java))
            R.id.profile_Pic -> startActivity(Intent(activity, MyProfileActivity::class.java))
            R.id.profile_Pic_toolbar -> startActivity(Intent(activity, MyProfileActivity::class.java))
            R.id.tv_searchSecond_toolbar -> startActivity(Intent(activity, SearchActivity::class.java))
        }
    }


    fun startAlphaAnimation(v: View, duration: Int, visibility: Int) {
        val alphaAnimation = if (visibility == View.VISIBLE)
            AlphaAnimation(0f, 1f)
        else
            AlphaAnimation(1f, 0f)

        alphaAnimation.duration = duration.toLong()
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
    }

    /**
     * The purpose of this method is use to Get Near By Guides
     */
    private fun getNearbyGuides() {
        if (mLocation != null)
        {
            var latitude = mLocation?.latitude
            var longitude = mLocation?.longitude
            var countryCode = AppPreference.getInstance(activity!!).getCountryCode()
            nearMeViewModel!!.getNearbyGuides(activity!!, AppPreference.getInstance(activity!!).getAuthToken(), AppPreference.getInstance(activity!!).getId(), latitude.toString(), longitude.toString(), countryCode)
                .observe(this, androidx.lifecycle.Observer {
                    if (it!= null && it.has("status") && it.get("status").asString.equals("1"))
                    {
                        var response: NearByGuidResponse = Gson().fromJson(it, NearByGuidResponse::class.java)

                        isRated = response.getIsRated()

                        if(response.getTourRequestId() != null){
                            tourRequestId = response.getTourRequestId()
                        }

                        if(response.getGuideImg() != null) {
                            guideProfilePic = response.getGuideImg()
                        }

                        if( response.getGuideName() != null){
                            guideName = response.getGuideName()
                        }


                        if(!(mContext!!.isFinishing())) {
                            //show dialog
                            if(isRated == false){
                                openDialogForRating()
                            }
                        }




//                        try {
//
//                        }
//                        catch (e: WindowManager.BadTokenException) {
//                            Log.e("TAG","Exception "+e)
//                        }



                        var popularDestinationArray = response.getPopulardestinations()
                        var nearByGuidArray = response.getData()
                        if (popularDestinationArray != null && popularDestinationArray.size > 0)
                        {
                            mHomeDashboardAdapterModel!!.setPopulardestinations(popularDestinationArray)
                        }
                        else
                        {

                        }

                        if (nearByGuidArray != null && nearByGuidArray.size > 0)
                        {
                            mHomeDashboardAdapterModel?.setData(nearByGuidArray)
                        }
                        else
                        {

                        }
                        if (mHomeDashboardAdapterModel!!.getData() != null && mHomeDashboardAdapterModel!!.getData()!!.size > 0)
                        {
                            cl_parentLayout.visibility = View.VISIBLE
                            cl_no_GuideFound.visibility = View.GONE
                            setupGuidList()
                        }
                        else
                        {
                            cl_parentLayout.visibility = View.GONE
                            cl_no_GuideFound.visibility = View.VISIBLE
                        }
                    }
                    else {
                        if (it!= null && it.has("message")){
                            Utils.showToast(activity!!, it.get("message").asString)
                        }
                    }
                })
        }
    }


    fun openDialogForRating(){
        dialogRating = Dialog(activity!!)
        dialogRating!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRating!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRating!!.setContentView(R.layout.dialog_rating)
        dialogRating!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)


        var tv_rate_now = dialogRating!!.findViewById<TextView>(R.id.tv_rate_now_)
        var tv_cancel = dialogRating!!.findViewById<TextView>(R.id.tv_cancel)
        var tv_guideName = dialogRating!!.findViewById<TextView>(R.id.tv_guideName)
        var rating_bar = dialogRating!!.findViewById<RatingBar>(R.id.rating_bar)
        var iv_profile_pict = dialogRating!!.findViewById<CircleImageView>(R.id.iv_profile_pict)

        dialogRating!!.show()

        dialogRating!!.setCanceledOnTouchOutside(false)
       // dialogRating!!.setCancelable(false);

        tv_guideName.setText(guideName)

        Glide.with(this)
            .load(guideProfilePic)
            .apply(RequestOptions().placeholder(R.mipmap.ic_profile_placeholder).error(R.mipmap.ic_profile_placeholder))
            .into(iv_profile_pict)





       // 1=rated, 0=cancel
        tv_rate_now.setOnClickListener {
            rating1 = (rating_bar.getRating()).toString()
                       if(rating1.equals("0.0"))
                       {
                           Toast.makeText(activity!!, getString(R.string.msg_please_give_the_ratings), Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           nearMeViewModel!!.userRatingToGuide(activity!!, AppPreference.getInstance(activity!!).getAuthToken(), tourRequestId!!, "1",
                               rating_bar.rating.toInt(), "").observe(this, Observer {

                               if(it!= null && it.has("status") && it.get("status").asString.equals("1"))
                               {
                                   Utils.showToast(activity!!, it.get("message").asString)
                               }
                               dialogRating!!.dismiss()
                           })
                       }
        }


        tv_cancel.setOnClickListener {
            nearMeViewModel!!.userRatingToGuide(activity!!, AppPreference.getInstance(activity!!).getAuthToken(), tourRequestId!!, "0",
                0, "").observe(this, Observer {

                if(it!= null && it.has("status") && it.get("status").asString.equals("1"))
                {
                    Utils.showToast(activity!!, it.get("message").asString)
                }
                dialogRating!!.dismiss()
            })
        }

    }











}
