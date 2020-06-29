package com.utalli.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.utalli.R
import com.utalli.activity.GuideProfileDetailsActivity
import com.utalli.callBack.GuideListCallBack
import com.utalli.models.GuideListModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.my_profile_activity.*



class GuideListAdapter(var mcontext: Context, var guidesList: List<GuideListModel>, var itemListener : GuideListCallBack) :
    RecyclerView.Adapter<GuideListAdapter.GuideViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideListAdapter.GuideViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_list_guides, parent, false)
        return GuideViewHolder(view)
    }

    override fun getItemCount(): Int {
        return guidesList.size
    }

    override fun onBindViewHolder(holder: GuideListAdapter.GuideViewHolder, position: Int) {

        holder.bind(guidesList.get(position))

        holder.itemView.setOnClickListener {

            itemListener.guideListData(guidesList.get(position).guide_info)

          /*  var intent = Intent(mcontext, GuideProfileDetailsActivity::class.java)
            intent.putExtra("guideId", guidesList.get(position).guide_info.id.toInt())
            mcontext.startActivity(intent)*/

        }

    }


    inner class GuideViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvGuideName: TextView
        var tvRatingValue: TextView
        var tvLanguages: TextView
        var tvGuideCharges: TextView
        var tvGuideChargesGroup: TextView
        var guideName : String = ""
        var profile_pic : CircleImageView
        var vieww1 : View
        var cl_second : ConstraintLayout
        var cl_first : ConstraintLayout

        init {
            tvGuideName = view.findViewById(R.id.tv_guide_name)
            tvRatingValue = view.findViewById(R.id.txt_ratingValue)
            tvLanguages = view.findViewById(R.id.tv_languages)
            tvGuideCharges = view.findViewById(R.id.tv_guide_charges)
            tvGuideChargesGroup = view.findViewById(R.id.tv_guide_charges_group)
            profile_pic = view.findViewById(R.id.civ_profile_pic)
            vieww1 = view.findViewById(R.id.vieww1)
            cl_second = view.findViewById(R.id.cl_second)
            cl_first = view.findViewById(R.id.cl_first)
        }


        fun bind(guideItem: GuideListModel) {
            val guideName = guideItem.guide_info.name.substring(0, 1).toUpperCase() + guideItem.guide_info.name.substring(1)
            tvGuideName.text = guideName   //guideItem.guide_info.name

            if(guideItem.guide_info.guiderating != null){
                tvRatingValue.text = guideItem.guide_info.guiderating
            } else {
                tvRatingValue.visibility = View.GONE
            }

            tvLanguages.text = guideItem.guide_info.lang


            if(guideItem.guide_info.tourtype == 1){
                vieww1.visibility = View.GONE
                cl_second.visibility = View.GONE
                cl_first.visibility = View.VISIBLE
                tvGuideCharges.text = "$ " + guideItem.guide_info.tourPrice
            }else if(guideItem.guide_info.tourtype == 2){
                vieww1.visibility = View.GONE
                cl_first.visibility = View.GONE
                cl_second.visibility = View.VISIBLE
                tvGuideChargesGroup.text = "$ " + guideItem.guide_info.tourPrice
            }


            Glide.with(mcontext)
                .load(guideItem.guide_info.guid_profile_img)
                .apply(RequestOptions().placeholder(R.mipmap.ic_profile_placeholder).error(R.mipmap.ic_profile_placeholder))
                .into(profile_pic)

        }


    }


}