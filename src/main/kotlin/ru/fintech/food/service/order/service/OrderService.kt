package ru.fintech.food.service.order.service

import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.order.dto.CreateOrderDto
import ru.fintech.food.service.order.dto.OrderDto
import ru.fintech.food.service.order.dto.ShortOrderDto
import ru.fintech.food.service.order.repository.OrderRepository
import ru.fintech.food.service.user.dto.user.UserDto

interface OrderService {
    fun createOrder(userDto: UserDto, createOrderDto: CreateOrderDto): OrderDto
    fun cancelOrder(userDto: UserDto, orderId: UUID): Response
    fun getUserOrders(userDto: UserDto): List<ShortOrderDto>
    fun getOrder(userDto: UserDto, orderId: UUID): OrderDto
    fun getCurrentRestaurantOrders(userDto: UserDto): List<OrderDto>
    fun getCourierOrders(userDto: UserDto): List<OrderDto>
    fun setCourierToOrder(userDto: UserDto, orderId: UUID, courierId: UUID): Response
}

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository
) : OrderService {
    @Transactional
    override fun createOrder(userDto: UserDto, createOrderDto: CreateOrderDto): OrderDto {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun cancelOrder(userDto: UserDto, orderId: UUID): Response {
        TODO("Not yet implemented")
    }

    override fun getUserOrders(userDto: UserDto): List<ShortOrderDto> {
        TODO("Not yet implemented")
    }

    override fun getOrder(userDto: UserDto, orderId: UUID): OrderDto {
        TODO("Not yet implemented")
    }

    override fun getCurrentRestaurantOrders(userDto: UserDto): List<OrderDto> {
        TODO("Not yet implemented")
    }

    override fun getCourierOrders(userDto: UserDto): List<OrderDto> {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun setCourierToOrder(userDto: UserDto, orderId: UUID, courierId: UUID): Response {
        TODO("Not yet implemented")
    }
}