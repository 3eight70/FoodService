package ru.fintech.food.service.restaurant.service

import java.util.UUID
import org.springframework.stereotype.Service
import ru.fintech.food.service.restaurant.dto.workinghours.WorkingHoursCreateDto
import ru.fintech.food.service.restaurant.dto.workinghours.WorkingHoursDto
import ru.fintech.food.service.restaurant.dto.workinghours.WorkingHoursUpdateDto
import ru.fintech.food.service.restaurant.exception.RestaurantNotFoundException
import ru.fintech.food.service.restaurant.exception.WorkingHoursAlreadyExistsException
import ru.fintech.food.service.restaurant.exception.WorkingHoursBadRequestException
import ru.fintech.food.service.restaurant.mapper.WorkingHoursMapper
import ru.fintech.food.service.restaurant.repository.RestaurantRepository
import ru.fintech.food.service.restaurant.repository.WorkingHoursRepository
import ru.fintech.food.service.user.dto.user.UserDto

interface WorkingHoursService {
    fun createWorkingHours(userDto: UserDto, workingHoursCreateDto: WorkingHoursCreateDto): WorkingHoursDto
    fun updateWorkingHours(userDto: UserDto, id: UUID, workingHoursUpdateDto: WorkingHoursUpdateDto): WorkingHoursDto
    fun getRestaurantWorkingHours(restaurantId: UUID): List<WorkingHoursDto>
}

@Service
class WorkingHoursServiceImpl(
    private val workingHoursRepository: WorkingHoursRepository,
    private val restaurantRepository: RestaurantRepository
) : WorkingHoursService {
    override fun createWorkingHours(userDto: UserDto, workingHoursCreateDto: WorkingHoursCreateDto): WorkingHoursDto {
        if (workingHoursCreateDto.closingTime.isBefore(workingHoursCreateDto.openingTime)){
            throw WorkingHoursBadRequestException("Время закрытия должно быть после времени открытия")
        }

        if (checkIfWorkingHoursAlreadyExists(workingHoursCreateDto)) {
            throw WorkingHoursAlreadyExistsException(
                workingHoursCreateDto.restaurantId,
                workingHoursCreateDto.dayOfWeek
            )
        }

        val restaurant = restaurantRepository.findById(workingHoursCreateDto.restaurantId)
            .orElseThrow { RestaurantNotFoundException(workingHoursCreateDto.restaurantId) }

        val workingHours = WorkingHoursMapper.WorkingHours(
            restaurant = restaurant,
            workingHoursCreateDto = workingHoursCreateDto,
        )

        workingHoursRepository.save(workingHours)

        return WorkingHoursMapper.WorkingHoursDto(workingHours)
    }

    override fun updateWorkingHours(
        userDto: UserDto,
        id: UUID,
        workingHoursUpdateDto: WorkingHoursUpdateDto
    ): WorkingHoursDto {
        if (workingHoursUpdateDto.closingTime.isBefore(workingHoursUpdateDto.openingTime)){
            throw WorkingHoursBadRequestException("Время закрытия должно быть после времени открытия")
        }

        val workingHours = workingHoursRepository.findById(id)
            .orElseThrow { RestaurantNotFoundException(id) }

        workingHours.closingTime = workingHoursUpdateDto.closingTime
        workingHours.openingTime = workingHoursUpdateDto.openingTime

        workingHoursRepository.save(workingHours)

        return WorkingHoursMapper.WorkingHoursDto(workingHours)
    }

    override fun getRestaurantWorkingHours(restaurantId: UUID): List<WorkingHoursDto> =
        workingHoursRepository.findWorkingHoursByRestaurantId(restaurantId)
            .map(WorkingHoursMapper::WorkingHoursDto)

    private fun checkIfWorkingHoursAlreadyExists(workingHoursCreateDto: WorkingHoursCreateDto): Boolean {
        val checkHours = workingHoursRepository.findWorkingHoursByRestaurantIdAndDayOfWeek(
            restaurantId = workingHoursCreateDto.restaurantId,
            dayOfWeekEnum = workingHoursCreateDto.dayOfWeek
        )

        return checkHours != null
    }
}