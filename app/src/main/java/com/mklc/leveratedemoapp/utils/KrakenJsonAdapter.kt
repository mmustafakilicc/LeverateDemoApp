package com.mklc.leveratedemoapp.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KrakenJsonAdapter @Inject constructor(){

    private val moshi = Moshi.Builder().build()

    fun toJson(data: Any): String{
        val jsonAdapter = moshi.adapter(data.javaClass)
        val str = jsonAdapter.toJson(data)
        Timber.e(str)
        return str
    }

    fun <T> getObjectFromJson(typeOfObject: Class<T>, jsonString: String): T? {
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<T> = moshi.adapter<T>(typeOfObject)
        return jsonAdapter.fromJson(jsonString)
    }
}