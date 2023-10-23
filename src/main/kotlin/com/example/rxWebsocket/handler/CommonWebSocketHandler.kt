package com.example.rxWebsocket.handler

import com.example.rxWebsocket.service.ChannelService
import com.example.rxWebsocket.service.MessageKafkaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.time.Duration
import java.time.Instant


@Component
class CommonWebSocketHandler(
    private val channelService: ChannelService,
    private val messageKafkaService: MessageKafkaService,
) : WebSocketHandler {


    override fun handle(session: WebSocketSession): Mono<Void> {
        val channelId = 1 // session.handshakeInfo.headers["channel"]?.first()?.toInt() ?: throw Exception()

        val inbound = session.receive()
            .doOnSubscribe {
                channelService.addSession(
                    channelId, session
                )
            }.doOnCancel {
                println("CA")
                channelService.removeSession(
                    channelId, session
                )
            }.concatMap {
                println("inboud $it")
                messageKafkaService.sendMessage(it.payloadAsText)
            }.then()


//        val message = channelService.receiveMessage(channelId)
//            .defaultIfEmpty("EMPTY")
//            .map {
//                println(it)
//                session.textMessage(it)
//            }

//        val message = Flux.interval(Duration.ofSeconds(5)).map {
//            session.textMessage(Instant.now().toString())
//        }
//        val message = messageKafkaService.receiveMessage().map {
//            println("${session.id} - RECEIVE  ${it.value()?.get("message")}")
//            session.textMessage((it.value()?.get("message") as String?) ?: "NONE")
//        }

        val message = messageKafkaService.producedData().map {
            println("${session.id} - RECEIVE  ${it?.get("message")}")
            session.textMessage((it?.get("message") as String?) ?: "NONE")
        }

        val outbound = session.send(message)
            .doOnError {
                println("ERR ${it.message}")
                it.stackTrace.forEach(::println)
            }.doOnCancel {
                println("out Cancel")
            }



        return Mono.zip(inbound, outbound).then()
    }


}