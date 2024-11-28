package ru.fintech.food.service.admin.exception

import ru.fintech.food.service.common.exception.BadRequestException

class UserCantChangeHimselfException : BadRequestException("Запрещено изменять свою собственную роль")