package ru.fintech.food.service.order.exception

import ru.fintech.food.service.common.exception.BadRequestException

class BucketIsEmptyException : BadRequestException("В корзине пользователя отсутствуют товары")