package ru.fintech.food.service.common.exception

import io.jsonwebtoken.security.SignatureException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.multipart.support.MissingServletRequestPartException
import ru.fintech.food.service.common.dto.Response


@ControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(SignatureException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleSignatureException(e: SignatureException): ResponseEntity<Response> {
        log.error(e.message, e)
        return ResponseEntity(
            Response(
                HttpStatus.UNAUTHORIZED.value(),
                "Неверная подпись токена авторизации"
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
                e.message
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
                "Отсутствует header Authorization"
            ), HttpStatus.UNAUTHORIZED
        )
    }

    @ExceptionHandler(MissingServletRequestPartException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMissingServletRequestPartException(e: MissingServletRequestPartException): ResponseEntity<Response> {
        log.error(e.message, e)
        return ResponseEntity(
            Response(
                HttpStatus.BAD_REQUEST.value(),
                e.message
            ), HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(NotFoundException::class)
    fun handle(e: NotFoundException): ResponseEntity<Response> {
        return ResponseEntity(
            Response(
                HttpStatus.NOT_FOUND.value(),
                e.message
            ),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(BadRequestException::class)
    fun handle(e: BadRequestException): ResponseEntity<Response> {
        return ResponseEntity(
            Response(
                HttpStatus.BAD_REQUEST.value(),
                e.message
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ResponseEntity<Response> {
        log.error("Произошла неизвестная ошибка", e)

        return ResponseEntity(
            Response(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Что-то пошло не так"
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}