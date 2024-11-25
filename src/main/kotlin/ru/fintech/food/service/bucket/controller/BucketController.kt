package ru.fintech.food.service.bucket.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.fintech.food.service.bucket.dto.BucketDto
import ru.fintech.food.service.bucket.dto.ProductInBucketDto
import ru.fintech.food.service.bucket.service.BucketService
import ru.fintech.food.service.common.SwaggerConfig.Companion.BEARER_AUTH
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.user.dto.user.UserDto
import java.util.UUID

@RestController
@Tag(name = "Корзина", description = "Контроллер, отвечающий за работу с корзиной")
@RequestMapping("/v1/bucket")
class BucketController(
    private val bucketService: BucketService
) {
    @GetMapping
    @Operation(
        summary = "Получение корзины",
        description = "Позволяет получить список продуктов, добавленных в корзину, и их общую цену",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun getBucket(
        @AuthenticationPrincipal user: UserDto,
    ): ResponseEntity<BucketDto> =
        ResponseEntity.ok(bucketService.getBucket(user))

    @PostMapping
    @Operation(
        summary = "Добавление продукта в корзину",
        description = "Позволяет добавить продукт в корзину",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun addProduct(
        @AuthenticationPrincipal user: UserDto,
        @RequestParam(value = "productId") productId: UUID,
        @RequestParam(value = "quantity", required = false, defaultValue = "1") quantity: Int,
    ): ResponseEntity<ProductInBucketDto> =
        ResponseEntity.ok(bucketService.addProduct(user, productId, quantity))

    @DeleteMapping
    @Operation(
        summary = "Удаление продукта из корзины",
        description = "Позволяет удалить продукт из корзины",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun deleteProduct(
        @AuthenticationPrincipal user: UserDto,
        @RequestParam(value = "productId") productId: UUID,
        @RequestParam(value = "quantity", required = false, defaultValue = "1") quantity: Int,
    ): ResponseEntity<Response> =
        ResponseEntity.ok(bucketService.deleteProduct(user, productId, quantity))

    @PostMapping("/clear")
    @Operation(
        summary = "Очистка корзины",
        description = "Позволяет пользователю очистить корзину",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    fun clearBucket(@AuthenticationPrincipal user: UserDto): ResponseEntity<Response> =
        ResponseEntity.ok(bucketService.clearBucket(user))
}