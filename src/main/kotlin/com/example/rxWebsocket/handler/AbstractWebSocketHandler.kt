package com.example.rxWebsocket.handler

import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class AbstractWebSocketHandler : WebSocketHandler {
    override fun handle(session: WebSocketSession): Mono<Void> {

        session.receive().doOnSubscribe {

        }

        TODO("Not yet implemented")
    }

    abstract fun handleInput(session: WebSocketSession) : Flux<Void>

    fun checkAlive(session : WebSocketSession){

    }


}