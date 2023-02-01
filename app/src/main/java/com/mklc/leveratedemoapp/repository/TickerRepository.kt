package com.mklc.leveratedemoapp.repository

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import timber.log.Timber
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.SSLSocketFactory

@Singleton
class TickerRepository @Inject constructor() {

    private val messagePublish = PublishSubject.create<String>()

    private val url = "wss://ws.kraken.com"
    private val uri: URI = URI(url)

    private lateinit var client: WebSocketClient

    private val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory

    private val subscriptionPayload = "{\n" +
            "  \"event\": \"subscribe\",\n" +
            "  \"pair\": [\n" +
            "\"XBT/EUR\",\n" +
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
            "    \"EUR/GBP\"\n" +
            "\n" +
            "  ],\n" +
            "  \"subscription\": {\n" +
            "    \"name\": \"ticker\"\n" +
            "  }\n" +
            "}"

    private val unSubscriptionPayload = "{\n" +
            "  \"event\": \"unsubscribe\",\n" +
            "  \"pair\": [\n" +
            "\"XBT/EUR\",\n" +
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
            "    \"EUR/GBP\"\n" +
            "\n" +
            "  ],\n" +
            "  \"subscription\": {\n" +
            "    \"name\": \"ticker\"\n" +
            "  }\n" +
            "}"

    fun init(){
        createSocketClient()
        client.setSocketFactory(socketFactory)
        client.connect()
    }

    fun close(){
        client.close()
    }

    private fun createSocketClient(){
        client = object : WebSocketClient(uri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Timber.d("onOpen")
                subscribe()
            }

            override fun onMessage(message: String?) {
                Timber.d(message)
                messagePublish.onNext(message)
                message?.let { parseMessage(it) }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Timber.d("onClose")
                unsubscribe()
            }

            override fun onError(ex: Exception?) {
                Timber.d(ex)
            }
        }
    }

    private fun subscribe(){
        client.send(subscriptionPayload)
    }

    private fun unsubscribe(){
        client.send(unSubscriptionPayload)
    }

    private fun parseMessage(message: String){
        //TODO convert message to ticker object
    }

    fun getMessages(): Observable<String> = messagePublish
}