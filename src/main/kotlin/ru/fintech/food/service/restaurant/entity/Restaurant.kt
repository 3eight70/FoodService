package ru.fintech.food.service.restaurant.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "t_restaurants")
data class Restaurant (
    @Id
    val id: UUID = UUID.randomUUID(),
    @Column(name = "name", nullable = false)
    var name: String,
    // По хорошему использовать ГАР или же свою таблицу адресов
    @Column(name = "address", nullable = false)
    var address: String
)