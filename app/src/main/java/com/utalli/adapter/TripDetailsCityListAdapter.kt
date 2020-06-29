package com.utalli.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.utalli.R
import com.utalli.callBack.TripDetailsStateListCallBack
import com.utalli.models.IndividualCityDetail


class TripDetailsCityListAdapter(var mcontext: Context, var itemListener: TripDetailsStateListCallBack, var screenType: String) : RecyclerView.Adapter<TripDetailsCityListAdapter.StateListViewHolder>() {

    var stateList = ArrayList<IndividualCityDetail>()



    fun setStateList(newStateList: ArrayList<IndividualCityDetail>, mcontext: Context) {
        this.stateList = newStateList
        this.mcontext = mcontext
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripDetailsCityListAdapter.StateListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_list_states, parent, false)
        return StateListViewHolder(view)
    }


    override fun getItemCount(): Int {
        return stateList.size
    }


    override fun onBindViewHolder(holder: TripDetailsCityListAdapter.StateListViewHolder, position: Int) {

        holder.iv_icon.setBackgroundResource(R.drawable.ic_report_black_24dp)

        holder.tv_state_name.setText(stateList[position].name)

        if (screenType.equals("TripDetailsActivity")) {
            holder.layoutParent.setOnClickListener {
                if (holder.layoutParent.isSelected) {
                    holder.layoutParent.setBackgroundResource(R.drawable.selected_round_rect)
                }
                itemListener?.recyclerViewListClicked(stateList.get(position))
                stateList.remove(stateList.get(position))
                notifyDataSetChanged()
            }

        }
        else if (screenType.equals("ViewAllStateActivity")) {
            holder.bind()
        }

    }


    inner class StateListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_state_name: TextView
        var layoutParent: LinearLayout
        var iv_icon: ImageView

        init {
            tv_state_name = itemView.findViewById(R.id.tv_state_name)
            layoutParent = itemView.findViewById(R.id.layout_parent)
            iv_icon = itemView.findViewById(R.id.iv_icon)
        }


        fun bind() {
            if (stateList.get(position).isSelected) {
                layoutParent.setBackgroundResource(R.drawable.selected_round_rect)
                iv_icon.setBackgroundResource(R.drawable.close_icon)

            } else {
                layoutParent.setBackgroundResource(R.drawable.unselected_round_rect)
                iv_icon.setBackgroundResource(R.drawable.ic_report_black_24dp)

            }


            layoutParent.setOnClickListener {
                stateList.get(position).isSelected = !stateList.get(position).isSelected
                itemListener?.recyclerViewListClicked(stateList.get(position))
                notifyItemChanged(position)
                //stateListAdd.remove(stateListAdd.get(position))
            }
        }


    }


}