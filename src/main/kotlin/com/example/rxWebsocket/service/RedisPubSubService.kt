package com.example.rxWebsocket.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.connection.ReactiveSubscription
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class RedisPubSubService(
    private val redisOperations: ReactiveRedisOperations<String, Any>,
) {
    fun produce(message : String): Mono<Long> {
        return redisOperations.convertAndSend("tmp", mapOf("message" to message))
    }

    fun listenTo(): Flux<out ReactiveSubscription.Message<String, Any>> {
        return redisOperations.listenToChannel("tmp")
    }



}