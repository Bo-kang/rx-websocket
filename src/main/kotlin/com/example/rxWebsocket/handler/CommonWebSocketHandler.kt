package com.example.rxWebsocket.handler

import com.example.rxWebsocket.service.ChannelService
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.Instant

class CommonWebSocketHandler(
    private val channelService: ChannelService
) : WebSocketHandler {
    override fun handle(session: WebSocketSession): Mono<Void> {
        val channelId = session.handshakeInfo.headers["channel"]?.toString()?.toInt() ?: throw Exception()

        val inbound = session.receive()
            .doOnSubscribe {
                channelService.addSession(
                    channelId, session
                )
            }.doOnCancel {
                channelService.removeSession(
                    channelId, session
                )
            }.concatMap {
                channelService.sendMessage(channelId, it.payloadAsText)
            }.then()

        val outbound = session.send(
            Flux.interval(Duration.ofSeconds(5))
                .map {
                    session.textMessage(Instant.now().toString())
                })


        return Mono.zip(inbound, outbound).then()
    }
}