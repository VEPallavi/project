package com.utalli.callBack

import com.utalli.models.GuideInfoModel

interface GuideListCallBack {
    fun  guideListData(itemDetails: GuideInfoModel)
}