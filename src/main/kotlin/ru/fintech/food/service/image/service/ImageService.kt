package ru.fintech.food.service.image.service

import io.minio.BucketArgs
import io.minio.BucketExistsArgs
import io.minio.GetObjectArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import org.slf4j.LoggerFactory
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.common.exception.BadRequestException
import ru.fintech.food.service.configuration.MinioProperties
import ru.fintech.food.service.image.dto.ImageDto
import ru.fintech.food.service.image.exception.ImageNotFoundException
import ru.fintech.food.service.image.exception.ImageUsedException
import ru.fintech.food.service.image.exception.UnavailableExtensionException
import ru.fintech.food.service.image.mapper.ImageMapper
import ru.fintech.food.service.image.repository.ImageRepository
import ru.fintech.food.service.product.repository.ProductRepository
import ru.fintech.food.service.user.dto.user.UserDto
import java.util.UUID

interface ImageService {
    fun uploadImage(userDto: UserDto, file: MultipartFile): ImageDto
    fun downloadImage(id: UUID): ResponseEntity<InputStreamResource>
    fun deleteImage(userDto: UserDto, imageId: UUID): Response
}

@Service
class ImageServiceImpl(
    private val minioClient: MinioClient,
    private val imageRepository: ImageRepository,
    private val productRepository: ProductRepository,
    private val minioProperties: MinioProperties
) : ImageService {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun uploadImage(userDto: UserDto, file: MultipartFile): ImageDto {
        val filename = file.originalFilename ?: throw BadRequestException("Название файла должно быть указано")
        log.info("Пользователь {} начал загрузку изображения: {}", userDto.email, filename)
        checkExtension(filename)

        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket().build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket().build())
        }

        val checkImage = imageRepository.findImageByNameContainsIgnoreCase(filename)

        if (checkImage != null) {
            throw BadRequestException("Вы уже загрузили файл с таким же названием");
        }

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket()
                .`object`(filename)
                .stream(file.inputStream, file.size, -1)
                .contentType(file.contentType)
                .build()
        )

        val image = ImageMapper.Image(file, userDto.email)
        imageRepository.save(image)

        return ImageMapper.ImageDto(image)
    }

    override fun downloadImage(id: UUID): ResponseEntity<InputStreamResource> {
        val image = imageRepository.findById(id)
            .orElseThrow { throw ImageNotFoundException(id) }

        val stream = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket()
                .`object`(image.name)
                .build()
        )

        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${image.name}")

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(InputStreamResource(stream));
    }

    @Transactional
    override fun deleteImage(userDto: UserDto, imageId: UUID): Response {
        log.info("Пользователь: {} удаляет изображение с id: {}", userDto.email, imageId)

        val image = imageRepository.findById(imageId)
            .orElseThrow { ImageNotFoundException(imageId) }

        if (productRepository.existsByImageId(imageId)) {
            throw ImageUsedException(imageId)
        }

        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket()
                .`object`(image.name)
                .build()
        )

        imageRepository.delete(image)

        return Response(
            status = HttpStatus.OK.value(),
            message = "Изображение успешно удалено"
        )
    }

    private fun checkExtension(filename: String) {
        val extension = filename
            .substringAfterLast('.', "")
            .lowercase()

        if (!minioProperties.availableExtensions.contains(extension)) {
            throw UnavailableExtensionException(extension)
        }
    }

    private fun <B : BucketArgs.Builder<B, A>, A : BucketArgs> BucketArgs.Builder<B, A>.bucket() =
        this.bucket(minioProperties.bucketName)

}