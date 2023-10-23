package com.example.rxWebsocket.config

import com.example.rxWebsocket.handler.CommonWebSocketHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Configuration
class WebsocketConfig(
    private val commonWebSocketHandler: CommonWebSocketHandler
) {

    @Bean
    fun handlerMapping(): HandlerMapping {
       return SimpleUrlHandlerMapping(mapOf("/path" to commonWebSocketHandler), -1)
    }

    @Bean
    fun handlerAdapter() =  WebSocketHandlerAdapter()

}