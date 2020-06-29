package com.utalli.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utalli.R

class LanguageAdapter (var mContext : Context) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>(){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageAdapter.LanguageViewHolder {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_list_language_known,parent,false)
        return LanguageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: LanguageAdapter.LanguageViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    inner class LanguageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }




}