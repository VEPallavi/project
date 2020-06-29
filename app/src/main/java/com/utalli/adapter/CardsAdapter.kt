package com.utalli.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

import com.utalli.R
import com.utalli.callBack.PaymentCardDeleteCallBack
import com.utalli.models.CardItems

class CardsAdapter : BaseAdapter() {
    lateinit var context : Context
    var cardDataList: ArrayList<CardItems>?=null
    lateinit var itemListener : PaymentCardDeleteCallBack



    fun setData(context:Context,cardItems: ArrayList<CardItems>,itemListener : PaymentCardDeleteCallBack ){
        this.context = context
        this.cardDataList = cardItems
        this.itemListener = itemListener
//        notifyDataSetChanged()
    }


    override fun getItem(position: Int): Any {
        return cardDataList!!.get(position)
    }



    override fun getItemId(position: Int): Long {
        return position.toLong()
    }



    override fun getCount(): Int {
        return cardDataList!!.size
    }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View?
        val holder: CardViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_row_list_payment, parent, false)
            holder = CardViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as CardViewHolder
        }

        holder.tvCardNumber.text = cardDataList!!.get(position).cardnumber
        holder.tvCardHolderName.text = cardDataList!!.get(position).cardholdername
        holder.tvCvv.text = cardDataList!!.get(position).cardcvv
        holder.tvValidThrough.text = cardDataList!!.get(position).validthrough

        holder.iv_delete_icon.setOnClickListener {
           itemListener.deleteCardListener(cardDataList!!.get(position))
        }


        holder.cl_parent.setOnClickListener {


        }





        return view!!
    }


     inner class CardViewHolder(view: View) {
         var tvCardNumber : TextView
         var tvCardHolderName : TextView
         var  tvCvv : TextView
         var  tvValidThrough :TextView
         var cl_parent: ConstraintLayout
         var iv_delete_icon : ImageView


        init {
            tvCardNumber = view.findViewById(R.id.tv_card_number) as TextView
            tvCardHolderName = view.findViewById(R.id.tv_card_holder_name) as TextView
            tvCvv = view.findViewById(R.id.tv_cvv) as TextView
            tvValidThrough  = view.findViewById(R.id.tv_validThrough) as TextView
            cl_parent = view.findViewById(R.id.cl_parent) as ConstraintLayout
            iv_delete_icon = view.findViewById(R.id.iv_delete_icon) as ImageView
        }
    }



}