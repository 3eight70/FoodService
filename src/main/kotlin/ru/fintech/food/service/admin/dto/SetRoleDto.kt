package ru.fintech.food.service.admin.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.util.UUID
import ru.fintech.food.service.user.dto.user.RoleEnum

@Schema(description = "dto для назначения ролей")
class SetRoleDto (
    @field:Schema(description = "Идентификатор пользователя", example = "3c03e1de-2177-43b2-aecb-aa4e9ff66a23")
    @field:NotNull(message = "Идентификатор должен быть указан")
    val userId: UUID,
    @field:Schema(description = "Роль пользователя", example = "COURIER")
    @field:NotNull(message = "Роль пользователя должна быть указана")
    val roleEnum: RoleEnum
)