package ru.fintech.food.service.restaurant.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.fintech.food.service.restaurant.dto.workinghours.WorkingHoursCreateDto
import ru.fintech.food.service.restaurant.dto.workinghours.WorkingHoursDto
import ru.fintech.food.service.restaurant.dto.workinghours.WorkingHoursUpdateDto
import ru.fintech.food.service.restaurant.service.WorkingHoursService
import ru.fintech.food.service.user.dto.user.UserDto

@RestController
@Tag(name = "Рабочие часы ресторанов", description = "Отвечает за работу с рабочими часами ресторанов")
@RequestMapping("/v1/restaurant/hours")
class WorkingHoursController(
    private val workingHoursService: WorkingHoursService
) {

    @Operation(
        summary = "Создание рабочих часов ресторана",
        description = "Позволяет создавать расписание рабочих часов",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @PostMapping
    fun createWorkingHours(
        @AuthenticationPrincipal userDto: UserDto,
        @RequestBody workingHoursCreateDto: WorkingHoursCreateDto
    ): ResponseEntity<WorkingHoursDto> =
        ResponseEntity.ok(
            workingHoursService.createWorkingHours(
                userDto = userDto,
                workingHoursCreateDto = workingHoursCreateDto
            )
        )

    @Operation(
        summary = "Обновление рабочих часов ресторана",
        description = "Позволяет обновить расписание рабочих часов",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @PutMapping
    fun updateWorkingHours(
        @AuthenticationPrincipal userDto: UserDto,
        @RequestParam(value = "restaurantId") restaurantId: UUID,
        @RequestBody workingHoursCreateDto: WorkingHoursUpdateDto
    ): ResponseEntity<WorkingHoursDto> =
        ResponseEntity.ok(
            workingHoursService.updateWorkingHours(
                id = restaurantId,
                userDto = userDto,
                workingHoursUpdateDto = workingHoursCreateDto,
            )
        )

    @Operation(
        summary = "Получение рабочих часов ресторана",
        description = "Позволяет получить расписание работы ресторана"
    )
    @GetMapping
    fun getRestaurantWorkingHours(@RequestParam(value = "restaurantId") restaurantId: UUID): ResponseEntity<List<WorkingHoursDto>> =
        ResponseEntity.ok(
            workingHoursService.getRestaurantWorkingHours(
                restaurantId = restaurantId
            )
        )
}