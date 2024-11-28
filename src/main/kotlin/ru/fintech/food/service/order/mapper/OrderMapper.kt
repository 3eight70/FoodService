package ru.fintech.food.service.order.mapper

import java.math.BigDecimal
import java.util.UUID
import ru.fintech.food.service.order.dto.CreateOrderDto
import ru.fintech.food.service.order.dto.OrderDto
import ru.fintech.food.service.order.dto.OrderStatusEnum
import ru.fintech.food.service.order.dto.ShortOrderDto
import ru.fintech.food.service.order.entity.Order
import ru.fintech.food.service.product.entity.Product
import ru.fintech.food.service.product.mapper.ProductMapper

object OrderMapper {
    fun OrderDto(order: Order) =
        OrderDto(
            id = order.id,
            createdTime = order.createdTime,
            modifiedTime = order.modifiedTime,
            restaurantId = order.restaurantId,
            comments = order.comments,
            deliveryTime = order.deliveryTime,
            deliveryAddress = order.deliveryAddress,
            paymentMethod = order.paymentMethod,
            totalPrice = order.totalPrice,
            status = order.status,
            products = order.orderedProducts.map(ProductMapper::ShortProductDto)
        )

    fun ShortOrderDto(order: Order) =
        ShortOrderDto(
            id = order.id,
            createdTime = order.createdTime,
            modifiedTime = order.modifiedTime,
            restaurantId = order.restaurantId,
            deliveryTime = order.deliveryTime,
            deliveryAddress = order.deliveryAddress,
            paymentMethod = order.paymentMethod,
            totalPrice = order.totalPrice,
            status = order.status,
        )

    fun Order(clientId: UUID, totalPrice: BigDecimal, orderedProducts: List<Product>, createOrderDto: CreateOrderDto) =
        Order(
            modifiedTime = null,
            clientId = clientId,
            restaurantId = createOrderDto.restaurantId,
            courierId = null,
            status = OrderStatusEnum.APPROVE_PENDING,
            deliveryTime = createOrderDto.deliveryTime,
            deliveryAddress = createOrderDto.deliveryAddress,
            paymentMethod = createOrderDto.paymentMethod,
            totalPrice = totalPrice,
            comments = createOrderDto.comments,
            orderedProducts = orderedProducts
        )
}