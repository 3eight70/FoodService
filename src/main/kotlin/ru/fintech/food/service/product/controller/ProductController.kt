package ru.fintech.food.service.product.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.fintech.food.service.product.dto.product.ProductRequestDto
import ru.fintech.food.service.product.service.ProductService
import ru.fintech.food.service.user.dto.user.UserDto
import java.util.*

@RestController
@RequestMapping("/v1/product")
@Tag(name = "Позиции меню", description = "Контроллер, отвечающий за работу с позициями меню")
class ProductController(
    private val productService: ProductService
) {
    @GetMapping
    @Operation(
        summary = "Получение списка позиций",
        description = "Позволяет получить список позиций меню с пагинацией"
    )
    fun getProducts(
        @ParameterObject @PageableDefault(sort = ["name"], size = 20, direction = Sort.Direction.ASC) pageable: Pageable
    ) = ResponseEntity.ok(productService.getProducts(pageable))

    @GetMapping("/{productId}")
    @Operation(
        summary = "Получение подробной информации о позиции меню",
        description = "Позволяет получить подробную информацию о позиции меню"
    )
    fun getInfoAboutProduct(
        @Parameter(description = "Идентификатор позиции") @PathVariable("productId") productId: UUID
    ) = ResponseEntity.ok(productService.getInfoAboutProduct(productId))

    @GetMapping("/category")
    @Operation(
        summary = "Получение позиций меню по определенной категории",
        description = "Позволяет получить позиции, принадлежащие выбранным категориям с пагинацией"
    )
    fun getProductsByCategory(
        @Parameter(description = "Идентификатор категории") @RequestParam("categoryId") categoryId: List<UUID>,
        @ParameterObject @PageableDefault(sort = ["name"], size = 20, direction = Sort.Direction.ASC) pageable: Pageable
    ) = ResponseEntity.ok(productService.getProductsByCategory(categoryId, pageable))

    @GetMapping("/unavailable")
    @Operation(
        summary = "Получение недоступных в текущий момент позиций меню",
        description = "Позволяет получить список недоступных позиций меню"
    )
    fun getUnavailableProducts() = ResponseEntity.ok(productService.getUnavailableProducts())

    @PostMapping
    @Operation(
        summary = "Создание позиции меню",
        description = "Позволяет создать позицию",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun createProduct(
        @AuthenticationPrincipal userDto: UserDto,
        @RequestBody @Validated productRequestDto: ProductRequestDto
    ) = ResponseEntity.ok(productService.createProduct(userDto, productRequestDto))

    @PutMapping
    @Operation(
        summary = "Обновление позиции меню",
        description = "Позволяет обновить конкретную позицию",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun updateProduct(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор позиции") @RequestParam("productId") productId: UUID,
        @RequestBody @Validated productRequestDto: ProductRequestDto
    ) = ResponseEntity.ok(productService.updateProduct(userDto, productId, productRequestDto))

    @DeleteMapping
    @Operation(
        summary = "Удаление позиции меню",
        description = "Позволяет удалить конкретную позицию",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun deleteProduct(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор позиции") @RequestParam("productId") productId: UUID
    ) = ResponseEntity.ok(productService.deleteProduct(userDto, productId))
}