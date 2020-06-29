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
import com.utalli.callBack.StateNotToVisitCallBack
import com.utalli.models.IndividualCityDetail


class TripDetailsCityListToVisitAdapter (var mcontext : Context, var  itemListener: StateNotToVisitCallBack) : RecyclerView.Adapter<TripDetailsCityListToVisitAdapter.SelectedStateViewHolder>(){
    var selectedStateListAdd = ArrayList<IndividualCityDetail>()



    fun setSelectedStateList (newStateList : ArrayList<IndividualCityDetail>, mcontext: Context){
        this.selectedStateListAdd = newStateList
        this.mcontext = mcontext
        notifyDataSetChanged()

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedStateViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_list_state_you_want_to_visit,parent,false)
        return SelectedStateViewHolder(view)

    }

    override fun getItemCount(): Int {
         return  selectedStateListAdd.size
    }

    override fun onBindViewHolder(holder: SelectedStateViewHolder, position: Int) {
        holder.tv_state_name.setText(selectedStateListAdd.get(position).name)

        holder.ivCancleIcon.setOnClickListener {
            selectedStateListAdd.get(position).isSelected = false
            itemListener.stateNotToVisitCallBack(selectedStateListAdd.get(position))

        }

    }


    inner class SelectedStateViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var tv_state_name: TextView
        var layoutParent: LinearLayout
        var ivCancleIcon : ImageView

        init {
            tv_state_name=itemView.findViewById(R.id.tv_state_name)
            layoutParent = itemView.findViewById(R.id.layout_parent)
            ivCancleIcon = itemView.findViewById(R.id.iv_cancle_icon)
        }

    }


}