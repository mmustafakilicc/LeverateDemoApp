package com.mklc.leveratedemoapp.data.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Subscription(
    @field:Json(name = "name")
    val name: String = "ticker"
)