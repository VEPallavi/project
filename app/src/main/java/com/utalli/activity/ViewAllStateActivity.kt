package com.utalli.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.utalli.R
import com.utalli.adapter.TripDetailsCityListAdapter
import com.utalli.callBack.TripDetailsStateListCallBack
import com.utalli.models.IndividualCityDetail
import kotlinx.android.synthetic.main.activity_view_all_state.*


class ViewAllStateActivity : AppCompatActivity(), View.OnClickListener {
    var tripDetailsStateListAdapter: TripDetailsCityListAdapter? = null
    var allCityList = ArrayList<IndividualCityDetail>()
    var selectedCityList = ArrayList<IndividualCityDetail>()
    var countryName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_state)


        toolbarr.setNavigationIcon(R.drawable.arrow_back_white)
        setSupportActionBar(toolbarr)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbarr.setNavigationOnClickListener {
            finish()
        }

        countryName = intent.getStringExtra("countryName")
        allCityList = getIntent().getSerializableExtra("cityDetailsList") as ArrayList<IndividualCityDetail>

        Log.e("TAG", "ViewAllActivity allCityList send to viewAll == "+allCityList.size)
        toolbarr.title = ""
        toolbarr_title.setText(countryName)
      //  Log.e("TAG", "countryName view All === " + countryName)


        initViews()

    }

    private fun initViews() {
        btn_add.setOnClickListener(this)

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.SPACE_AROUND
        recyclerView_allState.layoutManager = layoutManager

        tripDetailsStateListAdapter = TripDetailsCityListAdapter(this, object : TripDetailsStateListCallBack {
            override fun recyclerViewListClicked(itemDetails: IndividualCityDetail) {
                if (itemDetails.isSelected){
                    selectedCityList.add(itemDetails)
                }
                else{
                    selectedCityList.remove(itemDetails)
                }
            }

        }, "ViewAllStateActivity")
        recyclerView_allState.adapter = tripDetailsStateListAdapter
        tripDetailsStateListAdapter?.setStateList(allCityList, this)


    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add -> {
                var i = Intent()
                i.putExtra("selectedCities", selectedCityList)
                setResult(101, i)
                finish()
            }
        }


    }

}