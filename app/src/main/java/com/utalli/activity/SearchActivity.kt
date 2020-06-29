package com.utalli.activity

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Filter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.utalli.R
import com.utalli.adapter.SearchPlaceAdapter
import kotlinx.android.synthetic.main.activity_search.*
import android.content.Intent
import androidx.core.app.ShareCompat.IntentBuilder
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.utalli.helpers.Utils
import com.utalli.models.LocationSearchDataItems
import com.utalli.viewModels.SearchLocationViewModel
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList


class SearchActivity : AppCompatActivity(), View.OnClickListener {
    var searchPlaceAdapter: SearchPlaceAdapter? = null
    var searchLocationViewModels: SearchLocationViewModel? = null
    var locationSearchDataItems = ArrayList<LocationSearchDataItems>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initViews()

    }

    private fun initViews() {

        searchLocationViewModels = ViewModelProviders.of(this).get(SearchLocationViewModel::class.java)

        iv_back.setOnClickListener(this)

        iv_cancel.setOnClickListener(this)

        rv_languageList.setHasFixedSize(true)
        rv_languageList.layoutManager = LinearLayoutManager(this)
        searchPlaceAdapter = SearchPlaceAdapter(this, locationSearchDataItems!!)
        rv_languageList.adapter = searchPlaceAdapter

        et_search.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
                if (s.toString().trim { it <= ' ' }.length > 2) {
                    rv_languageList.visibility = View.VISIBLE
                    iv_cancel.visibility = View.VISIBLE
                    getSearchDetails(s.toString())
                } else {
                    rv_languageList.visibility = View.GONE
                    iv_cancel.visibility = View.GONE
                    cl_no_data_found.visibility=View.GONE
                    /*runOnUiThread(Runnable {
                        if (locationSearchDataItems != null) {
                            locationSearchDataItems!!.clear()
                            searchPlaceAdapter!!.notifyDataSetChanged()
                        }
                    })*/
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })


    }


    fun getSearchDetails(searchData: String) {

        if(checkValidation()){
            pb_loader.visibility = View.VISIBLE
            rv_languageList.visibility = View.INVISIBLE
            cl_no_data_found.visibility = View.INVISIBLE

            searchLocationViewModels!!.searchLocation(this, searchData).observe(this, androidx.lifecycle.Observer {

                if (it != null && it.has("status") && it.get("status").asString.equals("1")) {
                    rv_languageList.visibility = View.VISIBLE
                    pb_loader.visibility = View.GONE
                    cl_no_data_found.visibility = View.INVISIBLE
                    if (it.has("data") && it.get("data") is JsonArray) {

                        locationSearchDataItems = ArrayList()

                        val dataSearch = object : TypeToken<ArrayList<LocationSearchDataItems>>() {}.type
                        locationSearchDataItems.addAll(Gson().fromJson<ArrayList<LocationSearchDataItems>>(it.get("data").toString(), dataSearch))
                        searchPlaceAdapter = SearchPlaceAdapter(this, locationSearchDataItems!!)
                        rv_languageList.adapter = searchPlaceAdapter
                    }

                } else {
                    pb_loader.visibility = View.GONE

                    cl_no_data_found.visibility = View.VISIBLE
                    rv_languageList.visibility = View.INVISIBLE
                    // Utils.showToast(this, getString(R.string.msg_common_error))
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


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> finish()
            R.id.iv_cancel -> et_search.setText("")

        }
    }


}

