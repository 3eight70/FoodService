package ru.fintech.food.service.utils.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import java.util.UUID
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [SetStringUuidValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidUUID(
    val message: String = "Неверный формат UUID",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class SetStringUuidValidator : ConstraintValidator<ValidUUID, Set<String>> {
    override fun isValid(value: Set<String>?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrEmpty()) {
            return false
        }
        return value.all { uuidString -> isValidUuid(uuidString) }
    }

    private fun isValidUuid(uuidString: String): Boolean {
        return try {
            UUID.fromString(uuidString)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}