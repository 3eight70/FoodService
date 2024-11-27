package ru.fintech.food.service.order.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalTime
import java.util.UUID

@Schema(description = "dto для создания заказа")
class CreateOrderDto(
    @field:Schema(description = "Идентификатор ресторана", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    @field:NotNull(message = "Идентификатор должен быть указан")
    val restaurantId: UUID,
    @field:Schema(description = "Время, к которому нужно доставить заказ", example = "12:00")
    @field:NotNull(message = "Время должно быть указано")
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty(value = "deliveryTime")
    val deliveryTime: LocalTime,
    @field:Schema(description = "Адрес доставки еды", example = "ул. Пушкина 97, п.1, кв. 12")
    @field:NotNull(message = "Адрес доставки должен быть указан")
    val deliveryAddress: String,
    @field:Schema(description = "Способ оплаты заказа", example = "BY_CARD")
    @field:NotNull(message = "Метод оплаты должен быть указан")
    val paymentMethod: PaymentMethodEnum,
    @field:Schema(description = "Комментарии к заказу", example = "Хочется поесть побыстрее")
    val comments: String?
)