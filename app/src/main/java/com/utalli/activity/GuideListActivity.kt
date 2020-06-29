package com.utalli.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.utalli.R
import com.utalli.adapter.GuideListAdapter
import kotlinx.android.synthetic.main.activity_guide_list_new.*
/*import kotlinx.android.synthetic.main.activity_guide_list_new.spacer*/

/*import kotlinx.android.synthetic.main.activity_guide_lists.cl_pop_up
import kotlinx.android.synthetic.main.activity_guide_lists.iv_back_arrow
import kotlinx.android.synthetic.main.activity_guide_lists.iv_three_dots
import kotlinx.android.synthetic.main.activity_guide_lists.rv_guide_list
import kotlinx.android.synthetic.main.activity_guide_lists.tv_cancel
import kotlinx.android.synthetic.main.activity_guide_lists.tv_edit_trip*/
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.transitionseverywhere.Recolor
import com.transitionseverywhere.TransitionManager
import com.utalli.callBack.GuideListCallBack
import com.utalli.helpers.AppConstants
import com.utalli.helpers.Utils
import com.utalli.models.*
//import com.utalli.models.GuideSearchViewModel
import com.utalli.viewModels.GuideListViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class GuideListActivity : AppCompatActivity(), View.OnClickListener {

// country,states,date of arrival,date of departure

    lateinit var selectedCountry: LocationSearchDataItems

    var userSelectedCityList = ArrayList<IndividualCityDetail>()
    var tourStartDate = ""
    var tourEndDate = ""
    lateinit var guideListViewModel: GuideListViewModel
    var guideListCallBack : GuideListCallBack?= null
    var selectedStatesId = ""
    var dateOfArrival :String =""
    var dateOfDeparture : String =""
    var userSelectedstateName: String =""
    var userSelectedStateId: Int?=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide_list_new)

        initViews()

    }

    private fun initViews() {

        guideListViewModel = ViewModelProviders.of(this).get(GuideListViewModel::class.java)
        selectedCountry = intent.getParcelableExtra("selectedCountry")
        userSelectedstateName = intent.getStringExtra("selectedStateName")
        userSelectedStateId = intent.getIntExtra("selectedStateId", 0)
        userSelectedCityList = intent.getSerializableExtra("selectedCities") as ArrayList<IndividualCityDetail>
        tourStartDate = intent.getStringExtra("tourStartDate")
        tourEndDate = intent.getStringExtra("tourEndDate")

        Log.e("TAG","<<<< tourStartDate ===  "+ tourStartDate)
        Log.e("TAG","<<<< tourEndDate ===  "+ tourEndDate)


        var daysDiff = getCountOfDays(tourStartDate, tourEndDate)


        if (daysDiff.equals("0")) {
            tv_days_count.text = "On "
            tv_date.text = tourStartDate
        } else if (daysDiff.equals("1")) {
            tv_days_count.text = daysDiff + " day, "
            tv_date.text = formatDate(tourStartDate) + " to " + formatDate(tourEndDate)
        } else {
            tv_days_count.text = daysDiff + " days, "
            tv_date.text = formatDate(tourStartDate) + " to " + formatDate(tourEndDate)
        }




        iv_three_dots.setOnClickListener(this)
        iv_back_arrow.setOnClickListener(this)
        tv_edit_trip.setOnClickListener(this)
        tv_cancel.setOnClickListener(this)
        toolbar_back_arroww.setOnClickListener(this)


        tv_country_name.text = userSelectedstateName+", "+selectedCountry.name
        toolbar_country_name_toVisit.text = userSelectedstateName+", "+ selectedCountry.name

        var selectedCities = ""

        if (userSelectedCityList != null && userSelectedCityList.size > 0) {

            for (i in 0..userSelectedCityList.size - 1) {

                if (i == 0){
                    selectedCities = userSelectedCityList.get(i).name
                    selectedStatesId = userSelectedCityList.get(i).id.toString()
                }

                else if (i <= userSelectedCityList.size - 1){
                    selectedCities = selectedCities + ", " + userSelectedCityList.get(i).name
                    selectedStatesId = selectedStatesId + "," + userSelectedCityList.get(i).id.toString()
                }


                //selectedStates = userSelectedCityList.get(i).name + ", " + selectedStates
            }
        }

        if (!selectedCities.equals(""))
            tv_place_name.text = selectedCities
        else
            tv_place_name.text = selectedCountry.name




        appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {

                if (verticalOffset == 0) {

                    //TransitionManager.beginDelayedTransition(coordinate_layout)
                    var transition = Recolor()
                    transition.setDuration(500)

                    TransitionManager.beginDelayedTransition(
                        toolbar,
                        transition
                    )                   // spacer.visibility = View.VISIBLE
                    /*   cl_countryDateDetails.visibility = View.VISIBLE
                       cl_countryDetails.visibility = View.GONE*/

                    toolbar.setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.transparent)))


                } else if (Math.abs(verticalOffset) == appBarLayout!!.getTotalScrollRange()) {
                    // TransitionManager.beginDelayedTransition(coordinate_layout)

                    var transition = Recolor()
                    transition.setDuration(500)

                    TransitionManager.beginDelayedTransition(
                        toolbar,
                        transition
                    )
                    //  spacer.visibility = View.GONE
                    /* cl_countryDateDetails.visibility = View.GONE
                     cl_countryDetails.visibility = View.VISIBLE*/
                    toolbar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary)))
                }
            }
        })



        rv_guide_list.setHasFixedSize(true)
        rv_guide_list.layoutManager = LinearLayoutManager(this)


