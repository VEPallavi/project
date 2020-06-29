package com.utalli.models

data class GuideListModel(
    var id: String,
    var country_name: String,
    var statename: String,
    var guide_info: GuideInfoModel
)