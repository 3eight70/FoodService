package ru.fintech.food.service.common.exception

import io.jsonwebtoken.security.SignatureException
import io.minio.errors.MinioException
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.DisabledException
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.multipart.support.MissingServletRequestPartException
import ru.fintech.food.service.common.dto.ErrorDto
import ru.fintech.food.service.common.dto.ErrorResponse
import ru.fintech.food.service.common.dto.Response


@ControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        log.debug("Ошибка валидации: {}", e.bindingResult.allErrors.map { it.defaultMessage }, e)

        val errors: MutableList<ErrorDto> = mutableListOf()
        for (error in e.bindingResult.fieldErrors) {
            val fieldName = error.field
            val errorMessage = error.defaultMessage ?: "Ошибка валидации"
            errors.add(ErrorDto(fieldName, errorMessage))
        }

        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = "Ошибка валидации",
            errors = errors
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }


    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val errors: MutableList<ErrorDto> = mutableListOf()
        for (violation in e.constraintViolations) {
            errors.add(ErrorDto(violation.propertyPath.toString(), violation.message))
        }

        val errorResponse = ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Ошибка валидации",
            errors = errors
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<Response> {
        val errorResponse = Response(
            HttpStatus.BAD_REQUEST.value(),
            "Ошибка в теле запроса: ${e.localizedMessage}",
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DisabledException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleDisabledException(e: DisabledException): ResponseEntity<Response> =
        ResponseEntity(
            Response(
                HttpStatus.UNAUTHORIZED.value(),
                "Сперва подтвердите свой аккаунт",
            ), HttpStatus.UNAUTHORIZED
        )

    @ExceptionHandler(SignatureException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleSignatureException(e: SignatureException): ResponseEntity<Response> {
        log.error(e.message, e)
        return ResponseEntity(
            Response(
                HttpStatus.UNAUTHORIZED.value(),
                "Неверная подпись токена авторизации",
            ), HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMediaTypeNotAcceptableException(e: HttpMediaTypeNotAcceptableException): ResponseEntity<Response> {
        log.error(e.message, e)
        return ResponseEntity(
            Response(
                HttpStatus.BAD_REQUEST.value(),
                e.message,
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(MissingRequestHeaderException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleMissingRequestHeader(e: MissingRequestHeaderException): ResponseEntity<Response> {
        log.error(e.message, e)
        return ResponseEntity(
            Response(
                HttpStatus.UNAUTHORIZED.value(),
                "Отсутствует header Authorization",
            ), HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(MinioException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleMinioException(e: MinioException): ResponseEntity<Response> {
        log.error("При загрузке файла в minio что-то пошло не так", e)

        return ResponseEntity(
            Response(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.message,
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @ExceptionHandler(MissingServletRequestPartException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMissingServletRequestPartException(e: MissingServletRequestPartException): ResponseEntity<Response> {
        log.error(e.message, e)
        return ResponseEntity(
            Response(
                HttpStatus.BAD_REQUEST.value(),
                e.message,
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(NotFoundException::class)
    fun handle(e: NotFoundException): ResponseEntity<Response> {
        return ResponseEntity(
            Response(
                HttpStatus.NOT_FOUND.value(),
                e.message,
            ),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(BadRequestException::class)
    fun handle(e: BadRequestException): ResponseEntity<Response> {
        return ResponseEntity(
            Response(
                HttpStatus.BAD_REQUEST.value(),
                e.message,
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ResponseEntity<Response> {
        log.error("Произошла неожиданная ошибка", e)

        return ResponseEntity(
            Response(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Что-то пошло не так",
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}