//        var stateIdArray = IntArray(0)

        var datalist : ArrayList<Int> = ArrayList()
        datalist.add(-1)
        if (userSelectedCityList != null && userSelectedCityList.size > 0) {
//            stateIdArray = IntArray(userSelectedCityList.size)
            for (i in 0..userSelectedCityList.size - 1) {
//                stateIdArray[i] = userSelectedCityList.get(i).id
                datalist.add((userSelectedCityList.get(i).id))
            }
        }



        try {
            val sdfSource = SimpleDateFormat("dd/MM/yyyy")   //30/8/2019
            val date = sdfSource.parse(tourStartDate)
            val sdfDestination = SimpleDateFormat("yyyy-MM-dd")
            dateOfArrival = sdfDestination.format(date)
        } catch (pe: ParseException) {
            println("Parse Exception : $pe")
        }


        try {
            val sdfSource = SimpleDateFormat("dd/MM/yyyy")   //30/8/2019
            val date = sdfSource.parse(tourEndDate)
            val sdfDestination = SimpleDateFormat("yyyy-MM-dd")
            dateOfDeparture = sdfDestination.format(date)
        } catch (pe: ParseException) {
            println("Parse Exception : $pe")
        }
/////////////////////////////////////////////////////////////// t74658596860978ytjbfjkdbjhnyt

        guideListViewModel.searchGuide(this, selectedCountry.id, userSelectedStateId!!, datalist, dateOfArrival, dateOfDeparture)
            .observe(this, androidx.lifecycle.Observer {

                if (it != null) {
                    if (it.has("status") && it.get("status").asString.equals("1")) {

                        if (it.has("data") && it.get("data") is JsonArray) {

                            val type = object : TypeToken<List<GuideListModel>>() {}.type
                            var guidesList = Gson().fromJson<List<GuideListModel>>(it.get("data"), type)

                            if (guidesList != null && guidesList.size > 0) {

                                rv_guide_list.adapter = GuideListAdapter(this, guidesList, object : GuideListCallBack{

                                    override fun guideListData(itemDetails: GuideInfoModel) {

                                        var intent = Intent(this@GuideListActivity, GuideProfileDetailsActivity::class.java)
                                        intent.putExtra("IsComingFrom", AppConstants.GUIDE_PROFILE_SEE_FROM_PLACE_SEARCH)
                                        intent.putExtra("guideId", itemDetails.id.toInt())
                                        intent.putExtra("tourStartDate",tourStartDate)
                                        intent.putExtra("tourEndDate", tourEndDate)
                                        intent.putExtra("selectedStatesId",selectedStatesId)
                                        startActivity(intent)
                                    }
                                })
                            }

                            else{

                                if(guidesList.size == 0){
                                    rv_guide_list.visibility = View.GONE
                                    tv_no_data_found_guide_list.visibility = View.VISIBLE
                                }
                                else{
                                    rv_guide_list.visibility = View.VISIBLE
                                    tv_no_data_found_guide_list.visibility = View.GONE
                                }
                            }


                        } else {
                            if (it.has("message"))
                                Utils.showToast(this, it.get("message").asString)
                            else {
                                Utils.showToast(this, getString(R.string.msg_common_error))
                            }
                        }

                    }
                }


            })


    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.iv_three_dots -> {
                cl_pop_up.visibility = View.VISIBLE
            }
            R.id.iv_back_arrow -> {
                finish()
            }
            R.id.toolbar_back_arroww -> {
                finish()
            }
            R.id.tv_edit_trip -> {
                cl_pop_up.visibility = View.GONE
            }
            R.id.tv_cancel -> {
                cl_pop_up.visibility = View.GONE
            }

        }

    }


    fun formatDate(date: String): String {
        var sdfFrom = SimpleDateFormat("dd/MM/yyyy")
        var sdfTo = SimpleDateFormat("MMM dd, yyyy")

        var formattedDate = sdfFrom.parse(date)

        return sdfTo.format(formattedDate)
    }

    fun getCountOfDays(startDate: String, endDate: String): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        var createdConvertedDate: Date? = null
        var expireCovertedDate: Date? = null
        var todayWithZeroTime: Date? = null
        try {
            createdConvertedDate = dateFormat.parse(startDate)
            expireCovertedDate = dateFormat.parse(endDate)

            val today = Date()

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        var cYear = 0
        var cMonth = 0
        var cDay = 0

        if (createdConvertedDate!!.after(todayWithZeroTime)) {
            val cCal = Calendar.getInstance()
            cCal.setTime(createdConvertedDate)
            cYear = cCal.get(Calendar.YEAR)
            cMonth = cCal.get(Calendar.MONTH)
            cDay = cCal.get(Calendar.DAY_OF_MONTH)

        } else {
            val cCal = Calendar.getInstance()
            cCal.setTime(todayWithZeroTime)
            cYear = cCal.get(Calendar.YEAR)
            cMonth = cCal.get(Calendar.MONTH)
            cDay = cCal.get(Calendar.DAY_OF_MONTH)
        }


        val eCal = Calendar.getInstance()
        eCal.setTime(expireCovertedDate)

        val eYear = eCal.get(Calendar.YEAR)
        val eMonth = eCal.get(Calendar.MONTH)
        val eDay = eCal.get(Calendar.DAY_OF_MONTH)

        val date1 = Calendar.getInstance()
        val date2 = Calendar.getInstance()

        date1.clear()
        date1.set(cYear, cMonth, cDay)
        date2.clear()
        date2.set(eYear, eMonth, eDay)

        val diff = date2.getTimeInMillis() - date1.getTimeInMillis()

        val dayCount = diff.toFloat() / (24 * 60 * 60 * 1000)

        return "" + dayCount.toInt()
    }


}