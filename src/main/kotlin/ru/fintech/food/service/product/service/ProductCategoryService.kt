package ru.fintech.food.service.product.service

import jakarta.transaction.Transactional
import java.util.UUID
import java.util.concurrent.CompletableFuture
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.product.dto.category.ProductCategoryDto
import ru.fintech.food.service.product.dto.category.ProductCategoryRequestDto
import ru.fintech.food.service.product.exception.ProductCategoryAlreadyExistsException
import ru.fintech.food.service.product.exception.ProductCategoryNotFoundException
import ru.fintech.food.service.product.mapper.ProductCategoryMapper
import ru.fintech.food.service.product.repository.ProductCategoryRepository
import ru.fintech.food.service.product.repository.RedisProductCategoryRepository
import ru.fintech.food.service.user.dto.user.UserDto

interface ProductCategoryService {
    fun getCategories(): CompletableFuture<List<ProductCategoryDto>>
    fun getCategoryById(categoryId: UUID): CompletableFuture<ProductCategoryDto>
    fun createCategory(
        userDto: UserDto,
        categoryRequestDto: ProductCategoryRequestDto
    ): CompletableFuture<ProductCategoryDto>

    fun updateCategory(
        userDto: UserDto,
        categoryId: UUID,
        categoryRequestDto: ProductCategoryRequestDto
    ): CompletableFuture<ProductCategoryDto>

    fun deleteCategory(userDto: UserDto, categoryId: UUID): CompletableFuture<Response>
}

@Service
class ProductCategoryServiceImpl(
    private val productCategoryRepository: ProductCategoryRepository,
    private val redisProductCategoryRepository: RedisProductCategoryRepository
) : ProductCategoryService {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun getCategories(): CompletableFuture<List<ProductCategoryDto>> =
        CompletableFuture.supplyAsync {
            val cachedCategories = redisProductCategoryRepository.getAllCategories()

            if (cachedCategories.isNotEmpty()) {
                log.info("Возвращаем кэшированные категории в размере: {}", cachedCategories.size)
                return@supplyAsync cachedCategories
            }

            val categories = productCategoryRepository.findAll()
                .map(ProductCategoryMapper::ProductCategoryDto)

            log.info("Возвращаем {} категорий из бд", categories.size)
            categories.forEach {
                redisProductCategoryRepository.saveCategory(it.id.toString(), it)
            }

            categories
        }

    override fun getCategoryById(categoryId: UUID): CompletableFuture<ProductCategoryDto> =
        productCategoryRepository.findProductCategoryById(categoryId)
            .thenCompose { category ->
                if (category == null) {
                    return@thenCompose CompletableFuture.failedFuture(ProductCategoryNotFoundException(categoryId))
                }

                return@thenCompose CompletableFuture.completedFuture(category)
            }
            .thenApply(ProductCategoryMapper::ProductCategoryDto)

    @Transactional
    override fun createCategory(
        userDto: UserDto,
        categoryRequestDto: ProductCategoryRequestDto
    ): CompletableFuture<ProductCategoryDto> =
        productCategoryRepository.findByName(categoryRequestDto.name)
            .thenCompose { checkCategory ->
                if (checkCategory != null) {
                    CompletableFuture.failedFuture(ProductCategoryAlreadyExistsException(categoryRequestDto.name))
                } else {
                    val category = ProductCategoryMapper.ProductCategory(categoryRequestDto)

                    productCategoryRepository.save(category)

                    val dto = ProductCategoryMapper.ProductCategoryDto(category)
                    redisProductCategoryRepository.saveCategory(category.id.toString(), dto)

                    CompletableFuture.completedFuture(dto)
                }
            }
            .exceptionally { e ->
                when (val cause = e.cause) {
                    is ProductCategoryAlreadyExistsException -> throw cause

                    else -> {
                        log.error("Произошла неизвестная ошибка", e)
                        throw e
                    }
                }
            }


    @Transactional
    override fun updateCategory(
        userDto: UserDto,
        categoryId: UUID,
        categoryRequestDto: ProductCategoryRequestDto
    ): CompletableFuture<ProductCategoryDto> =
        productCategoryRepository.findByName(categoryRequestDto.name)
            .thenCompose { category ->
                if (category != null) {
                    CompletableFuture.failedFuture(ProductCategoryAlreadyExistsException(categoryRequestDto.name))
                } else {
                    productCategoryRepository.findProductCategoryById(categoryId)
                        .thenApply { existingCategory ->
                            existingCategory ?: throw ProductCategoryNotFoundException(categoryId)
                        }
                        .thenApply { existingCategory ->
                            existingCategory.name = categoryRequestDto.name
                            productCategoryRepository.save(existingCategory)
                            ProductCategoryMapper.ProductCategoryDto(existingCategory)
                        }
                        .thenApply { dto ->
                            redisProductCategoryRepository.saveCategory(categoryId.toString(), dto)
                            dto
                        }
                }
            }


    @Transactional
    override fun deleteCategory(userDto: UserDto, categoryId: UUID): CompletableFuture<Response> =
        productCategoryRepository.findProductCategoryById(categoryId)
            .thenCompose { category ->
                if (category == null) {
                    return@thenCompose CompletableFuture.failedFuture(ProductCategoryNotFoundException(categoryId))
                }

                productCategoryRepository.delete(category)
                redisProductCategoryRepository.deleteCategory(categoryId.toString())

                return@thenCompose CompletableFuture.completedFuture(
                    Response(
                        status = 200,
                        message = "Категория успешно удалена",
                    )
                )
            }
}