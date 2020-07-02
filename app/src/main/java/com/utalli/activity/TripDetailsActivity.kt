package com.utalli.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.utalli.R
import com.utalli.adapter.StateListAdapter
import com.utalli.adapter.TripDetailsCityListToVisitAdapter
import com.utalli.adapter.TripDetailsCityListAdapter
import com.utalli.callBack.StateNotToVisitCallBack
import com.utalli.callBack.TripDetailsStateListCallBack
import com.utalli.helpers.AppConstants
import com.utalli.helpers.AppPreference
import com.utalli.helpers.Utils
import com.utalli.models.*
import com.utalli.viewModels.TripDetailsViewModel
import kotlinx.android.synthetic.main.activity_trip_details.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TripDetailsActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener, TripDetailsStateListCallBack {


    var c = Calendar.getInstance()
    var year = c.get(Calendar.YEAR)
    var month = c.get(Calendar.MONTH)
    var day = c.get(Calendar.DAY_OF_MONTH)
    var countryName: String = ""
    var countryId: Int? = null
    var cityList = ArrayList<IndividualCityDetail>()


    var subCityDataList = ArrayList<SubCityData>()
    var individualCityDetail = ArrayList<IndividualCityDetail>()


    var remainingCityList = ArrayList<IndividualCityDetail>()
    var visibleCityList = ArrayList<IndividualCityDetail>()
    var itemAddedCount = 0


    var userSelectedCityList = ArrayList<IndividualCityDetail>()
    var tripDetailsCityListAdapter: TripDetailsCityListAdapter? = null
    var selectedCityAdapter: TripDetailsCityListToVisitAdapter? = null

    var arrivalDateStr: String? = null

    var ddArrivalDate: Date? = null
    var ddDepartureDate: Date? = null
    var departureDateStr: String? = null

    var getCityByCountryIdViewModel: TripDetailsViewModel? = null
    var selectedCountry: LocationSearchDataItems? = null

    var isComingFrom : Int =0
    var guideLocation: ArrayList<GuideStateListModel>? = null


    lateinit var mStateList: ArrayList<StateListData>   // add new 29June
    var mStateAdapter: StateListAdapter? = null    // add new 29June
    var selectedStateData: StateListData?= null  // new add on 29 june
    var selectedStatePosition: Int = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)

        toolbar.title = ""
        toolbar.setNavigationIcon(R.drawable.arrow_back_white)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
