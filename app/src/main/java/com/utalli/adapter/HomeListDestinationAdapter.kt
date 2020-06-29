package com.utalli.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.utalli.R
import com.utalli.RobotoRegularTextView
import com.utalli.models.Dashboard.PopulardestinationList
import kotlinx.android.synthetic.main.item_row_list_destination_home.view.*


class HomeListDestinationAdapter(var mcontext: Context, var popularDestinationList: List<PopulardestinationList>?) : RecyclerView.Adapter<HomeListDestinationAdapter.HomeListDestinationViewHolder>(){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListDestinationViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_row_list_destination_home, parent, false)
        return HomeListDestinationViewHolder(v)
    }


    override fun getItemCount(): Int {
       return popularDestinationList!!.size
    }

    override fun onBindViewHolder(holder: HomeListDestinationViewHolder, position: Int) {
        var popularLocation: PopulardestinationList = popularDestinationList!!.get(position)
        Glide.with(mcontext)
            .load(popularLocation.getTripImage())
            .apply(RequestOptions().placeholder(R.mipmap.ic_profile_placeholder).error(R.mipmap.ic_profile_placeholder))
            .into(holder.iv_place_pic)
        holder.tv_place_name.text = popularLocation.getLocationName()
//        holder.itemView.setOnClickListener {
//            var intent = Intent(mcontext,GuideProfileDetailsActivity::class.java)
//            mcontext.startActivity(intent)
//        }
    }



     inner class HomeListDestinationViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
         var iv_place_pic: ImageView
         var tv_place_name: RobotoRegularTextView

         init {
             iv_place_pic = itemView.findViewById(R.id.iv_place_pic)
             tv_place_name = itemView.findViewById(R.id.tv_place_name)
         }
    }
}