package ru.fintech.food.service.order.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.fintech.food.service.order.entity.Order

@Repository
interface OrderRepository : JpaRepository<Order, UUID> {
}