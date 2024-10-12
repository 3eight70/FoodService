package ru.fintech.food.service.user.service

interface RefreshTokenService {
    fun verifyExpiration()
}