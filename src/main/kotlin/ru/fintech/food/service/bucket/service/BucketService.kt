package ru.fintech.food.service.bucket.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.fintech.food.service.bucket.dto.BucketDto
import ru.fintech.food.service.bucket.dto.ProductInBucketDto
import ru.fintech.food.service.bucket.entity.Bucket
import ru.fintech.food.service.bucket.exception.ProductNotInBucketException
import ru.fintech.food.service.bucket.mapper.BucketMapper
import ru.fintech.food.service.bucket.repository.BucketRepository
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.common.exception.BadRequestException
import ru.fintech.food.service.product.exception.ProductNotFoundException
import ru.fintech.food.service.product.repository.ProductRepository
import ru.fintech.food.service.user.dto.user.UserDto
import java.util.UUID

interface BucketService {
    fun addProduct(userDto: UserDto, productId: UUID, amount: Int): ProductInBucketDto
    fun deleteProduct(userDto: UserDto, productId: UUID, amount: Int): Response
    fun clearBucket(userDto: UserDto): Response
    fun getBucket(userDto: UserDto): BucketDto
}

@Service
class BucketServiceImpl(
    private val bucketRepository: BucketRepository,
    private val productRepository: ProductRepository,
) : BucketService {
    @Transactional
    override fun addProduct(userDto: UserDto, productId: UUID, amount: Int): ProductInBucketDto {
        if (amount <= 0) {
            throw BadRequestException("Количество продуктов должно быть больше 0")
        }

        val product = productRepository.findById(productId)
            .orElseThrow { ProductNotFoundException(productId) }

        var productInBucket = bucketRepository.findByUserIdAndProductId(userDto.id, productId)

        if (productInBucket != null) {
            productInBucket.quantity += amount
        } else {
            productInBucket = Bucket(
                userId = userDto.id,
                productId = product.id,
                quantity = amount
            )
        }

        bucketRepository.save(productInBucket)

        return BucketMapper.ProductInBucketDto(productInBucket, product)
    }

    @Transactional
    override fun deleteProduct(userDto: UserDto, productId: UUID, amount: Int): Response {
        if (amount <= 0) {
            throw BadRequestException("Количество продуктов должно быть больше 0")
        }

        val productInBucket = bucketRepository.findByUserIdAndProductId(userDto.id, productId)
            ?: throw ProductNotInBucketException(productId)

        if (productInBucket.quantity - amount <= 0) {
            bucketRepository.delete(productInBucket)
        } else {
            productInBucket.quantity -= amount
            bucketRepository.save(productInBucket)
        }

        return Response(
            status = HttpStatus.OK.value(),
            message = "Продукт удален"
        )
    }

    @Transactional
    override fun clearBucket(userDto: UserDto): Response {
        bucketRepository.deleteAllByUserId(userDto.id)

        return Response(
            status = HttpStatus.OK.value(),
            message = "Корзина успешно очищена"
        )
    }

    override fun getBucket(userDto: UserDto): BucketDto {
        val buckets = bucketRepository.findAllByUserId(userDto.id)

        val productsInBucket = buckets
            .map {
                val product = productRepository.findById(it.productId)
                    .orElseThrow { ProductNotFoundException(it.productId) }

                BucketMapper.ProductInBucketDto(it, product)
            }

        return BucketMapper.BucketDto(productsInBucket)
    }

}