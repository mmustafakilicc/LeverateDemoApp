package com.mklc.leveratedemoapp.utils

import com.mklc.leveratedemoapp.data.model.network.SubscribedResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import timber.log.Timber

class TickerMessageParser {

    fun parseTickerResponse(message: String){
        Timber.e(message)
    }
}