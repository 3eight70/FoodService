package ru.fintech.food.service.common.aop

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.fintech.food.service.product.dto.product.ProductRequestDto
import ru.fintech.food.service.user.dto.user.UserDto
import java.util.UUID

@Aspect
@Component
class CUDProductLoggingAspect {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Pointcut(
        "execution(* ru.fintech.food.service.product.service.ProductServiceImpl.create*(..)) || " +
                "execution(* ru.fintech.food.service.product.service.ProductServiceImpl.update*(..)) || " +
                "execution(* ru.fintech.food.service.product.service.ProductServiceImpl.delete*(..))"
    )
    fun serviceMethods() {
    }

    @After("serviceMethods()")
    fun logAfterMethod(joinPoint: JoinPoint) {
        val methodName = joinPoint.signature.name
        val userDto = joinPoint.args[0] as UserDto
        val product = joinPoint.args[1]

        logByProduct(userDto, methodName, product)
    }

    private fun logByProduct(userDto: UserDto, methodName: String, product: Any) =
        when (product) {
            is ProductRequestDto -> log.info(
                "Пользователь с id: {} вызвал метод: {}, для создания продукта: {}",
                userDto.id,
                methodName,
                product.name
            )

            is UUID -> log.info(
                "Пользователь с id: {} вызвал метод: {} для продукта с id: {}",
                userDto.id,
                methodName,
                product
            )

            else -> null
        }
}