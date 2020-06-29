package com.utalli.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.utalli.R
import com.utalli.adapter.PoolListAdapter
import kotlinx.android.synthetic.main.activity_guide_pools_list.*

class GuidePoolsListActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide_pools_list)
        initView()

    }

    fun initView() {

        toolbar.title = ""
        toolbar.setNavigationIcon(R.drawable.arrow_back_white)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener { finish() }





        rv_pool_list.setHasFixedSize(true)
        rv_pool_list.layoutManager = LinearLayoutManager(this)
        rv_pool_list.adapter = PoolListAdapter()

    }


}
