package com.example.webflux_websocket.data

import java.time.Instant

data class WebfluxSocketMessage (
    val createdAt : Instant = Instant.now(),
    val action : String,
    val message : String,
    val author : String
)