package ru.fintech.food.service.common.dto

import java.time.Instant

class Response(
    val status: Int,
    val message: String?,
    val timestamp: Instant = Instant.now()
)