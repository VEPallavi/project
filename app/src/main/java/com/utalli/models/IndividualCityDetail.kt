package com.utalli.models

import java.io.Serializable

data class IndividualCityDetail (
var id:Int,
var name : String ="",
var country_id : Int,
var states_id: Int,

var isSelected : Boolean

):Serializable