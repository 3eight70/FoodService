package ru.fintech.food.service.order.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalTime
import java.util.UUID
import ru.fintech.food.service.order.dto.OrderStatusEnum
import ru.fintech.food.service.order.dto.PaymentMethodEnum
import ru.fintech.food.service.product.entity.Product

@Entity
@Table(name = "t_orders")
class Order(
    @Id
    val id: UUID = UUID.randomUUID(),
    @Column(name = "created_time", nullable = false)
    val createdTime: Instant = Instant.now(),
    @Column(name = "modified_time")
    val modifiedTime: Instant?,
    @Column(name = "client_id", nullable = false)
    val clientId: UUID,
    @Column(name = "restaurant_id", nullable = false)
    val restaurantId: UUID,
    @Column(name = "courier_id")
    val courierId: UUID?,
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: OrderStatusEnum,
    @Column(name = "delivery_time", nullable = false)
    val deliveryTime: LocalTime,
    @Column(name = "delivery_address", nullable = false)
    val deliveryAddress: String,
    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    val paymentMethod: PaymentMethodEnum,
    @Column(name = "total_price", nullable = false)
    val totalPrice: BigDecimal,
    @Column(name = "comments")
    val comments: String?,
    @ManyToMany
    @JoinTable(
        name = "t_order_products",
        joinColumns = [JoinColumn(name = "order_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    val orderedProducts: List<Product>,
)