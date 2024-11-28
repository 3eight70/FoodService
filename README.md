# Курсовая для финтех курса

## Краткое описание
Сервис для сети ресторанов доставки еды, позволяет добавлять в систему рестораны, курьеров, операторов ресторанов, ну и пользователей.

Администраторы могут конфигурировать рестораны, назначать рабочие часы.

Операторы ресторанов следят за заказами, подтверждают/отклоняют заказы.

Пользователи могут делать заказы на выбранные позиции в указанное время дня.

## Инфраструктура
- Redis - используется для работы с токенами аутентификации и кэшированием позиций меню и категорий меню
- Minio - используется для хранения изображений позиций меню

## Как тестировать

1. Поднимите всю инфраструктуру, для этого в папке "local" лежит docker-compose.yml, его запустить можно при помощи
   команды ```docker-compose up -d```
2. Поднимите приложение

Swagger: http://localhost:8080/swagger-ui/index.html#/