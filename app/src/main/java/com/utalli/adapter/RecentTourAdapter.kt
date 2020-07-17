package com.utalli.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.utalli.R
import com.utalli.models.RecentTourListModel
import com.utalli.models.UpcomingTourListModel
import java.text.ParseException
import java.text.SimpleDateFormat

class RecentTourAdapter(var mcontext: Context, var recentComingTourList: List<RecentTourListModel>) :
    RecyclerView.Adapter<RecentTourAdapter.MyRecentTourViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentTourAdapter.MyRecentTourViewHolder {
        return MyRecentTourViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_recent_my_tours,
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {
        return recentComingTourList.size
    }


    override fun onBindViewHolder(holder: RecentTourAdapter.MyRecentTourViewHolder, position: Int) {

        var strArrivalDate = recentComingTourList.get(position).startdate
        var strDeptDate = recentComingTourList.get(position).enddate


        try {
            val sdfSource = SimpleDateFormat("yyyy-MM-dd")
            val date = sdfSource.parse(strArrivalDate)
            val sdfDestination = SimpleDateFormat("MMM dd, yyyy")
            strArrivalDate = sdfDestination.format(date)
        } catch (pe: ParseException) {
            println("Parse Exception : $pe")
        }


        try {
            val sdfSource = SimpleDateFormat("yyyy-MM-dd")
            val date = sdfSource.parse(strDeptDate)
            val sdfDestination = SimpleDateFormat("MMM dd, yyyy")
            strDeptDate = sdfDestination.format(date)
        } catch (pe: ParseException) {
            println("Parse Exception : $pe")
        }


        holder.tv_tour_states.text = recentComingTourList.get(position).cityNames


        holder.tv_tourDate.text = strArrivalDate + " - " + strDeptDate


        if (recentComingTourList.get(position).requesttype.equals("1")) {
            holder.tv_tour_charges.text = "Private-" + "$" + recentComingTourList.get(position).tourPrice
        } else if (recentComingTourList.get(position).requesttype.equals("2")) {
            holder.tv_tour_charges.text = "Pool-" + "$" + recentComingTourList.get(position).tourPrice
        }


    }

    inner class MyRecentTourViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tv_tour_states: TextView
        var tv_tour_charges: TextView
        var tv_tourDate: TextView

        init {
            tv_tour_states = view.findViewById(R.id.tv_tour_states)
            tv_tour_charges = view.findViewById(R.id.tv_tour_charges)
            tv_tourDate = view.findViewById(R.id.tv_tourDate)
        }

    }

}