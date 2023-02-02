package com.mklc.leveratedemoapp.utils

import com.mklc.leveratedemoapp.data.model.network.Ask
import com.mklc.leveratedemoapp.data.model.network.Ticker
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class is supposed to parse the messages coming from web socket
 *
 */
@Singleton
class TickerMessageParser @Inject constructor(
    private val krakenJsonAdapter: KrakenJsonAdapter
){

    /**
     * @param message
     * The message formats are below
     * "[channelId: Int, anonymous: JSONObject, channelName: String, pair: String]"
     * {"event": "heartbeat"}
     * {"connectionID": Int,"event": String, "status": String, "version": String}
     * {"channelID": Int, "channelName": String, "event": String, "pair": String, "status": String, "subscription": {"name": String}}
     * The message containing the prices is not json format, so have to use string parsing
     */
    fun parseTickerResponse(message: String): Ticker?{
        try {
            val jsonObj = JSONObject(message)
        }catch (ignored:java.lang.Exception){
            val name = getChannelName(message)
            val id = getId(message)
            val ask = getTicker(message)
            if(id != null && name != null && ask != null){
                return Ticker(id, ask, name)
            }
        }
        return null
    }

    private fun getChannelName(message: String): String? {
        val channelName = message.substringAfterLast(",\"").substringBefore("\"]")
        if(channelName == message){
            return null
        }
        return channelName
    }

    private fun getId(message: String): Int?{
        val channelId = message.substringAfter("[").substringBefore(",")
        if(channelId == message){
            return null
        }
        return channelId.toInt()
    }

    private fun getTicker(message: String): Ask? {
        val ask = message.substringAfter("{").substringBefore("}")
        if (ask == message) {
            return null
        }
        val askJsonString = "{$ask}"
        val askStr = krakenJsonAdapter.getObjectFromJson(AskStr::class.java, askJsonString)
        if(askStr != null && askStr.a.size >= 3){
            return Ask(askStr.a[0], askStr.a[1], askStr.a[2])
        }
        return null
    }

    @JsonClass(generateAdapter = true)
    data class AskStr(
        @field:Json(name = "a")
        val a: List<String>
    )
}