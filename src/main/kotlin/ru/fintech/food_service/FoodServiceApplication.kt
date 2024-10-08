package ru.fintech.food_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FoodServiceApplication

fun main(args: Array<String>) {
	runApplication<FoodServiceApplication>(*args)
}
