package ru.fintech.food.service.configuration

import io.minio.MinioClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.multipart.MultipartResolver
import org.springframework.web.multipart.support.StandardServletMultipartResolver

@Configuration
@EnableConfigurationProperties(MinioProperties::class)
class MinioConfiguration(
    private val properties: MinioProperties
) {
    @Bean
    fun multipartResolver(): MultipartResolver = StandardServletMultipartResolver()

    @Bean
    fun minioClient(): MinioClient =
        MinioClient.builder()
            .endpoint(properties.endpoint)
            .credentials(properties.accessKey, properties.secretKey)
            .build()
}

/**
 * Конфигурация для соединения с minio
 */
@ConfigurationProperties("minio")
class MinioProperties(
    val accessKey: String,
    val secretKey: String,
    val endpoint: String,
    val bucketName: String,
    val availableExtensions: Set<String>
)