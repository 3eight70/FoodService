package ru.fintech.food.service.image.exception

import ru.fintech.food.service.common.exception.BadRequestException

class UnavailableExtensionException(extension: String)
    : BadRequestException("В файле использовано недопустимое расширение: $extension")