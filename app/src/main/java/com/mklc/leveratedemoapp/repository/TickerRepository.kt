package com.mklc.leveratedemoapp.repository

import com.mklc.leveratedemoapp.data.model.network.KrakenRequestPayload
import com.mklc.leveratedemoapp.data.model.network.Subscription
import com.mklc.leveratedemoapp.data.model.network.Ticker
import com.mklc.leveratedemoapp.utils.*
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
class TickerRepository @Inject constructor(
    private val tickerMessageParser: TickerMessageParser,
    private val krakenJsonAdapter: KrakenJsonAdapter
) {

    private val messagePublish = PublishSubject.create<Ticker>()
    private val connectPublish = PublishSubject.create<Boolean>()

    private val uri: URI = URI(URL)

    private lateinit var client: WebSocketClient

    private val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory

    init {
        createSocketClient()
    }

    fun open() {
        client.connect()
    }

    fun start(){
        subscribe()
    }

    fun stop(){
        unsubscribe()
    }

    fun close() {
        client.close()
    }

    private fun createSocketClient() {
        client = object : WebSocketClient(uri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Timber.d("Web socket closed")
                connectPublish.onNext(true)
            }

            override fun onMessage(message: String?) {
                message?.let { parseMessage(it) }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Timber.d("Web socket closed")
                connectPublish.onNext(false)
            }

            override fun onError(ex: Exception?) {
                connectPublish.onNext(false)
            }
        }
        client.setSocketFactory(socketFactory)
    }

    private fun subscribe() {
        client.send(krakenJsonAdapter.toJson(createSubscriptionRequest("subscribe")))
    }

    private fun unsubscribe() {
        client.send(krakenJsonAdapter.toJson(createSubscriptionRequest("unsubscribe")))
    }

    private fun parseMessage(message: String) {
        val ticker = tickerMessageParser.parseTickerResponse(message)
        if (ticker != null) {
            messagePublish.onNext(ticker)
        }
    }

    private fun createSubscriptionRequest(event: String): KrakenRequestPayload {
        return KrakenRequestPayload(event = event, pair = buildList {
            add(XBT_USD)
            add(XBT_GBP)
            add(ETH_EUR)
            add(ETH_USD)
            add(ETH_GBP)
            add(ATLAS_USD)
            add(ATOM_AUD)
            add(BADGER_EUR)
            add(BAL_EUR)
            add(BNC_USD)
            add(CRV_ETH)
            add(EUR_GBP)
        }, Subscription(name = "ticker"))
    }

    fun getMessages(): Observable<Ticker> = messagePublish

    fun isConnected(): Observable<Boolean> = connectPublish
}