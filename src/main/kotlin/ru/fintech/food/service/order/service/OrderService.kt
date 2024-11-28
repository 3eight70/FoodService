package ru.fintech.food.service.order.service

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.fintech.food.service.bucket.repository.BucketRepository
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.common.exception.BadRequestException
import ru.fintech.food.service.courier.exception.BadOrderStatusException
import ru.fintech.food.service.order.dto.CreateOrderDto
import ru.fintech.food.service.order.dto.OrderDto
import ru.fintech.food.service.order.dto.OrderStatusEnum
import ru.fintech.food.service.order.dto.ShortOrderDto
import ru.fintech.food.service.order.exception.BadDeliveryTimeException
import ru.fintech.food.service.order.exception.BucketIsEmptyException
import ru.fintech.food.service.order.exception.OrderNotFoundException
import ru.fintech.food.service.order.exception.RestaurantDoesNotWorkInChosenTimeException
import ru.fintech.food.service.order.exception.RestaurantDoesNotWorkInCurrentDayException
import ru.fintech.food.service.order.mapper.OrderMapper
import ru.fintech.food.service.order.repository.OrderRepository
import ru.fintech.food.service.product.repository.ProductRepository
import ru.fintech.food.service.restaurant.dto.DayOfWeekEnum
import ru.fintech.food.service.restaurant.repository.WorkingHoursRepository
import ru.fintech.food.service.user.dto.user.UserDto

interface OrderService {
    fun createOrder(userDto: UserDto, createOrderDto: CreateOrderDto): OrderDto
    fun cancelOrder(userDto: UserDto, orderId: UUID): Response
    fun approveOrder(userDto: UserDto, orderId: UUID): Response
    fun getUserOrders(userDto: UserDto): List<ShortOrderDto>
    fun getOrder(userDto: UserDto, orderId: UUID): OrderDto
    fun getCurrentRestaurantOrders(userDto: UserDto, restaurantId: UUID): List<OrderDto>
    fun orderIsReadyForDelivery(userDto: UserDto, orderId: UUID): Response
    fun getPendingRestaurantOrders(userDto: UserDto, restaurantId: UUID): List<OrderDto>
}

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val bucketRepository: BucketRepository,
    private val productRepository: ProductRepository,
    private val workingHoursRepository: WorkingHoursRepository
) : OrderService {
    @Transactional
    override fun createOrder(userDto: UserDto, createOrderDto: CreateOrderDto): OrderDto {
        val currentTime = LocalTime.now()
        if (createOrderDto.deliveryTime.isBefore(currentTime)){
            throw BadDeliveryTimeException("Назад в будущее запрещено")
        }
        else if (createOrderDto.deliveryTime.isBefore(currentTime.plusMinutes(30))) {
            throw BadDeliveryTimeException("Время доставки должно быть на 30 минут в будущем от текущего")
        }

        val productsInBucket = bucketRepository.findAllByUserId(userDto.id)

        if (productsInBucket.isEmpty()) {
            throw BucketIsEmptyException()
        }

        val products = productRepository.findAllById(productsInBucket.map { it.productId })

        val currentDayOfWeek = getCurrentDayOfWeek()

        val restaurantWorkingHours = workingHoursRepository.findWorkingHoursByRestaurantIdAndDayOfWeek(
            restaurantId = createOrderDto.restaurantId,
            dayOfWeekEnum = currentDayOfWeek
        )

        if (restaurantWorkingHours == null) {
            throw RestaurantDoesNotWorkInCurrentDayException(currentDayOfWeek);
        }

        if (restaurantWorkingHours.openingTime.isAfter(createOrderDto.deliveryTime)
            || restaurantWorkingHours.closingTime.isBefore(createOrderDto.deliveryTime)
        ) {
            throw RestaurantDoesNotWorkInChosenTimeException(createOrderDto.deliveryTime)
        }

        val order = OrderMapper.Order(
            clientId = userDto.id,
            totalPrice = if (productsInBucket.isEmpty())
                BigDecimal.ZERO
            else
                products
                    .map { it.price }
                    .reduce { acc, price -> acc + price },
            orderedProducts = products,
            createOrderDto = createOrderDto,
        )

        orderRepository.save(order)
        bucketRepository.deleteAllByUserId(userDto.id)

        return OrderMapper.OrderDto(order)
    }

    @Transactional
    override fun cancelOrder(userDto: UserDto, orderId: UUID): Response {
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException(orderId) }

        order.status = OrderStatusEnum.REJECTED
        order.modifiedTime = Instant.now()

        orderRepository.save(order)

        return Response(
            status = HttpStatus.OK.value(),
            message = "Заказ успешно отменен"
        )
    }

    @Transactional
    override fun approveOrder(userDto: UserDto, orderId: UUID): Response {
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException(orderId) }

        order.status = OrderStatusEnum.APPROVED
        order.modifiedTime = Instant.now()

        orderRepository.save(order)

        return Response(
            status = HttpStatus.OK.value(),
            message = "Заказ успешно подтвержден"
        )
    }

    override fun getUserOrders(userDto: UserDto): List<ShortOrderDto> =
        orderRepository.findAllByClientId(userDto.id)
            .map(OrderMapper::ShortOrderDto)
            .sortedBy { it.createdTime }

    override fun getOrder(userDto: UserDto, orderId: UUID): OrderDto {
        val userOrder = orderRepository.findByClientIdAndId(
            clientId = userDto.id,
            orderId = orderId
        )

        if (userOrder == null) {
            throw OrderNotFoundException(orderId)
        }

        return OrderMapper.OrderDto(userOrder)
    }

    override fun getCurrentRestaurantOrders(userDto: UserDto, restaurantId: UUID): List<OrderDto> =
        orderRepository.findAllByRestaurantIdAndStatus(restaurantId, OrderStatusEnum.APPROVED)
            .map(OrderMapper::OrderDto)

    override fun getPendingRestaurantOrders(userDto: UserDto, restaurantId: UUID): List<OrderDto> =
        orderRepository.findAllByRestaurantIdAndStatus(restaurantId, OrderStatusEnum.APPROVE_PENDING)
            .map(OrderMapper::OrderDto)

    @Transactional
    override fun orderIsReadyForDelivery(userDto: UserDto, orderId: UUID): Response {
        val order = orderRepository.findById(orderId)
            .orElseThrow { OrderNotFoundException(orderId) }

        if (order.status != OrderStatusEnum.APPROVED){
            throw BadRequestException("Заказ не был подтвержден")
        }

        order.status = OrderStatusEnum.READY_FOR_DELIVERY
        order.modifiedTime = Instant.now()

        orderRepository.save(order)

        return Response(
            status = HttpStatus.OK.value(),
            message = "Заказ готов к доставке"
        )
    }

    private fun getCurrentDayOfWeek(): DayOfWeekEnum {
        val dayOfWeek = LocalDate.now().dayOfWeek.value - 1

        return DayOfWeekEnum.fromInt(dayOfWeek)
    }
}