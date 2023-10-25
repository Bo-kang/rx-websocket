package com.example.rxWebsocket.config

import com.example.rxWebsocket.handler.CommonWebSocketHandler
import com.example.rxWebsocket.handler.RedisTestWebSocketHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Configuration
class WebsocketConfig(
    private val commonWebSocketHandler: CommonWebSocketHandler,
    private val redisTestWebSocketHandler: RedisTestWebSocketHandler
) {

    @Bean
    fun handlerMapping(): HandlerMapping {
       return SimpleUrlHandlerMapping(mapOf("/path" to commonWebSocketHandler, "/r" to redisTestWebSocketHandler), -1)
    }

    @Bean
    fun handlerAdapter() =  WebSocketHandlerAdapter()

}