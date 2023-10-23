package com.example.rxWebsocket.service

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.kafka.sender.SenderResult
import java.net.InetAddress
import java.util.Random
import java.util.UUID

@Service
class MessageKafkaService(
    private val kafkaProducerTemplate: ReactiveKafkaProducerTemplate<String, Map<String, Any>>,
) {
    private val sink = Sinks.many().multicast().onBackpressureBuffer<Map<String, Any>>()
    companion object{
        const val TOPIC = "local.rx.topic"
    }
    fun sendMessage(message : String): Mono<SenderResult<Void>> {
        return kafkaProducerTemplate.send(TOPIC, mapOf("message" to message))
    }

    fun producedData(): Flux<Map<String, Any>> {
        return sink.asFlux()
    }

    @KafkaListener(topics = [TOPIC])
    fun receiveMessage(message : Map<String , Any>, ack : Acknowledgment) {

        println("First Group $message")
        sink.tryEmitNext(message)
    }
}
