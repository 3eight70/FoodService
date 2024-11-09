package ru.fintech.food.service.product.service

import java.util.UUID
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
import ru.fintech.food.service.product.exception.ProductCategoryNotFoundException
import ru.fintech.food.service.product.exception.ProductNotFoundException
import ru.fintech.food.service.product.mapper.ProductMapper
import ru.fintech.food.service.product.repository.ProductCategoryRepository
import ru.fintech.food.service.product.repository.ProductRepository
import ru.fintech.food.service.product.repository.RedisProductRepository
import ru.fintech.food.service.user.dto.user.UserDto

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
    private val redisProductRepository: RedisProductRepository
) : ProductService {
    private val log = LoggerFactory.getLogger(ProductServiceImpl::class.java)

    override fun getProducts(pageable: Pageable): Page<ShortProductDto> {
        val cachedProducts = redisProductRepository.getAllMenuItems()

        if (cachedProducts.isNotEmpty()) {
            val shortProductDtos = cachedProducts.map { ProductMapper.fullProductDtoToShort(it) }
            log.debug("Возвращаем кэшированные значения в размере: {}", shortProductDtos.size)

            return PageImpl(shortProductDtos, pageable, shortProductDtos.size.toLong())
        }

        val products = productRepository.findAll(pageable)

        val shortProductDtos = products.content.map { ProductMapper.productToShortDto(it) }
        log.debug("Возвращаем значения из бд в размере: {}", products.size)

        products.content.forEach { product ->
            val fullProductDto = ProductMapper.productToFullDto(product)
            redisProductRepository.saveMenuItem(product.id.toString(), fullProductDto)
        }

        return PageImpl(shortProductDtos, pageable, products.totalElements)
    }

    override fun getInfoAboutProduct(productId: UUID): FullProductDto {
        val cachedProduct = redisProductRepository.getMenuItem(productId.toString())
        if (cachedProduct != null) {
            log.debug("Возвращаем кэшированный продукт с id: {}", productId)
            return cachedProduct
        }

        return ProductMapper.productToFullDto(
            productRepository.findById(productId)
                .orElseThrow { ProductNotFoundException(productId) }
        )
    }

    override fun getProductsByCategory(categoryIdList: List<UUID>, pageable: Pageable): Page<ShortProductDto> {
        val products = productRepository.findByCategoryIdIn(categoryIdList, pageable)

        return PageImpl(
            products.content
                .map(ProductMapper::productToShortDto),
            pageable,
            products.totalElements
        )
    }

    override fun getUnavailableProducts(): List<ShortProductDto> =
        productRepository.findByAvailableIsFalse()
            .map(ProductMapper::productToShortDto)

    override fun updateProduct(userDto: UserDto, productId: UUID, productDto: ProductRequestDto): ShortProductDto {
        val product = productRepository.findById(productId)
            .orElseThrow { ProductNotFoundException(productId) }

        validateCategoryIds(productDto)

        val categories = productCategoryRepository.findAllById(productDto.categoryIds.map { UUID.fromString(it) })

        product.name = productDto.name
        product.price = productDto.price
        // TODO: Проверка imageId
        product.imageId = productDto.imageId
        product.description = productDto.description
        product.available = productDto.available
        product.categories = HashSet(categories)

        productRepository.save(product)

        val updatedProductDto = ProductMapper.productToFullDto(product)
        redisProductRepository.saveMenuItem(productId.toString(), updatedProductDto)

        return ProductMapper.fullProductDtoToShort(updatedProductDto)
    }

    override fun createProduct(userDto: UserDto, productDto: ProductRequestDto): ShortProductDto {
        productRepository.findProductByName(productDto.name)
            ?.let { throw ProductAlreadyExistsException(productDto.name) }

        validateCategoryIds(productDto)

        // TODO: Проверка imageId
        val product = ProductMapper.productRequestDtoToProduct(productDto)

        productRepository.save(product)

        val createdProductDto = ProductMapper.productToFullDto(product)
        redisProductRepository.saveMenuItem(product.id.toString(), createdProductDto)

        return ProductMapper.fullProductDtoToShort(createdProductDto)
    }

    override fun deleteProduct(userDto: UserDto, productId: UUID): Response {
        val product = productRepository.findById(productId)
            .orElseThrow { ProductNotFoundException(productId) }

        productRepository.delete(product)

        redisProductRepository.deleteMenuItem(productId.toString())

        return Response(
            HttpStatus.OK.value(),
            "Продукт с id: $productId успешно удален",
        )
    }

    private fun validateCategoryIds(productDto: ProductRequestDto) {
        productDto.categoryIds.forEach {
            val exists = productCategoryRepository.existsById(UUID.fromString(it))

            if (!exists) {
                throw ProductCategoryNotFoundException(UUID.fromString(it))
            }
        }
    }
}