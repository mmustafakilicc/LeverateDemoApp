package com.mklc.leveratedemoapp.data.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubscribedResponse(
    @field:Json(name = "channelID")
    val channelID    : Int,
    @field:Json(name = "channelName")
    val channelName  : String,
    @field:Json(name = "event")
    val event        : String,
    @field:Json(name = "pair")
    val pair         : String,
    @field:Json(name = "status")
    val status       : String,
    @field:Json(name = "subscription")
    val subscription : Subscription
)