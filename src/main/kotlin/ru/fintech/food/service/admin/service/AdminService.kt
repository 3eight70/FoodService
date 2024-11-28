package ru.fintech.food.service.admin.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.fintech.food.service.admin.dto.SetRoleDto
import ru.fintech.food.service.admin.exception.AdminBadRequestException
import ru.fintech.food.service.admin.exception.UserCantChangeHimselfException
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.common.exception.NotFoundException
import ru.fintech.food.service.user.dto.user.RoleEnum
import ru.fintech.food.service.user.dto.user.UserDto
import ru.fintech.food.service.user.repository.UserRepository

interface AdminService {
    fun setUserRole(userDto: UserDto, setRoleDto: SetRoleDto): Response
}

@Service
class AdminServiceImpl(
    private val userRepository: UserRepository
) : AdminService {
    @Transactional
    override fun setUserRole(userDto: UserDto, setRoleDto: SetRoleDto): Response {
        if (setRoleDto.roleEnum == RoleEnum.ADMIN) {
            throw AdminBadRequestException();
        }

        if (setRoleDto.userId == userDto.id){
            throw UserCantChangeHimselfException();
        }

        val user = userRepository.findById(setRoleDto.userId)
            .orElseThrow { NotFoundException("Пользователь с id: ${setRoleDto.userId} не найден") }

        user.role = setRoleDto.roleEnum

        userRepository.save(user)

        return Response(
            status = HttpStatus.OK.value(),
            message = "Роль пользователя успешно изменена"
        )
    }
}