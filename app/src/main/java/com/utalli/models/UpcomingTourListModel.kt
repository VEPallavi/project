package com.utalli.models

import com.utalli.models.Payment.PaymentData
import java.io.Serializable

class UpcomingTourListModel (

    var id : Int,
    var guideId : Int,
    var userId : Int,
    var tourReqId : Int,
    var startdate : String ="",
    var enddate : String ="",
  //  var tourtype : Int,
    var tourPrice : String ="",
    var requesttype : String ="",
    var requeststatus : Int,
  //  var tourdays : Int,
  //  var tourStatus : Int,
   // var tourdates : String ="",
    var payment : PaymentData


):Serializable