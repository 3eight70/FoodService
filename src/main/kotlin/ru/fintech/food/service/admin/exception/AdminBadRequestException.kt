package ru.fintech.food.service.admin.exception

import ru.fintech.food.service.common.exception.BadRequestException

class AdminBadRequestException : BadRequestException("Нельзя назначать пользователей на роль администратора данным путем")