package com.utalli.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.utalli.R
import com.utalli.models.StateListData


class StateListAdapter(var mContext: Context, var mStateList: ArrayList<StateListData>) : BaseAdapter(){

    val mInflater: LayoutInflater = LayoutInflater.from(mContext)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_custom_spinner, parent, false)
            holder = ViewHolder()
            holder.tv_spinnerText = convertView!!.findViewById<View>(R.id.tv_spinnerTextNew) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.tv_spinnerText!!.text = mStateList[position].getName()
        return convertView
    }


    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_row_spinner_drop_down_window, parent, false)
            holder = ViewHolder()

            holder.tv_spinnerDropdownText = convertView!!.findViewById<View>(R.id.tv_spinnerDropdownText_new) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.tv_spinnerDropdownText!!.text = mStateList[position].getName()
        return convertView
    }


    override fun getItem(p0: Int): Any {
        return mStateList.get(p0).getName()!!
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return mStateList.size
    }


    internal class ViewHolder {
        var tv_spinnerText: TextView? = null
        var tv_spinnerDropdownText: TextView? = null
    }

}