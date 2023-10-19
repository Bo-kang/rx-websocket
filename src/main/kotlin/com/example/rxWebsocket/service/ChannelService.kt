package com.example.rxWebsocket.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux

@Service
class ChannelService {
    private val channels = listOf(1,2,3,4)
    private val channelSessionMap : Map<Int, MutableList<WebSocketSession>> = channels.associateWith { mutableListOf() }
    private val channelMessage : Map<Int, MutableList<String>> = channels.associateWith { mutableListOf() }
    fun addSession(channelId : Int, session : WebSocketSession){
        channelSessionMap[channelId]?.add(session) ?: throw Exception("Not Found Channel")
    }

    fun removeSession(channelId: Int, session : WebSocketSession){
        channelSessionMap[channelId]?.remove(session) ?: throw Exception("Not Found Channel")
    }

    fun sendMessage(channelId: Int, message : String): Flux<String> {
        channelMessage[channelId]?.add(message)
        return Flux.just(message)
    }



}