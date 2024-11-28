package ru.fintech.food.service.order.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.fintech.food.service.order.dto.OrderStatusEnum
import ru.fintech.food.service.order.entity.Order

@Repository
interface OrderRepository : JpaRepository<Order, UUID> {
    fun findAllByCourierIdAndStatus(courierId: UUID, orderStatusEnum: OrderStatusEnum): List<Order>
    fun findByCourierIdAndId(courierId: UUID, orderId: UUID): Order?
    fun findByClientIdAndId(clientId: UUID, orderId: UUID): Order?
    fun findAllByClientId(clientId: UUID): List<Order>
    fun findAllByRestaurantIdAndStatus(restaurantId: UUID, orderStatusEnum: OrderStatusEnum): List<Order>
}