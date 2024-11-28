package ru.fintech.food.service.order.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.fintech.food.service.common.SwaggerConfig.Companion.BEARER_AUTH
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.order.dto.CreateOrderDto
import ru.fintech.food.service.order.dto.OrderDto
import ru.fintech.food.service.order.dto.ShortOrderDto
import ru.fintech.food.service.order.service.OrderService
import ru.fintech.food.service.user.dto.user.UserDto

@RestController
@RequestMapping("/v1/order")
@Tag(name = "Заказы", description = "Отвечает за работу с заказами")
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping
    @Operation(
        summary = "Создание заказа",
        description = "Позволяет пользователю создать заказ",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun createOrder(
        @AuthenticationPrincipal userDto: UserDto,
        @RequestBody createOrderDto: CreateOrderDto,
    ): ResponseEntity<OrderDto> = ResponseEntity.ok(
        orderService.createOrder(
            userDto = userDto,
            createOrderDto = createOrderDto
        )
    )

    @PutMapping("/cancel")
    @Operation(
        summary = "Отмена заказа",
        description = "Позволяет оператору отменить заказ",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun cancelOrder(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор заказа") @RequestParam(value = "orderId") orderId: UUID
    ): ResponseEntity<Response> = ResponseEntity.ok(
        orderService.cancelOrder(
            userDto = userDto,
            orderId = orderId
        )
    )

    @PutMapping("/approve")
    @Operation(
        summary = "Подтверждение заказа",
        description = "Позволяет оператору подтвердить заказ",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun approveOrder(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор заказа") @RequestParam(value = "orderId") orderId: UUID
    ): ResponseEntity<Response> = ResponseEntity.ok(
        orderService.approveOrder(
            userDto = userDto,
            orderId = orderId
        )
    )

    @PutMapping("/ready")
    @Operation(
        summary = "Заказ готов к доставке",
        description = "Позволяет оператору поставить статус, что заказ готов к доставке",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun orderIsReadyForDelivery(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор заказа") @RequestParam(value = "orderId") orderId: UUID
    ): ResponseEntity<Response> = ResponseEntity.ok(
        orderService.orderIsReadyForDelivery(
            userDto = userDto,
            orderId = orderId
        )
    )

    @GetMapping
    @Operation(
        summary = "Получение истории заказов пользователя",
        description = "Позволяет пользователю получить историю заказов",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun getUserOrders(
        @AuthenticationPrincipal userDto: UserDto
    ): ResponseEntity<List<ShortOrderDto>> = ResponseEntity.ok(orderService.getUserOrders(userDto))

    @GetMapping("/{orderId}")
    @Operation(
        summary = "Получение информации по конкретному заказу пользователя",
        description = "Позволяет пользователю получить подробную информацию о заказе",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun getOrderInfo(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор заказа") @PathVariable(value = "orderId") orderId: UUID
    ): ResponseEntity<OrderDto> = ResponseEntity.ok(orderService.getOrder(userDto, orderId))

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(
        summary = "Получение активных заказов ресторана",
        description = "Позволяет оператору получить список активных заказов",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun getCurrentRestaurantOrders(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор ресторана") @PathVariable(value = "restaurantId") restaurantId: UUID
    ): ResponseEntity<List<OrderDto>> =
        ResponseEntity.ok(orderService.getCurrentRestaurantOrders(userDto, restaurantId))

    @GetMapping("/restaurant/pending/{restaurantId}")
    @Operation(
        summary = "Получение заказов, ожидающих подтверждения от ресторана",
        description = "Позволяет оператору получить список ожидающих подтверждения заказов",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun getPendingRestaurantOrders(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор ресторана") @PathVariable(value = "restaurantId") restaurantId: UUID
    ): ResponseEntity<List<OrderDto>> =
        ResponseEntity.ok(orderService.getPendingRestaurantOrders(userDto, restaurantId))
}