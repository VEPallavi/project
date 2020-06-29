package com.utalli.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationSearchDataItems(
    var id : Int,
    var name : String = "",
    var sortname : String = ""

) : Parcelable