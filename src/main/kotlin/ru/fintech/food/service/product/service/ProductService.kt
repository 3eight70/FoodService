package ru.fintech.food.service.product.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.product.dto.product.FullProductDto
import ru.fintech.food.service.product.dto.product.ProductRequestDto
import ru.fintech.food.service.product.dto.product.ShortProductDto
import ru.fintech.food.service.product.repository.ProductRepository
import ru.fintech.food.service.user.dto.user.UserDto
import java.util.*

interface ProductService {
    fun getProducts(pageable: Pageable): List<ShortProductDto>
    fun getInfoAboutProduct(productId: UUID): FullProductDto
    fun getProductsByCategory(categoryId: List<UUID>, pageable: Pageable): List<ShortProductDto>
    fun getUnavailableProducts(): List<ShortProductDto>
    fun updateProduct(userDto: UserDto, productId: UUID, productDto: ProductRequestDto): ShortProductDto
    fun createProduct(userDto: UserDto, productDto: ProductRequestDto): ShortProductDto
    fun deleteProduct(userDto: UserDto, productId: UUID): Response
}

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository
) : ProductService {
    override fun getProducts(pageable: Pageable): List<ShortProductDto> {
        TODO("Not yet implemented")
    }

    override fun getInfoAboutProduct(productId: UUID): FullProductDto {
        TODO("Not yet implemented")
    }

    override fun getProductsByCategory(categoryId: List<UUID>, pageable: Pageable): List<ShortProductDto> {
        TODO("Not yet implemented")
    }

    override fun getUnavailableProducts(): List<ShortProductDto> {
        TODO("Not yet implemented")
    }

    override fun updateProduct(userDto: UserDto, productId: UUID, productDto: ProductRequestDto): ShortProductDto {
        TODO("Not yet implemented")
    }

    override fun createProduct(userDto: UserDto, productDto: ProductRequestDto): ShortProductDto {
        TODO("Not yet implemented")
    }

    override fun deleteProduct(userDto: UserDto, productId: UUID): Response {
        TODO("Not yet implemented")
    }
}