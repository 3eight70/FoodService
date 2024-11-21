package ru.fintech.food.service.restaurant.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalTime
import java.util.UUID
import ru.fintech.food.service.restaurant.dto.DayOfWeekEnum

@Entity
@Table(name = "t_working_hours")
data class WorkingHours(
    @Id
    val id: UUID = UUID.randomUUID(),
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    val restaurant: Restaurant,
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    val dayOfWeek: DayOfWeekEnum,
    @Column(name = "opening_time", nullable = false)
    var openingTime: LocalTime,
    @Column(name = "closing_time", nullable = false)
    var closingTime: LocalTime
)