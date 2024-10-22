package ru.fintech.food.service.product.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
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
import ru.fintech.food.service.product.dto.category.ProductCategoryRequestDto
import ru.fintech.food.service.product.service.ProductCategoryService
import ru.fintech.food.service.user.dto.user.UserDto
import java.util.*

@RestController
@Tag(name = "Категории продуктов", description = "Позволяет работать с категориями продуктов")
@RequestMapping("/v1/category")
class ProductCategoryController(
    private val productCategoryService: ProductCategoryService
) {
    @GetMapping
    @Operation(
        summary = "Получение всех категорий продуктов",
        description = "Позволяет получить все категории продуктов"
    )
    fun getCategories() =
        ResponseEntity.ok(productCategoryService.getCategories())

    @GetMapping("/{categoryId}")
    @Operation(
        summary = "Получение конкретной категории",
        description = "Позволяет получить категорию по id"
    )
    fun getCategory(
        @Parameter(description = "Идентификатор категории") @PathVariable(name = "categoryId") categoryId: UUID
    ) = ResponseEntity.ok(productCategoryService.getCategoryById(categoryId))

    @DeleteMapping
    @Operation(
        summary = "Удаление категории",
        description = "Позволяет удалить категорию",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun deleteCategory(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор категории") @RequestParam(name = "categoryId") categoryId: UUID
    ) =
        ResponseEntity.ok(productCategoryService.deleteCategory(userDto, categoryId))

    @PostMapping
    @Operation(
        summary = "Создание категории",
        description = "Позволяет создать категорию",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun createCategory(
        @AuthenticationPrincipal userDto: UserDto,
        @Validated @RequestBody categoryRequestDto: ProductCategoryRequestDto
    ) =
        ResponseEntity.ok(productCategoryService.createCategory(userDto, categoryRequestDto))

    @PutMapping
    @Operation(
        summary = "Обновление категории",
        description = "Позволяет обновить категорию",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    fun updateCategory(
        @AuthenticationPrincipal userDto: UserDto,
        @Parameter(description = "Идентификатор категории для изменения") @RequestParam(name = "categoryId") categoryId: UUID,
        @Validated @RequestBody categoryRequestDto: ProductCategoryRequestDto
    ) =
        ResponseEntity.ok(productCategoryService.updateCategory(userDto, categoryId, categoryRequestDto))
}