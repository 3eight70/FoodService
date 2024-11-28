package ru.fintech.food.service.order.dto

enum class OrderStatusEnum {
    APPROVE_PENDING,
    APPROVED,
    READY_FOR_DELIVERY,
    IN_DELIVERY,
    DELIVERED,
    REJECTED
}