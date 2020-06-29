package com.utalli.models

data class GuideInfoModel(
    var id: String,
    var name: String,
    var gender: String,
    var lang: String,
    var guid_profile_img: String,
    var guide_private_price: String,
    var guide_pool_price: String,
    var guiderating: String,
    var tourtype : Int,
    var tourPrice : Float


)

