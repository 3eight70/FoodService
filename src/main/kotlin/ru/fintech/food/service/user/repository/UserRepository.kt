package ru.fintech.food.service.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.fintech.food.service.user.entity.User
import java.util.UUID

interface UserRepository: JpaRepository<User, UUID> {
}