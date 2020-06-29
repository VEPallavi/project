package com.utalli.callBack

import com.utalli.models.CardItems

interface PaymentCardDeleteCallBack {

    fun deleteCardListener(itemDetails : CardItems)

}