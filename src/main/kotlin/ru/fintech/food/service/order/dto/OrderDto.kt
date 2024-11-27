package ru.fintech.food.service.order.dto

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalTime
import java.util.UUID
import ru.fintech.food.service.product.dto.product.ShortProductDto

@Schema(description = "dto для заказа")
class OrderDto(
    @field:Schema(description = "Идентификатор заказа", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    val id: UUID,
    @field:Schema(description = "Время создания заказа")
    val createdTime: Instant,
    @field:Schema(description = "Время изменения статуса заказа")
    val modifiedTime: Instant?,
    @field:Schema(description = "Идентификатор ресторана", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    val restaurantId: UUID,
    @field:Schema(description = "Комментарии к заказу", example = "Хочется поесть побыстрее")
    val comments: String?,
    @field:Schema(description = "Время, к которому нужно доставить заказ")
    val deliveryTime: LocalTime,
    @field:Schema(description = "Адрес доставки еды", example = "ул. Пушкина 97, п.1, кв. 12")
    val deliveryAddress: String,
    @field:Schema(description = "Способ оплаты заказа", example = "BY_CARD")
    val paymentMethod: PaymentMethodEnum,
    @field:Schema(description = "Общая сумма заказа", example = "123")
    val totalPrice: BigDecimal,
    @field:Schema(description = "Статус заказа", example = "DELIVERED")
    val status: OrderStatusEnum,
    @field:ArraySchema(
        arraySchema = Schema(
            description = "Список продуктов заказа",
            implementation = ShortProductDto::class
        )
    )
    val products: List<ShortProductDto>
)