package com.example.rxWebsocket.handler

import com.example.rxWebsocket.service.RedisPubSubService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class RedisTestWebSocketHandler(
    private val redisPubSubService: RedisPubSubService
) : WebSocketHandler {
    override fun handle(session: WebSocketSession): Mono<Void> {
        val channelId = 1 // session.handshakeInfo.headers["channel"]?.first()?.toInt() ?: throw Exception()

        val inbound = session.receive()
            .concatMap {
                println("inboud $it")
                redisPubSubService.produce(it.payloadAsText)
            }.then()

        val message = redisPubSubService.listenTo().map {
            println("${session.id} | ${it.channel} - RECEIVE  ${it.message}")
            session.textMessage((it.toString()) ?: "NONE")
        }

        val outbound = session.send(message)
            .doOnError {
                println("ERR ${it.message}")
                it.stackTrace.forEach(::println)
            }.doOnCancel {
                println("out Cancel")
            }



        return Mono.zip(inbound, outbound).then()
    }
}