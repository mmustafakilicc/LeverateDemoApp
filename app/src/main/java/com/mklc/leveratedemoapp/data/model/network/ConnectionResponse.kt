package com.mklc.leveratedemoapp.data.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConnectionResponse(
    @field:Json(name = "connectionID")
    val connectionID : Int?    = null,
    @field:Json(name = "event")
    val event        : String? = null,
    @field:Json(name = "status")
    val status       : String? = null,
    @field:Json(name = "version")
    val version      : String? = null
)