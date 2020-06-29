package com.utalli.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.utalli.R

class PoolListAdapter:RecyclerView.Adapter<PoolListAdapter.PoolListViewHolder>()



{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoolListViewHolder {
        return PoolListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pool_list,parent,false))
    }

    override fun getItemCount(): Int {
    return 5
    }

    override fun onBindViewHolder(holder: PoolListViewHolder, position: Int) {
    }

    inner class PoolListViewHolder(view:View): RecyclerView.ViewHolder(view)
    {}
}