package ru.fintech.food.service.restaurant.service

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.fintech.food.service.common.dto.Response
import ru.fintech.food.service.restaurant.dto.ConcreteTime
import ru.fintech.food.service.restaurant.dto.DayOfWeekEnum
import ru.fintech.food.service.restaurant.dto.restaurant.RestaurantDto
import ru.fintech.food.service.restaurant.dto.restaurant.RestaurantRequestDto
import ru.fintech.food.service.restaurant.exception.RestaurantNotFoundException
import ru.fintech.food.service.restaurant.mapper.RestaurantMapper
import ru.fintech.food.service.restaurant.repository.RestaurantRepository
import ru.fintech.food.service.restaurant.repository.WorkingHoursRepository
import ru.fintech.food.service.user.dto.user.UserDto

interface RestaurantService {
    fun getAllRestaurants(): List<RestaurantDto>
    fun getRestaurantsAvailableInConcreteTime(
        concreteTime: ConcreteTime,
        dayOfWeekEnum: DayOfWeekEnum
    ): List<RestaurantDto>

    fun createRestaurant(user: UserDto, restaurantRequestDto: RestaurantRequestDto): RestaurantDto
    fun updateRestaurant(user: UserDto, id: UUID, restaurantRequestDto: RestaurantRequestDto): RestaurantDto
    fun deleteRestaurant(user: UserDto, id: UUID): Response
}

@Service
class RestaurantServiceImpl(
    private val workingHoursRepository: WorkingHoursRepository,
    private val restaurantRepository: RestaurantRepository
) : RestaurantService {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    override fun getAllRestaurants(): List<RestaurantDto> =
        restaurantRepository.findAll()
            .map(RestaurantMapper::RestaurantDto)

    override fun getRestaurantsAvailableInConcreteTime(
        concreteTime: ConcreteTime,
        dayOfWeekEnum: DayOfWeekEnum
    ): List<RestaurantDto> {
        val workingHours = workingHoursRepository.findByDayOfWeek(dayOfWeekEnum)
        val time = LocalTime.parse(concreteTime.time, formatter)

        val restaurants = workingHours
            .filter {
                (it.openingTime.isBefore(time) || it.openingTime == time) &&
                        (it.closingTime.isAfter(time) || it.closingTime == time)
            }
            .map { it.restaurant }
            .toSet()

        return restaurants
            .map(RestaurantMapper::RestaurantDto)
    }

    @Transactional
    override fun createRestaurant(user: UserDto, restaurantRequestDto: RestaurantRequestDto): RestaurantDto {
        val restaurant = RestaurantMapper.Restaurant(restaurantRequestDto)

        restaurantRepository.save(restaurant)

        return RestaurantMapper.RestaurantDto(restaurant)
    }

    @Transactional
    override fun updateRestaurant(user: UserDto, id: UUID, restaurantRequestDto: RestaurantRequestDto): RestaurantDto {
        val restaurant = restaurantRepository.findById(id)
            .orElseThrow { RestaurantNotFoundException(id) }

        restaurant.name = restaurantRequestDto.name
        restaurant.address = restaurantRequestDto.address

        restaurantRepository.save(restaurant)

        return RestaurantMapper.RestaurantDto(restaurant)
    }

    @Transactional
    override fun deleteRestaurant(user: UserDto, id: UUID): Response {
        val restaurant = restaurantRepository.findById(id)
            .orElseThrow { RestaurantNotFoundException(id) }

        workingHoursRepository.deleteAllByRestaurant(restaurant)
        restaurantRepository.delete(restaurant)

        return Response(
            status = HttpStatus.OK.value(),
            message = "Ресторан успешно удален"
        )
    }
}