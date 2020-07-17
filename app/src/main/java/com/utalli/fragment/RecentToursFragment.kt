package com.utalli.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import com.utalli.R
import com.utalli.adapter.RecentTourAdapter
import com.utalli.adapter.UpcomingTourAdapter
import com.utalli.helpers.Utils
import com.utalli.models.RecentTourListModel
import com.utalli.models.UpcomingTourListModel
import com.utalli.viewModels.MyToursViewModel
import kotlinx.android.synthetic.main.fragment_recent_tours.*
import kotlinx.android.synthetic.main.fragment_upcoming_tours.*

class RecentToursFragment : Fragment() {

    var myToursViewModel : MyToursViewModel?= null



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rv_recentTours.setHasFixedSize(true)
        rv_recentTours.layoutManager = LinearLayoutManager(activity!!)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recent_tours, container, false)
        myToursViewModel = ViewModelProviders.of(activity!!).get(MyToursViewModel::class.java)
        getRecentToursData()
        return view
    }

    private fun getRecentToursData() {
        myToursViewModel!!.getRecentTours(activity!!,2).observe(this, Observer {

            if(it != null && it.has("status") && it.get("status").asString.equals("1")){

                if(it.has("data") && it.get("data") is JsonArray){

                    val type = object : TypeToken<List<RecentTourListModel>>() {}.type
                    var recentComingTourList = Gson().fromJson<List<RecentTourListModel>>(it.get("data"), type)

                    
                    val stateType = object : TypeToken<List<String>>() {}.type
                     var recentTourStateList=Gson().fromJson<List<String>>(it.get("statesList"),stateType)


                        // comment by pallavi (17-07-2020) because response change
//                    for(i in 0..recentComingTourList.size-1)
//                    {
//                        recentComingTourList.get(i).stateName=recentTourStateList.get(i)
//
//                    }


                    if(recentComingTourList != null && recentComingTourList.size >0){

                        rv_recentTours.visibility = View.VISIBLE
                        cl_no_recentTourFound.visibility = View.GONE

                        rv_recentTours.adapter = RecentTourAdapter(activity!!, recentComingTourList)
                    }
                    else
                    {
                        cl_no_recentTourFound.visibility = View.VISIBLE
                        rv_recentTours.visibility = View.GONE
                    }
                }
                else {
                    if (it.has("message"))
                        Utils.showToast(activity!!, it.get("message").asString)
                    else {
                        Utils.showToast(activity!!, getString(R.string.msg_common_error))
                    }
                }
            }
        })
    }
}