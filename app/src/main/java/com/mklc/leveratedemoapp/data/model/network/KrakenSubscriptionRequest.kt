package com.mklc.leveratedemoapp.data.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KrakenSubscriptionRequest(
    @field:Json(name = "event")
    val event: String = "subscribe",
    @field:Json(name = "pair")
    val pair: List<String> = listOf("\"XBT/EUR\",\n" +
            "    \"XBT/USD\",\n" +
            "    \"XBT/GBP\",\n" +
            "    \"ETH/EUR\",\n" +
            "    \"ETH/USD\",\n" +
            "    \"ETH/GBP\",\n" +
            "    \"ATLAS/USD\",\n" +
            "    \"ATOM/AUD\",\n" +
            "    \"BADGER/EUR\",\n" +
            "    \"BAL/EUR\",\n" +
            "    \"BNC/USD\",\n" +
            "    \"CRV/ETH\",\n" +
            "    \"EUR/GBP\"\n"),
    @field:Json(name = "subscription")
    val subscription: Subscription
)

@JsonClass(generateAdapter = true)
data class Subscription(
    @field:Json(name = "name")
    val name: String = "ticker"
)