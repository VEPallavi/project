package com.utalli.models

import java.io.Serializable

class CardItems(
    var id:Int,
    var userId: Int,
    var cardnumber : String ,
    var cardholdername : String,
    var cardcvv : String,
    var validthrough : String


):Serializable