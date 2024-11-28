package ru.fintech.food.service.courier.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.fintech.food.service.common.SwaggerConfig.Companion.BEARER_AUTH
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.courier.service.CourierService
import ru.fintech.food.service.order.dto.OrderDto
import ru.fintech.food.service.user.dto.user.UserDto

@RestController
@Tag(name = "Курьеры", description = "Отвечает за работу с курьерами")
@RequestMapping("/v1/courier")
class CourierController(
    private val courierService: CourierService
) {
    @GetMapping("/current")
    @Operation(
        summary = "Получение заказов курьера",
        description = "Позволяет получить список заказов курьера, которые он забрал",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun getCurrentCourierOrders(
        @AuthenticationPrincipal userDto: UserDto
    ): ResponseEntity<List<OrderDto>> = ResponseEntity.ok(courierService.getCurrentCourierOrders(userDto))

    @GetMapping("/ready")
    @Operation(
        summary = "Получение готовых для доставки заказов курьера",
        description = "Позволяет получить список заказов курьера, которые готовы к доставке",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun getCourierOrdersReadyForDelivery(
        @AuthenticationPrincipal userDto: UserDto
    ): ResponseEntity<List<OrderDto>> = ResponseEntity.ok(courierService.getCourierOrdersReadyForDelivery(userDto))

    @PostMapping
    @Operation(
        summary = "Назначение курьера на заказ",
        description = "Позволяет назначить курьеру заказ",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun setCourierForOrder(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор заказа") @RequestParam(value = "orderId") orderId: UUID,
        @Parameter(description = "Идентификатор курьера") @RequestParam(value = "courierId") courierId: UUID,
    ): ResponseEntity<Response> = ResponseEntity.ok(
        courierService.setCourierToOrder(
            userDto = userDto,
            courierId = courierId,
            orderId = orderId
        )
    )

    @GetMapping
    @Operation(
        summary = "Получение курьеров",
        description = "Позволяет получить список курьеров",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun getCouriers(
        @AuthenticationPrincipal userDto: UserDto
    ): ResponseEntity<List<UserDto>> = ResponseEntity.ok(courierService.getCouriers(userDto))

    @PostMapping("/order/delivery")
    @Operation(
        summary = "Замена статуса на 'Заказ принят в доставку'",
        description = "Позволяет курьеру поменять статус заказа",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun setOrderInDeliveryStatus(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор заказа") @RequestParam(value = "orderId") orderId: UUID,
    ): ResponseEntity<Response> = ResponseEntity.ok(
        courierService.setOrderInDeliveryStatus(
            userDto = userDto,
            orderId = orderId
        )
    )

    @PostMapping("/order/delivered")
    @Operation(
        summary = "Замена статуса на 'Заказ доставлен'",
        description = "Позволяет курьеру поменять статус заказа",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun setOrderDeliveredStatus(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор заказа") @RequestParam(value = "orderId") orderId: UUID,
    ): ResponseEntity<Response> = ResponseEntity.ok(
        courierService.setOrderDeliveredStatus(
            userDto = userDto,
            orderId = orderId
        )
    )
}