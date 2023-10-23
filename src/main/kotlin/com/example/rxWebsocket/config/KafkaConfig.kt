package com.example.rxWebsocket.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.receiver.ReceiverOptions
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderOptions
import java.util.Collections

@Configuration
class KafkaConfig(
    private val props : KafkaProperties
) {

    @Bean
    fun reactiveKafkaProducerTemplate() : ReactiveKafkaProducerTemplate<String, Map<String, Any>>{
        return ReactiveKafkaProducerTemplate(
            SenderOptions.create(props.buildProducerProperties())
        )
    }

    @Bean
    fun kafkaListenerContainerFactory() : ConcurrentKafkaListenerContainerFactory<String, Map<String, Any>>{

        return ConcurrentKafkaListenerContainerFactory<String, Map<String, Any>>().apply {
            consumerFactory = DefaultKafkaConsumerFactory<String, Map<String, Any>>(
                mapOf(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to props.bootstrapServers,
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to props.consumer.keyDeserializer,
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to props.consumer.valueDeserializer,
                    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
                    ConsumerConfig.GROUP_ID_CONFIG to props.consumer.groupId

                )
            )
        }


    }
}
