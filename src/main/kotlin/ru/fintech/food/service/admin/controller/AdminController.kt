package ru.fintech.food.service.admin.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.fintech.food.service.admin.dto.SetRoleDto
import ru.fintech.food.service.admin.service.AdminService
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.user.dto.user.UserDto

@RestController
@Tag(name = "Админка", description = "Отвечает за назначение ролей в системе")
@RequestMapping("/v1/admin")
class AdminController(
    private val adminService: AdminService
) {
    @PutMapping("/user")
    fun setRole(
        @AuthenticationPrincipal userDto: UserDto,
        @RequestBody setRoleDto: SetRoleDto
    ): ResponseEntity<Response> = ResponseEntity.ok(adminService.setUserRole(setRoleDto))
}