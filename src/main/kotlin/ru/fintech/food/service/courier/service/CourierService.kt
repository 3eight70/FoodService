package ru.fintech.food.service.courier.service

import java.time.Instant
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.fintech.food.service.admin.exception.UserIsNotCourierException
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.courier.exception.CourierDoesNotHaveOrderException
import ru.fintech.food.service.courier.exception.OrderIsNotReadyForDeliveryException
import ru.fintech.food.service.order.dto.OrderDto
import ru.fintech.food.service.order.dto.OrderStatusEnum
import ru.fintech.food.service.order.exception.OrderNotFoundException
import ru.fintech.food.service.order.mapper.OrderMapper
import ru.fintech.food.service.order.repository.OrderRepository
import ru.fintech.food.service.user.dto.user.RoleEnum
import ru.fintech.food.service.user.dto.user.UserDto
import ru.fintech.food.service.user.exception.UserNotFoundException
import ru.fintech.food.service.user.mapper.UserMapper
import ru.fintech.food.service.user.repository.UserRepository

interface CourierService {
    fun getCurrentCourierOrders(userDto: UserDto): List<OrderDto>
    fun getCourierOrdersReadyForDelivery(userDto: UserDto): List<OrderDto>
    fun setCourierToOrder(userDto: UserDto, orderId: UUID, courierId: UUID): Response
    fun getCouriers(userDto: UserDto): List<UserDto>
    fun setOrderInDeliveryStatus(userDto: UserDto, orderId: UUID): Response
    fun setOrderDeliveredStatus(userDto: UserDto, orderId: UUID): Response
}

@Service
class CourierServiceImpl(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository
) : CourierService {
    override fun getCurrentCourierOrders(userDto: UserDto): List<OrderDto> =
        orderRepository.findAllByCourierIdAndStatus(userDto.id, OrderStatusEnum.IN_DELIVERY)
            .map(OrderMapper::OrderDto)

    override fun getCourierOrdersReadyForDelivery(userDto: UserDto): List<OrderDto> =
        orderRepository.findAllByCourierIdAndStatus(userDto.id, OrderStatusEnum.READY_FOR_DELIVERY)
            .map(OrderMapper::OrderDto)

    @Transactional
    override fun setCourierToOrder(userDto: UserDto, orderId: UUID, courierId: UUID): Response {
        val courier = userRepository.findById(courierId)
            .orElseThrow { UserNotFoundException(courierId) }

        if (courier.role != RoleEnum.COURIER) {
            throw UserIsNotCourierException()
        }

        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException(orderId) }

        order.courierId = courierId

        return Response(
            status = HttpStatus.OK.value(),
            message = "Курьер успешно назначен на заказ"
        )
    }

    override fun getCouriers(userDto: UserDto): List<UserDto> =
        userRepository.findAllByRole(RoleEnum.COURIER)
            .map(UserMapper::UserDto)

    @Transactional
    override fun setOrderInDeliveryStatus(userDto: UserDto, orderId: UUID): Response {
        changeOrderStatus(
            userDto = userDto,
            orderId = orderId,
            requiredStatus = OrderStatusEnum.READY_FOR_DELIVERY,
            desiredStatus = OrderStatusEnum.IN_DELIVERY
        )

        return Response(
            status = HttpStatus.OK.value(),
            message = "Заказ принят в доставку"
        )
    }

    @Transactional
    override fun setOrderDeliveredStatus(userDto: UserDto, orderId: UUID): Response {
        changeOrderStatus(
            userDto = userDto,
            orderId = orderId,
            requiredStatus = OrderStatusEnum.IN_DELIVERY,
            desiredStatus = OrderStatusEnum.DELIVERED
        )

        return Response(
            status = HttpStatus.OK.value(),
            message = "Заказ доставлен"
        )
    }

    private fun changeOrderStatus(
        userDto: UserDto,
        orderId: UUID,
        requiredStatus: OrderStatusEnum,
        desiredStatus: OrderStatusEnum
    ) {
        val order = orderRepository.findByCourierIdAndId(
            courierId = userDto.id,
            orderId = orderId
        )

        if (order == null) {
            throw CourierDoesNotHaveOrderException(orderId)
        }

        if (order.status != requiredStatus) {
            throw OrderIsNotReadyForDeliveryException()
        }

        order.status = desiredStatus
        order.modifiedTime = Instant.now()

        orderRepository.save(order)
    }
}