package com.utalli.models

import java.io.Serializable

class RecentTourListModel(

    var id: Int,
    var guideId: Int,
    var userId: Int,
    var tourReqId: Int,
    var startdate: String = "",
    var enddate: String = "",
    var stateId: String,
    var tourPrice: String = "",
    var requesttype: String,
    var requeststatus: String = "",
    var poolId: String = "",
    var ratingStatus: String = "",
    var rating: Int,
    var ratingComments: String = "",
    var cityNames: String = "",


    var stateName: String = ""


) : Serializable