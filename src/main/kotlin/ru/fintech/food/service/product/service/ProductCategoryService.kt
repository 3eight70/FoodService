package ru.fintech.food.service.product.service

import org.springframework.stereotype.Service
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.product.dto.category.ProductCategoryDto
import ru.fintech.food.service.product.dto.category.ProductCategoryRequestDto
import ru.fintech.food.service.product.exception.ProductCategoryAlreadyExistsException
import ru.fintech.food.service.product.exception.ProductCategoryNotFoundException
import ru.fintech.food.service.product.mapper.ProductCategoryMapper
import ru.fintech.food.service.product.repository.ProductCategoryRepository
import ru.fintech.food.service.user.dto.user.UserDto
import java.util.*

interface ProductCategoryService {
    fun getCategories(): List<ProductCategoryDto>
    fun getCategoryById(categoryId: UUID): ProductCategoryDto
    fun createCategory(userDto: UserDto, categoryRequestDto: ProductCategoryRequestDto): ProductCategoryDto
    fun updateCategory(
        userDto: UserDto,
        categoryId: UUID,
        categoryRequestDto: ProductCategoryRequestDto
    ): ProductCategoryDto

    fun deleteCategory(userDto: UserDto, categoryId: UUID): Response
}

@Service
class ProductCategoryServiceImpl(
    private val productCategoryRepository: ProductCategoryRepository
) : ProductCategoryService {
    override fun getCategories(): List<ProductCategoryDto> =
        productCategoryRepository.findAll()
            .map(ProductCategoryMapper::toProductCategoryDto)

    override fun getCategoryById(categoryId: UUID): ProductCategoryDto =
        ProductCategoryMapper.toProductCategoryDto(
            productCategoryRepository.findById(categoryId)
                .orElseThrow { ProductCategoryNotFoundException(categoryId) }
        )

    override fun createCategory(userDto: UserDto, categoryRequestDto: ProductCategoryRequestDto): ProductCategoryDto {
        productCategoryRepository.findByName(categoryRequestDto.name)
            .ifPresent { throw ProductCategoryAlreadyExistsException(categoryRequestDto.name) }

        val category = ProductCategoryMapper.toEntity(categoryRequestDto)

        productCategoryRepository.save(category)

        return ProductCategoryMapper.toProductCategoryDto(category)
    }

    override fun updateCategory(
        userDto: UserDto,
        categoryId: UUID,
        categoryRequestDto: ProductCategoryRequestDto
    ): ProductCategoryDto {
        productCategoryRepository.findByName(categoryRequestDto.name)
            .ifPresent { throw ProductCategoryAlreadyExistsException(categoryRequestDto.name) }

        val category = productCategoryRepository.findById(categoryId)
            .orElseThrow { ProductCategoryNotFoundException(categoryId) }

        category.name = categoryRequestDto.name

        productCategoryRepository.save(category)

        return ProductCategoryMapper.toProductCategoryDto(category)
    }

    override fun deleteCategory(userDto: UserDto, categoryId: UUID): Response {
        val category = productCategoryRepository.findById(categoryId)
            .orElseThrow { ProductCategoryNotFoundException(categoryId) }

        productCategoryRepository.delete(category)

        return Response(
            status = 200,
            message = "Категория успешно удалена"
        )
    }
}