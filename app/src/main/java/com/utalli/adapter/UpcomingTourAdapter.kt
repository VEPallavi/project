package com.utalli.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.utalli.R
import com.utalli.activity.PayNowActivity
import com.utalli.callBack.UpcomingTourCancelCallBack
import com.utalli.models.UpcomingTourListModel
import org.w3c.dom.Text
import java.text.ParseException
import java.text.SimpleDateFormat


class UpcomingTourAdapter(var mcontext: Context, var upComingTourList: List<UpcomingTourListModel>, var itemListener: UpcomingTourCancelCallBack) : RecyclerView.Adapter<UpcomingTourAdapter.MyToursViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyToursViewHolder {
        return MyToursViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_upcoming_my_tours, parent, false))
    }



    override fun getItemCount(): Int {
        return upComingTourList.size
    }



    override fun onBindViewHolder(holder: MyToursViewHolder, position: Int) {

        holder.tv_tour_charges.setText("$ "+upComingTourList.get(position).tourPrice)


        if(upComingTourList.get(position).requesttype.equals("1")){
            holder.tv_tourType.setText("Private")
        }else if(upComingTourList.get(position).requesttype.equals("2")){
            holder.tv_tourType.setText("Pool")
        }


        var strArrivalDate = upComingTourList.get(position).startdate
        var strDeptDate = upComingTourList.get(position).enddate


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




        ///////////////////////////////////////////////
        // requeststatus = 2-accepted , 3-rejected, 1- not accepted by the guide,


        if(upComingTourList.get(position).requeststatus == 2)
        {


             // payment part of upcoming tour
            if (upComingTourList.get(position).payment == null || upComingTourList.get(position).payment.paymentStatus.equals("0"))
            {
                holder.tv_pay_now.isClickable = true
                holder.tv_pay_now.setTextColor(mcontext.getResources().getColor(R.color.colorBlack))
                holder.tv_pay_now.setText("Pay now")
                holder.tv_tour_status.setText("Accepted by the guide, waiting for the payment")
                holder.tv_guide_status.setText("Accepted by the guide, waiting for the payment")

                holder.tv_cancel.isClickable = true
                holder.tv_cancel.isFocusable = true

                holder.tv_pay_now.setOnClickListener {
                    itemListener.upComingTourListener(upComingTourList.get(position), "PAY_NOW")
                }

                holder.tv_cancel.setOnClickListener {
                    itemListener.upComingTourListener(upComingTourList.get(position), "CANCEL")
                }
            }
            else if(upComingTourList.get(position).payment != null && upComingTourList.get(position).payment.paymentStatus.equals("1"))
            {
                holder.tv_pay_now.setText("Paid")
                holder.tv_pay_now.isClickable = false
                holder.tv_cancel.isClickable = false
                holder.tv_cancel.isFocusable = false
                holder.tv_cancel.setTextColor(mcontext.getResources().getColor(R.color.grey_6))
                holder.tv_pay_now.setTextColor(mcontext.getResources().getColor(R.color.colorBlack))
                holder.tv_tour_status.setText("You are all set, enjoy the tour!")
                holder.tv_guide_status.setText("You are all set, enjoy the tour!")
            }

        }

        else if(upComingTourList.get(position).requeststatus == 3 || upComingTourList.get(position).requeststatus == 4 || upComingTourList.get(position).requeststatus == 5)
        {
            holder.tv_pay_now.isClickable = false
            holder.tv_pay_now.setText("Pay now")
            holder.tv_pay_now.setTextColor(mcontext.getResources().getColor(R.color.grey_6))
            holder.tv_tour_status.setText("Rejected by the guide")
            holder.tv_guide_status.setText("Rejected by the guide")

           /* holder.tv_cancel.setOnClickListener {
                itemListener.upComingTourListener(upComingTourList.get(position), "CANCEL")
            }*/
        }

        else if(upComingTourList.get(position).requeststatus == 1){
            holder.tv_pay_now.isClickable = false
            holder.tv_cancel.isFocusable = true
            holder.tv_pay_now.setTextColor(mcontext.getResources().getColor(R.color.grey_6))
            holder.tv_pay_now.setText("Pay now")
            holder.tv_tour_status.setText("Waiting by the guide, to accept the request")
            holder.tv_guide_status.setText("Waiting by the guide, to accept the request")
            holder.tv_cancel.setTextColor(mcontext.getResources().getColor(R.color.colorBlack))

            holder.tv_cancel.setOnClickListener {
                itemListener.upComingTourListener(upComingTourList.get(position), "CANCEL")
            }
        }



        ////////////////////////////////////////////////

        holder.tv_tour_dates.setText(strArrivalDate + " - "+ strDeptDate)

    }



    inner class MyToursViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tv_tour_charges : TextView
        var tv_tour_dates : TextView
        var tv_cancel : TextView
        var tv_pay_now : TextView
        var tv_tourType : TextView
        var tv_tour_status : TextView
        var tv_guide_status : TextView

        init {
            tv_tour_charges = view.findViewById(R.id.tv_tour_charges)
            tv_tour_dates = view.findViewById(R.id.tv_tour_dates)
            tv_cancel = view.findViewById(R.id.tv_cancel)
            tv_pay_now = view.findViewById(R.id.tv_pay_now)
            tv_tourType = view.findViewById(R.id.tv_tourType)
            tv_tour_status = view.findViewById(R.id.tv_tour_status)
            tv_guide_status = view.findViewById(R.id.tv_guide_status)
        }




    }



}