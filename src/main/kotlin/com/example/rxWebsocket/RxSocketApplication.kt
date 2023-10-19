package com.example.rxWebsocket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RxSocketApplication

fun main(args: Array<String>) {
    runApplication<RxSocketApplication>(*args)
}