/*

        countryName = intent.getStringExtra("countryName")
        countryId = intent.getIntExtra("countryId", 0)
*/

        getCityByCountryIdViewModel = ViewModelProviders.of(this).get(TripDetailsViewModel::class.java)
        isComingFrom = intent.getIntExtra("IsComingFrom",0)
        if (isComingFrom == AppConstants.GUIDE_PROFILE_SEE_FROM_HOME)
        {
            var guideLocation: ArrayList<GuideStateListModel>  = intent.getParcelableArrayListExtra("GuideStateList")
            countryName = AppPreference.getInstance(this).getCountryName();
            tv_change.visibility = View.GONE
            setUpGuideStateListUI(guideLocation)
        }
        else if (isComingFrom == AppConstants.GUIDE_PROFILE_SEE_FROM_PLACE_SEARCH)
        {
            selectedCountry = intent.getParcelableExtra("selectedCountry")

            countryName = selectedCountry!!.name
            countryId = selectedCountry!!.id
            tv_change.visibility = View.VISIBLE
            getStateData(countryId!!)
           // getData(countryId!!,)
        }


        initViews()
        initialiseStateSpinner()
    }


    private fun initViews() {

        button_confirm.setOnClickListener(this)
        tv_date_of_arrival.setOnClickListener(this)
        tv_date_of_departure.setOnClickListener(this)
        tv_arrival_date_change.setOnClickListener(this)
        tv_departure_date_change.setOnClickListener(this)
        tv_change.setOnClickListener(this)
        tv_view_all.setOnClickListener(this)


        if (tv_date_of_arrival.text.toString().equals("")) {
            tv_arrival_date_change.visibility = View.GONE
        } else {
            tv_arrival_date_change.visibility = View.VISIBLE
        }

        if (tv_date_of_departure.text.toString().equals("")) {
            tv_departure_date_change.visibility = View.GONE
        } else {
            tv_departure_date_change.visibility = View.VISIBLE
        }
    }

    private fun getStateData(countryId: Int) {

        if(Utils.isInternetAvailable(this))
        {

            getCityByCountryIdViewModel!!.getStateData(this, countryId).observe(this, androidx.lifecycle.Observer {
                if (it != null && it.has("status") && it.get("status").asString.equals("1"))
                {
                    if (it.has("data") && it.get("data") is JsonObject) {
                        var dataObject = it.getAsJsonObject("data")
                        if (dataObject != null)
                        {
                            val type = object : TypeToken<List<StateListData>>() {}.type
                            var stateList = Gson().fromJson<List<StateListData>>(dataObject.get("states"), type)
                            if (stateList != null)
                            {
                                mStateList.clear()
                                var state = StateListData()
                                state.setId(-1)
                                state.setName("Select state")
                                mStateList.add(0, state)
                                mStateList.addAll(stateList)
                                mStateAdapter?.notifyDataSetChanged()
                            }
                            else
                            {

                            }
                        }
                    }
                }
                else {
                    if(it != null && it.has("message")){
                        Utils.showToast(this,it.get("message").asString)
                        Log.e("TAG","message status 0 TripDetails  === "+it.get("message").asString)
                    }
                }
            })
        }
        else
        {
            Utils.showToast(this, getString(R.string.msg_no_internet))
        }


    }


    private fun initialiseStateSpinner(){
        mStateList = ArrayList<StateListData>()
        var state = StateListData()
        state.setName("Select state")
        state.setId(-1)
        mStateList.add(state)
        mStateAdapter = StateListAdapter(this, mStateList)
        spinner_chooseState.adapter = mStateAdapter
        spinner_chooseState.onItemSelectedListener = this
    }



    private fun setUpGuideStateListUI(guideLocation: ArrayList<GuideStateListModel>)
    {
        if (guideLocation != null && guideLocation.size > 0)
        {
            for (item in guideLocation) {
                var individualCityList = IndividualCityDetail(item.getCityId()!!, item.getCityname()!!, item.getCountryId()!!, item.getStateId()!!, item.getStatename()!!, false)
                individualCityDetail.add(individualCityList)
            }

            if (individualCityDetail != null && individualCityDetail.size > 0)
            {
                spinner_chooseState.visibility = View.GONE
                iv_state_dropDown.visibility = View.GONE
                tv_chooseState.visibility = View.VISIBLE
                tv_chooseState.text = individualCityDetail.get(0).stateName
                selectedStatePosition = individualCityDetail.get(0).states_id
              //  getStateData(individualCityDetail.get(0).country_id)
                setupAdapter()
            }
        }
        else
        {
            Utils.showToast(this, getString(R.string.guide_list_not_available))
        }
    }


    private fun getData(countryId: Int, stateId: Int) {

        if(checkValidation()){

            getCityByCountryIdViewModel!!.getCityData(this, countryId, stateId).observe(this, androidx.lifecycle.Observer {

                Utils.hideProgressDialog()
                if (it != null && it.has("status") && it.get("status").asString.equals("1")) {

                    if (it.has("data")) {

                        var dataObj = it.getAsJsonObject("data")

                        if (dataObj.has("cities")) {
                            individualCityDetail.clear()
                            var cityDataArryList = object : TypeToken<ArrayList<IndividualCityDetail>>() {}.type
                            individualCityDetail.addAll(Gson().fromJson<ArrayList<IndividualCityDetail>>(dataObj.get("cities").toString(),cityDataArryList))
                            Log.e("TAG", "individualCityDetail.size() ====  " + individualCityDetail.size)
                        }
                        setupAdapter()
                    }

                } else {
                    Utils.showToast(this, getString(R.string.msg_common_error))
                }
            })
        }


    }


    private fun checkValidation(): Boolean {
        if (!Utils.isInternetAvailable(this)) {
            Utils.showToast(this, resources.getString(R.string.msg_no_internet))
            return false
        }
        return true
    }

    private fun setupAdapter()
    {
        tv_selected_country_name.text = countryName

        if (individualCityDetail.size > 5) {
            tv_view_all.visibility = View.VISIBLE
        } else {
            tv_view_all.visibility = View.GONE
        }

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.SPACE_AROUND
        rv_cities_list.layoutManager = layoutManager

        tripDetailsCityListAdapter = TripDetailsCityListAdapter(this, this, "TripDetailsActivity")
        rv_cities_list.adapter = tripDetailsCityListAdapter

        if (individualCityDetail.size > 5) {
            visibleCityList = ArrayList(individualCityDetail.subList(0, 4))
            remainingCityList = ArrayList(individualCityDetail.subList(4, individualCityDetail.size))

        } else if (individualCityDetail.size < 5) {
            visibleCityList = individualCityDetail
            remainingCityList = ArrayList()
        }

        tripDetailsCityListAdapter?.setStateList(visibleCityList, this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_confirm -> {
                if (tv_date_of_arrival.text.toString().equals("")) {
                    Utils.showToast(this, getString(R.string.msg_please_choose_arrival_date))
                } else if (tv_date_of_departure.text.toString().equals("")) {
                    Utils.showToast(this, getString(R.string.msg_please_choose_departure_date))
                } else if (ddArrivalDate!!.compareTo(ddDepartureDate) > 0) {
                    Utils.showToast(this, getString(R.string.msg_please_choose_arrival_date_smaller_or_equal_to_departure_date))
                }
                else if (userSelectedCityList.size == 0)
                {
                    Utils.showToast(this, "Please select your city.")
                }
                else if(selectedStatePosition == 0){
                    Utils.showToast(this, "Please select your state.")
                }
                else {
                    if(isComingFrom == AppConstants.GUIDE_PROFILE_SEE_FROM_HOME)
                    {
                        var selectedCitiesId = ""
                        if (userSelectedCityList != null && userSelectedCityList.size > 0) {

                            for (i in 0..userSelectedCityList.size - 1) {

                                if (i == 0){
                                    selectedCitiesId = userSelectedCityList.get(i).id.toString()
                                }

                                else if (i <= userSelectedCityList.size - 1){
                                    selectedCitiesId = selectedCitiesId + "," + userSelectedCityList.get(i).id.toString()
                                }
                            }
                        }

                        var strArrivalDate = tv_date_of_arrival.text.toString()
                        var strDeptDate = tv_date_of_departure.text.toString()


                        try {
                            val sdfSource = SimpleDateFormat("MM/dd/yyyy")
                            val date = sdfSource.parse(strArrivalDate)
                            val sdfDestination = SimpleDateFormat("dd/MM/yyyy")
                            strArrivalDate = sdfDestination.format(date)
                        } catch (pe: ParseException) {
                            println("Parse Exception : $pe")
                        }

                        try {
                            val sdfSource = SimpleDateFormat("MM/dd/yyyy")
                            val date = sdfSource.parse(strDeptDate)
                            val sdfDestination = SimpleDateFormat("dd/MM/yyyy")
                            strDeptDate = sdfDestination.format(date)
                        } catch (pe: ParseException) {
                            println("Parse Exception : $pe")
                        }


                        var returnIntent = Intent()
                        returnIntent.putExtra("selectedCitiesId", selectedCitiesId)  // "selectedStatesId"
                        returnIntent.putExtra("tourStartDate", strArrivalDate)
                        returnIntent.putExtra("tourEndDate", strDeptDate)
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                    else if(isComingFrom == AppConstants.GUIDE_PROFILE_SEE_FROM_PLACE_SEARCH)
                    {
                        var strArrivalDate = tv_date_of_arrival.text.toString()
                        var strDeptDate = tv_date_of_departure.text.toString()

                        try {
                            val sdfSource = SimpleDateFormat("MM/dd/yyyy")
                            val date = sdfSource.parse(strArrivalDate)
                            val sdfDestination = SimpleDateFormat("dd/MM/yyyy")
                            strArrivalDate = sdfDestination.format(date)
                        } catch (pe: ParseException) {
                            println("Parse Exception : $pe")
                        }

                        try {
                            val sdfSource = SimpleDateFormat("MM/dd/yyyy")
                            val date = sdfSource.parse(strDeptDate)
                            val sdfDestination = SimpleDateFormat("dd/MM/yyyy")
                            strDeptDate = sdfDestination.format(date)
                        } catch (pe: ParseException) {
                            println("Parse Exception : $pe")
                        }


                        var intent = Intent(this@TripDetailsActivity, GuideListActivity::class.java)

                        intent.putExtra("selectedCountry", selectedCountry)
                        intent.putExtra("selectedStateName", selectedStateData!!.getName())
                        intent.putExtra("selectedStateId",selectedStateData!!.getId())
                        intent.putExtra("selectedCities", userSelectedCityList)
                        intent.putExtra("tourStartDate", strArrivalDate)
                        intent.putExtra("tourEndDate", strDeptDate)
                        startActivity(intent)
                    }
                }
            }
            R.id.tv_change -> {
                var intent = Intent(this@TripDetailsActivity, SearchActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.iv_back_arrow -> {
                finish()
            }
            R.id.tv_date_of_arrival -> {

                val datePickerDialog = DatePickerDialog(
                    this,
                    R.style.DialogTheme,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                     //   tv_date_of_arrival.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)

                        tv_date_of_arrival.setText("" +(monthOfYear + 1) + "/" + dayOfMonth + "/" + year)

                        arrivalDateStr = ("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)

                        var sdf = SimpleDateFormat("dd/MM/yyyy")
                        ddArrivalDate = sdf.parse(arrivalDateStr)

                        if (tv_date_of_arrival.text.toString().equals("")) {
                            tv_arrival_date_change.visibility = View.GONE
                        } else {
                            tv_arrival_date_change.visibility = View.VISIBLE  //tv_arrival_date_change
                            button_confirm.setBackgroundResource(R.drawable.rounded_rect_blue)
                        }

                    }, year, month, day)

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)  // disable the previous date from the calendar
                datePickerDialog.show()

            }

            R.id.tv_date_of_departure -> {

                if (tv_date_of_arrival.text.toString().equals("")) {
                    Toast.makeText(this, getString(R.string.msg_please_choose_arrival_date_first), Toast.LENGTH_SHORT).show()
                } else {

                    val datePickerDialog = DatePickerDialog(
                        this,
                        R.style.DialogTheme,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                            tv_date_of_departure.setText("" + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year)
                           // tv_date_of_departure.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)

                            departureDateStr = ("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
                            var sdf = SimpleDateFormat("dd/MM/yyyy")
                            ddDepartureDate = sdf.parse(departureDateStr)


                            if (tv_date_of_departure.text.toString().equals("")) {
                                tv_departure_date_change.visibility = View.GONE
                            } else {
                                tv_departure_date_change.visibility = View.VISIBLE
                                button_confirm.setBackgroundResource(R.drawable.rounded_rect_blue)
                            }
                        }, year, month, day)
                    datePickerDialog.getDatePicker().setMinDate(ddArrivalDate!!.time)
                    datePickerDialog.show()

                }


            }

            R.id.tv_departure_date_change -> {

                if (tv_date_of_arrival.text.toString().equals("")) {
                    Toast.makeText(this, getString(R.string.msg_please_choose_arrival_date_first), Toast.LENGTH_SHORT).show()
                } else {

                    val datePickerDialog = DatePickerDialog(this, R.style.DialogTheme,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                            tv_date_of_departure.setText("" + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year)
                            //tv_date_of_departure.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)

                            departureDateStr = ("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
                            var sdf = SimpleDateFormat("dd/MM/yyyy")
                            ddDepartureDate = sdf.parse(departureDateStr)

                            Log.e("Tag", "deptDate === " + arrivalDateStr)
                        }, year, month, day)

                    datePickerDialog.getDatePicker().setMinDate(ddArrivalDate!!.time)
                    datePickerDialog.show()
                }
            }

            R.id.tv_arrival_date_change -> {
                val datePickerDialog = DatePickerDialog(
                    this,
                    R.style.DialogTheme,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                        tv_date_of_arrival.setText("" + (monthOfYear + 1) + "/" + dayOfMonth + "/" + year)
                        //tv_date_of_arrival.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)

                        arrivalDateStr = ("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)

                        var sdf = SimpleDateFormat("dd/MM/yyyy")
                        ddArrivalDate = sdf.parse(arrivalDateStr)

                    }, year, month, day)

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
                datePickerDialog.show()
            }
            R.id.tv_view_all -> {
                var intent = Intent(this, ViewAllStateActivity::class.java)
                intent.putExtra("countryName", countryName)
                // remainingCityList.addAll(visibleCityList)
                intent.putExtra("cityDetailsList", remainingCityList)
                Log.e("TAG", "TripDetailsActivity remainingCityList send to viewAll == " + remainingCityList.size)
                startActivityForResult(intent, 101)
            }

        }

    }


    override fun recyclerViewListClicked(itemDetails: IndividualCityDetail) {

        if (selectedCityAdapter == null) {
            userSelectedCityList.add(itemDetails)

            val layoutManager = FlexboxLayoutManager(this)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.SPACE_AROUND
            rv_states_u_want_visit.layoutManager = layoutManager


            // data of deleted , state_you_want_to_visit and add items in stateList
            selectedCityAdapter = TripDetailsCityListToVisitAdapter(this, object : StateNotToVisitCallBack {
                override fun stateNotToVisitCallBack(itemDetails: IndividualCityDetail) {

                    if (visibleCityList.size < 4) {
                        visibleCityList.add(itemDetails)
                        tripDetailsCityListAdapter!!.notifyItemInserted(visibleCityList.size - 1)
                    } else {
                        remainingCityList.add(itemDetails)
                    }

                    if (remainingCityList.size == 0) {
                        tv_view_all.visibility = View.GONE
                    } else {
                        tv_view_all.visibility = View.VISIBLE
                    }

                    userSelectedCityList.remove(itemDetails)
                    selectedCityAdapter!!.notifyDataSetChanged()

                    Log.e("TAG", "userSelectedCityList size === " + userSelectedCityList.size)
                    if (userSelectedCityList.size > 0) {
                        view1.visibility = View.VISIBLE
                        tv_states_you_want_to_visit.visibility = View.VISIBLE
                        rv_states_u_want_visit.visibility = View.VISIBLE
                    } else {
                        view1.visibility = View.GONE
                        tv_states_you_want_to_visit.visibility = View.GONE
                        rv_states_u_want_visit.visibility = View.GONE
                    }

                    if (userSelectedCityList.size.equals(individualCityDetail.size)) {
                        view2.visibility = View.GONE
                        tv_cities_in_country.visibility = View.GONE
                        rv_cities_list.visibility = View.GONE
                    } else {
                        view2.visibility = View.VISIBLE
                        tv_cities_in_country.visibility = View.VISIBLE
                        rv_cities_list.visibility = View.VISIBLE
                    }


                }
            })

            rv_states_u_want_visit.adapter = selectedCityAdapter
            selectedCityAdapter?.setSelectedStateList(userSelectedCityList, this)

            if (userSelectedCityList.size.equals(individualCityDetail.size)) {
                view2.visibility = View.GONE
                tv_cities_in_country.visibility = View.GONE
                rv_cities_list.visibility = View.GONE
            } else {
                view2.visibility = View.VISIBLE
                tv_cities_in_country.visibility = View.VISIBLE
                rv_cities_list.visibility = View.VISIBLE
            }


        } else {
            userSelectedCityList.add(itemDetails)
            selectedCityAdapter!!.notifyItemInserted(userSelectedCityList.size - 1)

            if (userSelectedCityList.size.equals(individualCityDetail.size)) {
                view2.visibility = View.GONE
                tv_cities_in_country.visibility = View.GONE
                rv_cities_list.visibility = View.GONE
            } else {
                view2.visibility = View.VISIBLE
                tv_cities_in_country.visibility = View.VISIBLE
                rv_cities_list.visibility = View.VISIBLE
            }

        }


        if (userSelectedCityList.size > 0) {
            view1.visibility = View.VISIBLE
            tv_states_you_want_to_visit.visibility = View.VISIBLE
            rv_states_u_want_visit.visibility = View.VISIBLE
        } else {
            view1.visibility = View.GONE
            tv_states_you_want_to_visit.visibility = View.GONE
            rv_states_u_want_visit.visibility = View.GONE
        }




        if (remainingCityList.size == 0) {
            tv_view_all.visibility = View.GONE
        } else {
            tv_view_all.visibility = View.VISIBLE
        }


        if (remainingCityList.size > 0) {
            visibleCityList.add(remainingCityList.get(0))
            remainingCityList.removeAt(0)
            tripDetailsCityListAdapter!!.notifyItemInserted(visibleCityList.size - 1)

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // view all
        if (data != null && requestCode == 101) {

            var selectedCitiesListNewViewAll = data.getSerializableExtra("selectedCities") as ArrayList<IndividualCityDetail>


            if (selectedCityAdapter == null) {
                userSelectedCityList.addAll(selectedCitiesListNewViewAll)
                val layoutManager = FlexboxLayoutManager(this)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.SPACE_AROUND
                rv_states_u_want_visit.layoutManager = layoutManager

                // data of deleted , state_you_want_to_visit and add items in stateList
                selectedCityAdapter = TripDetailsCityListToVisitAdapter(this, object : StateNotToVisitCallBack {
                    override fun stateNotToVisitCallBack(itemDetails: IndividualCityDetail) {

                        ////////////////////////////////////
                        userSelectedCityList.remove(itemDetails)
                        selectedCityAdapter!!.notifyDataSetChanged()
                        //////////////////////////////////////////////

                        if (userSelectedCityList.size > 0) {
                            view1.visibility = View.VISIBLE
                            tv_states_you_want_to_visit.visibility = View.VISIBLE
                            rv_states_u_want_visit.visibility = View.VISIBLE
                        } else {
                            view1.visibility = View.GONE
                            tv_states_you_want_to_visit.visibility = View.GONE
                            rv_states_u_want_visit.visibility = View.GONE
                        }
                    }
                })

                rv_states_u_want_visit.adapter = selectedCityAdapter
                selectedCityAdapter?.setSelectedStateList(userSelectedCityList, this)

            } else {
                userSelectedCityList.addAll(selectedCitiesListNewViewAll)
                selectedCityAdapter!!.notifyItemInserted(userSelectedCityList.size - 1)


            }

            if (userSelectedCityList.size > 0) {
                view1.visibility = View.VISIBLE
                tv_states_you_want_to_visit.visibility = View.VISIBLE
                rv_states_u_want_visit.visibility = View.VISIBLE
            } else {
                view1.visibility = View.GONE
                tv_states_you_want_to_visit.visibility = View.GONE
                rv_states_u_want_visit.visibility = View.GONE
            }

        }
    }




    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        when(p0?.id){

            R.id.spinner_chooseState ->
            {
                selectedStatePosition = p2
                if(p2 != 0){
                    selectedStateData = mStateList.get(p2)
                    tv_states_you_want_to_visit.visibility = View.GONE
                    rv_states_u_want_visit.visibility = View.GONE
                    userSelectedCityList.clear()
                    getData(countryId!!, selectedStateData!!.getId()!!)
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }



}