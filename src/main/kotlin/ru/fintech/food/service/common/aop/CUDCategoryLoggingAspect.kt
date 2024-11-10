package ru.fintech.food.service.common.aop

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.fintech.food.service.product.dto.category.ProductCategoryRequestDto
import ru.fintech.food.service.user.dto.user.UserDto
import java.util.UUID

@Aspect
@Component
class CUDCategoryLoggingAspect {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Pointcut(
        "execution(* ru.fintech.food.service.product.service.ProductCategoryServiceImpl.create*(..)) || " +
                "execution(* ru.fintech.food.service.product.service.ProductCategoryServiceImpl.update*(..)) || " +
                "execution(* ru.fintech.food.service.product.service.ProductCategoryServiceImpl.delete*(..))"
    )
    fun serviceMethods() {
    }

    @After("serviceMethods()")
    fun logAfterMethod(joinPoint: JoinPoint) {
        val methodName = joinPoint.signature.name
        val userDto = joinPoint.args[0] as UserDto
        val category = joinPoint.args[1]

        logByCategory(userDto, methodName, category)
    }

    private fun logByCategory(userDto: UserDto, methodName: String, category: Any) =
        when (category) {
            is ProductCategoryRequestDto -> log.info(
                "Пользователь с id: {} вызвал метод: {}, для создания категории: {}",
                userDto.id,
                methodName,
                category.name
            )

            is UUID -> log.info(
                "Пользователь с id: {} вызвал метод: {} для категории с id: {}",
                userDto.id,
                methodName,
                category
            )

            else -> null
        }
}
