package ru.fintech.food.service.image.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.fintech.food.service.common.SwaggerConfig.Companion.BEARER_AUTH
import ru.fintech.food.service.image.dto.ImageDto
import ru.fintech.food.service.image.service.ImageService
import ru.fintech.food.service.user.dto.user.UserDto

@RestController
@RequestMapping("/v1/image")
@Tag(name = "Изображения", description = "Отвечает за работу с изображениями")
class ImageController(
    private val imageService: ImageService
) {
    @Operation(
        summary = "Получение изображения",
        description = "Позволяет получить изображению по идентификатору"
    )
    @GetMapping("/{id}")
    fun uploadImage(
        @PathVariable("id") @Parameter(description = "Идентификатор файла") id: UUID
    ): ResponseEntity<InputStreamResource> = imageService.downloadImage(id)

    @Operation(
        summary = "Загрузка изображения",
        description = "Позволяет сохранить изображение в хранилище",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(
        @AuthenticationPrincipal userDto: UserDto,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<ImageDto> = ResponseEntity.ok(imageService.uploadImage(userDto, file))

    @Operation(
        summary = "Удаление изображения",
        description = "Позволяет удлить изображение из хранилища",
        security = [SecurityRequirement(name = BEARER_AUTH)]
    )
    @DeleteMapping("/{id}")
    fun deleteImage(
        @AuthenticationPrincipal userDto: UserDto,
        @PathVariable("id") @Parameter(description = "Идентификатор изображения") id: UUID
    ) = ResponseEntity.ok(imageService.deleteImage(userDto, id))
}