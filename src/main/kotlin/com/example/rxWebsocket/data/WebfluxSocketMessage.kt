package com.example.rxWebsocket.data

import java.time.Instant

data class WebfluxSocketMessage (
    val createdAt : Instant = Instant.now(),
    val action : String,
    val message : String,
    val author : String
)