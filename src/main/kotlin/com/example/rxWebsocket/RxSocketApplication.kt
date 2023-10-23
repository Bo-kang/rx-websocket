package com.example.rxWebsocket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
class RxSocketApplication

fun main(args: Array<String>) {
    runApplication<RxSocketApplication>(*args)
}
