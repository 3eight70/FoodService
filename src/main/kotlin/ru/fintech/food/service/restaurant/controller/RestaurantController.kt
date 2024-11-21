package ru.fintech.food.service.restaurant.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.fintech.food.service.common.SwaggerConfig.Companion.BEARER_AUTH
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.restaurant.dto.DayOfWeekEnum
import ru.fintech.food.service.restaurant.dto.ConcreteTime
import ru.fintech.food.service.restaurant.dto.restaurant.RestaurantDto
import ru.fintech.food.service.restaurant.dto.restaurant.RestaurantRequestDto
import ru.fintech.food.service.restaurant.service.RestaurantService
import ru.fintech.food.service.user.dto.user.UserDto

@RestController
@Tag(name = "Рестораны", description = "Отвечает за работу с ресторанами доставки")
@RequestMapping("/v1/restaurant")
class RestaurantController(
    private val restaurantService: RestaurantService
) {

    @GetMapping
    @Operation(
        summary = "Получение списка все ресторанов",
        description = "Позволяет получить список ресторанов"
    )
    fun getAllRestaurants(): ResponseEntity<List<RestaurantDto>> =
        ResponseEntity.ok(restaurantService.getAllRestaurants())

    @PostMapping("/open")
    @Operation(
        summary = "Получение открытых, в указанную дату, ресторанов",
        description = "Позволяет получить список открытых ресторанов по указанной дате и времени"
    )
    fun getRestaurantsAvailableInConcreteTime(
        @RequestBody concreteTime: ConcreteTime,
        @Parameter(description = "День недели") @RequestParam(name = "dayOfWeek") dayOfWeekEnum: DayOfWeekEnum
    ): ResponseEntity<List<RestaurantDto>> =
        ResponseEntity.ok(
            restaurantService.getRestaurantsAvailableInConcreteTime(
                concreteTime = concreteTime,
                dayOfWeekEnum = dayOfWeekEnum
            )
        )

    @PostMapping
    @Operation(
        summary = "Создание ресторана",
        description = "Позволяет создать ресторан в системе",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun createRestaurant(
        @AuthenticationPrincipal userDto: UserDto,
        @RequestBody restaurantRequestDto: RestaurantRequestDto
    ): ResponseEntity<RestaurantDto> =
        ResponseEntity.ok(restaurantService.createRestaurant(userDto, restaurantRequestDto))

    @PutMapping
    @Operation(
        summary = "Обновление ресторана",
        description = "Позволяет обновить ресторан в системе",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun updateRestaurant(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор ресторана") @RequestParam(value = "id") id: UUID,
        @RequestBody restaurantRequestDto: RestaurantRequestDto
    ): ResponseEntity<RestaurantDto> =
        ResponseEntity.ok(
            restaurantService.updateRestaurant(
                user = userDto,
                id = id,
                restaurantRequestDto = restaurantRequestDto
            )
        )

    @DeleteMapping
    @Operation(
        summary = "Удаление ресторана",
        description = "Позволяет удалить ресторан из системы",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun deleteRestaurant(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор ресторана") id: UUID
    ): ResponseEntity<Response> =
        ResponseEntity.ok(restaurantService.deleteRestaurant(userDto, id))
}