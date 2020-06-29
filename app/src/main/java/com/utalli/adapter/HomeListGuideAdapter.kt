package com.utalli.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.utalli.R
import com.utalli.RobotoRegularTextView
import com.utalli.models.Dashboard.DashboardArrayList
import com.utalli.models.Dashboard.GuidListData


class HomeListGuideAdapter(var mcontext: Context, var mHomeDashboardAdapterModel: DashboardArrayList?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var FAMOUS_LOC_TYPE = 0
    var GUIDE_TYPE = 1
    private var mClickListener: ListItemClickListener? = null

    interface ListItemClickListener {
        fun listItemClickListener(guidProfileData: GuidListData)
    }

    fun setItemClickListener(listener: ListItemClickListener) {
        this.mClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == FAMOUS_LOC_TYPE) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_famous_location, parent, false)
            return HomeListDestinationViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_row_list_guides, parent, false)
            return HomeListGuideViewHolder(v)
        }
    }


    override fun getItemCount(): Int {
        if (mHomeDashboardAdapterModel!!.getPopulardestinations()!!.size > 0) {
            return mHomeDashboardAdapterModel!!.getData()!!.size + 1
        } else {
            return mHomeDashboardAdapterModel!!.getData()!!.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return FAMOUS_LOC_TYPE
        } else {
            return GUIDE_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is HomeListDestinationViewHolder) {
            holder.tv_famousLocation.text = "Popular destinations"
            holder.bindLocationView()

        } else if (holder is HomeListGuideViewHolder) {
            var itemPosition = position

            if (mHomeDashboardAdapterModel!!.getPopulardestinations()!!.size > 0) {
                itemPosition = itemPosition - 1
            }

            var guidListData: GuidListData = mHomeDashboardAdapterModel!!.getData()!!.get(itemPosition)
            Glide.with(mcontext)
                .load(guidListData.getGuidProfileImg())
                .apply(RequestOptions().placeholder(R.mipmap.ic_profile_placeholder).error(R.mipmap.ic_profile_placeholder))
                .into(holder.civ_profile_pic)
            holder.tv_guide_name.text = guidListData.getName()
            holder.txt_ratingValue.text = guidListData.getGuiderating().toString()
            holder.tv_guide_charges.text = guidListData.getGuidePrivatePrice().toString()
            holder.tv_guide_charges_group.text = guidListData.getGuidePoolPrice().toString()
            holder.tv_languages.text = guidListData.getLang()

            holder.bindGuideView(guidListData)
        }
    }

    inner class HomeListGuideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var civ_profile_pic: ImageView
        var tv_guide_name: RobotoRegularTextView
        var txt_ratingValue: RobotoRegularTextView
        var tv_guide_charges: RobotoRegularTextView
        var tv_guide_charges_group: RobotoRegularTextView
        var tv_languages: RobotoRegularTextView

        init {
            civ_profile_pic = itemView.findViewById(R.id.civ_profile_pic)
            tv_guide_name = itemView.findViewById(R.id.tv_guide_name)
            txt_ratingValue = itemView.findViewById(R.id.txt_ratingValue)
            tv_guide_charges = itemView.findViewById(R.id.tv_guide_charges)
            tv_guide_charges_group = itemView.findViewById(R.id.tv_guide_charges_group)
            tv_languages = itemView.findViewById(R.id.tv_languages)
        }

        fun bindGuideView(guidListData: GuidListData) {
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    mClickListener!!.listItemClickListener(guidListData)
                }
            })
        }
    }


    inner class HomeListDestinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rvFamousLocations: RecyclerView
        var tv_famousLocation: RobotoRegularTextView

        init {
            rvFamousLocations = itemView.findViewById(R.id.rv_famousLocation)
            tv_famousLocation = itemView.findViewById(R.id.tv_famousLocation)

            rvFamousLocations.setHasFixedSize(true)

            var horizontalLayoutManager = LinearLayoutManager(mcontext)
            horizontalLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rvFamousLocations.layoutManager = horizontalLayoutManager
        }

        fun bindLocationView() {
            if (mHomeDashboardAdapterModel!!.getPopulardestinations()!!.size > 0) {
                rvFamousLocations.adapter =
                    HomeListDestinationAdapter(mcontext, mHomeDashboardAdapterModel!!.getPopulardestinations())
            }
        }
    }
}