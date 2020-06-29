package com.utalli.activity

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.utalli.R
import kotlinx.android.synthetic.main.activity_place_profile_details.*

class PlaceProfileDetailsActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_profile_details)

        initViews()

    }

    private fun initViews() {

        ratingBar.rating = 3F
        val stars = ratingBar.getProgressDrawable() as LayerDrawable
        stars.getDrawable(2).setColorFilter(Color.parseColor("#f4ad42"), PorterDuff.Mode.SRC_ATOP)
    }


    override fun onClick(v: View?) {

    }

}