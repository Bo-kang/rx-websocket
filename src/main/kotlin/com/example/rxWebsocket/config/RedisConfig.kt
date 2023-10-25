package com.example.rxWebsocket.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.lettuce.core.SocketOptions
import io.lettuce.core.TimeoutOptions
import io.lettuce.core.cluster.ClusterClientOptions
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
class RedisConfig(
    @Value("\${datasource.redis.host}")
    private val host: String,
) {
    @Primary
    @Bean
    fun lettuceConnectionFactory(): ReactiveRedisConnectionFactory {
        val redisClusterConfiguration = RedisClusterConfiguration(listOf(host))
        val clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
            // enablePeriodicRefresh : 지정한 시간마다 클러스터 구성 정보를 가져와서 업데이트
            .enablePeriodicRefresh(Duration.ofMinutes(5))
            // 특정 이벤트 발생시(command의 응답)에 반응하여 refresh (MOVED, ASK , UNKOWN_NODE, UNCOVERD_SLOT 등등)
            .enableAllAdaptiveRefreshTriggers()
            .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(30))
            .build()

        val clusterClientOptions = ClusterClientOptions.builder()
            .socketOptions(SocketOptions.builder().connectTimeout(Duration.ofSeconds(10)).keepAlive(true).build())
            .timeoutOptions(TimeoutOptions.enabled(Duration.ofSeconds(30)))
            .topologyRefreshOptions(clusterTopologyRefreshOptions)
            .build()

        val clientConfig = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofMinutes(1))
            .clientOptions(clusterClientOptions)
            .build()
        return LettuceConnectionFactory(redisClusterConfiguration, clientConfig)
    }

//    @Primary
//    @Bean
//    fun listenerContainer(factory: ReactiveRedisConnectionFactory): ReactiveRedisMessageListenerContainer =
//        ReactiveRedisMessageListenerContainer(factory)

    @Primary
    @Bean("redisOperations")
    fun redisOperations(
        lettuceConnectionFactory: ReactiveRedisConnectionFactory,
    ): ReactiveRedisOperations<String, Any> { //@Todo Any to interface
        val serializationContext: RedisSerializationContext<String, Any> = RedisSerializationContext
            .newSerializationContext<String, Any>(StringRedisSerializer())
            .value(
                Jackson2JsonRedisSerializer(
                    ObjectMapper().registerKotlinModule(),
                    Any::class.java
                )
            )
            .build()
        return ReactiveRedisTemplate(lettuceConnectionFactory, serializationContext)
    }
}

