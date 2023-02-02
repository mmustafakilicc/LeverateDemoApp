package com.mklc.leveratedemoapp.data.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KrakenRequestPayload(
    @field:Json(name = "event")
    val event: String,
    @field:Json(name = "pair")
    val pair: List<String>,
    @field:Json(name = "subscription")
    val subscription: Subscription
)