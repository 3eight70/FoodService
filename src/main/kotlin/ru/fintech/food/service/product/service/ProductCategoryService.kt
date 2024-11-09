package ru.fintech.food.service.product.service

import java.util.UUID
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
    private val productCategoryRepository: ProductCategoryRepository,
    private val redisProductCategoryRepository: RedisProductCategoryRepository
) : ProductCategoryService {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun getCategories(): List<ProductCategoryDto> {
        val cachedCategories = redisProductCategoryRepository.getAllCategories()

        if (cachedCategories.isNotEmpty()) {
            log.info("Возвращаем кэшированные категории в размере: {}", cachedCategories.size)
            return cachedCategories
        }

        val categories = productCategoryRepository.findAll()
            .map(ProductCategoryMapper::toProductCategoryDto)

        log.info("Возвращаем {} категорий из бд", categories.size)
        categories.forEach {
            redisProductCategoryRepository.saveCategory(it.id.toString(), it)
        }

        return categories
    }

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

        val dto = ProductCategoryMapper.toProductCategoryDto(category)
        redisProductCategoryRepository.saveCategory(category.id.toString(), dto)

        return dto
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

        val dto = ProductCategoryMapper.toProductCategoryDto(category)
        redisProductCategoryRepository.saveCategory(categoryId.toString(), dto)

        return dto
    }

    override fun deleteCategory(userDto: UserDto, categoryId: UUID): Response {
        val category = productCategoryRepository.findById(categoryId)
            .orElseThrow { ProductCategoryNotFoundException(categoryId) }

        productCategoryRepository.delete(category)
        redisProductCategoryRepository.deleteCategory(categoryId.toString())

        return Response(
            status = 200,
            message = "Категория успешно удалена",
        )
    }
}