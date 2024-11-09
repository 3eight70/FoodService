package ru.fintech.food.service.product.service

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.product.dto.product.FullProductDto
import ru.fintech.food.service.product.dto.product.ProductRequestDto
import ru.fintech.food.service.product.dto.product.ShortProductDto
import ru.fintech.food.service.product.exception.ProductAlreadyExistsException
import ru.fintech.food.service.product.exception.ProductNotFoundException
import ru.fintech.food.service.product.mapper.ProductMapper
import ru.fintech.food.service.product.repository.ProductCategoryRepository
import ru.fintech.food.service.product.repository.ProductRepository
import ru.fintech.food.service.product.specification.ProductSpecification
import ru.fintech.food.service.user.dto.user.UserDto
import java.util.UUID

interface ProductService {
    fun getProducts(pageable: Pageable): Page<ShortProductDto>
    fun getInfoAboutProduct(productId: UUID): FullProductDto
    fun getProductsByCategory(categoryIdList: List<UUID>, pageable: Pageable): Page<ShortProductDto>
    fun getUnavailableProducts(): List<ShortProductDto>
    fun updateProduct(userDto: UserDto, productId: UUID, productDto: ProductRequestDto): ShortProductDto
    fun createProduct(userDto: UserDto, productDto: ProductRequestDto): ShortProductDto
    fun deleteProduct(userDto: UserDto, productId: UUID): Response
}

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val productCategoryRepository: ProductCategoryRepository,
) : ProductService {
    private val log = LoggerFactory.getLogger(ProductServiceImpl::class.java)

    override fun getProducts(pageable: Pageable): Page<ShortProductDto> =
        PageImpl(
            productRepository.findAll(pageable)
                .map(ProductMapper::productToShortDto)
                .toList()
        )

    override fun getInfoAboutProduct(productId: UUID): FullProductDto =
        ProductMapper.productToFullDto(
            productRepository.findById(productId)
                .orElseThrow { ProductNotFoundException(productId) }
        )

    override fun getProductsByCategory(categoryIdList: List<UUID>, pageable: Pageable): Page<ShortProductDto> {
        val specification = ProductSpecification.isInCategory(categoryIdList)

        return PageImpl(
            productRepository.findAll(specification, pageable)
                .map(ProductMapper::productToShortDto)
                .toList()
        )
    }

    override fun getUnavailableProducts(): List<ShortProductDto> =
        productRepository.findByAvailableIsFalse()
            .map(ProductMapper::productToShortDto)

    override fun updateProduct(userDto: UserDto, productId: UUID, productDto: ProductRequestDto): ShortProductDto {
        log.info("Пользователь: ${userDto.id} обновляет продукт с id: $productId")

        val product = productRepository.findById(productId)
            .orElseThrow { ProductNotFoundException(productId) }

        val categories = productCategoryRepository.findAllById(productDto.categoryIds)

        product.name = productDto.name
        product.price = productDto.price
        // TODO: Проверка imageId
        product.imageId = productDto.imageId
        product.description = productDto.description
        product.available = productDto.available
        product.categories = categories.toSet()

        productRepository.save(product)

        return ProductMapper.productToShortDto(product)
    }

    override fun createProduct(userDto: UserDto, productDto: ProductRequestDto): ShortProductDto {
        log.info("Пользователь: ${userDto.id} добавляет продукт с ${productDto.name}")

        productRepository.findProductByName(productDto.name)
            ?.let { throw ProductAlreadyExistsException(productDto.name) }

        // TODO: Проверка imageId
        val product = ProductMapper.productRequestDtoToProduct(productDto)

        productRepository.save(product)

        return ProductMapper.productToShortDto(product)
    }

    override fun deleteProduct(userDto: UserDto, productId: UUID): Response {
        // TODO: Переделать логирование CUD методов на аспекты
        log.info("Пользователь с id: ${userDto.id} удаляет продукт с id: $productId")

        val product = productRepository.findById(productId)
            .orElseThrow { ProductNotFoundException(productId) }

        productRepository.delete(product)

        return Response(
            HttpStatus.OK.value(),
            "Продукт с id: $productId успешно удален"
        )
    }
}