package org.waterlevelmonitor.backend.service

import org.slf4j.LoggerFactory
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.waterlevelmonitor.backend.domain.LocationRepository
import org.waterlevelmonitor.backend.domain.WaterLevelRepository
import org.waterlevelmonitor.backend.exceptions.LocationNotFoundException
import org.waterlevelmonitor.backend.exceptions.MaxAmountOfMeasurementsReachedException
import org.waterlevelmonitor.backend.exceptions.OutOfToleranceException
import org.waterlevelmonitor.backend.model.*
import org.waterlevelmonitor.backend.utils.DateUtil
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap

@CrossOrigin
@RestController
@RequestMapping("/api/v1/waterlevels")
class WaterLevelController(
        private val waterLevelRepository: WaterLevelRepository,
        private val locationRepository: LocationRepository
) {

    private val logger = LoggerFactory.getLogger(WaterLevelController::class.java)

    @GetMapping("/avgAllMonth")
    fun avgAllMonths(
            @RequestParam("year") year: Short,
            @RequestParam("location_id") locationId: Long
    ): Map<String, Float> {
        val map = HashMap<String, Float>()
        val monthArray = arrayListOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OKT", "NOV", "DEC")

        for (i in 1..12) {
            map[monthArray[i - 1]] = getAvgForMonth(locationId, year, i.toShort())
        }

        return map
    }

    @GetMapping("/avgLastSevenDays/{locationId}")
    fun avgLastSevenDays(@PathVariable("locationId") locationId: Long): List<WaterDateLevelDto> {
        val today = LocalDate.now()
        val list = ArrayList<WaterDateLevelDto>()

        for (i in 6 downTo 0) {
            logger.info(i.toString())
            val tmpDate = today.minusDays(i.toLong())
            val tmpDateStart = tmpDate.atStartOfDay()
            val tmpDateEnd = tmpDate.atTime(23, 59, 59)

            list.add(WaterDateLevelDto(tmpDate, waterLevelRepository.getAvgWaterLevelBetweenDates(locationId,
                    Date.from(tmpDateStart.atZone(ZoneId.systemDefault()).toInstant()),
                    Date.from(tmpDateEnd.atZone(ZoneId.systemDefault()).toInstant()))
                    ?: 0F))
        }

        return list
    }

    @GetMapping("/avgLastTwentyFourHoursPerHour/{locationId}")
    fun avgLastTwentyFourHoursPerHour(@PathVariable("locationId") locationId: Long): List<WaterDateTimeLevelDto> {
        val list = ArrayList<WaterDateTimeLevelDto>()
        val current = LocalDateTime.now()

        for (i in 0..23) {
            var dateTime = current.minusHours(i.toLong())
            dateTime = dateTime.withMinute(0)
            dateTime = dateTime.withSecond(0)
            list.add(WaterDateTimeLevelDto(dateTime, getAvgOfHour(dateTime, locationId)))
        }
        return list
    }

    private fun getAvgOfHour(dateTime: LocalDateTime, locationId: Long): Float {
        var start = LocalDateTime.from(dateTime)
        start = start.withMinute(0)
        start = start.withSecond(0)
        val sDate = Date.from(start.atZone(ZoneId.systemDefault()).toInstant())
        logger.info("sdate: $sDate")
        var end = LocalDateTime.from(dateTime)
        end = end.withMinute(59)
        end = end.withSecond(59)
        val eDate = Date.from(end.atZone(ZoneId.systemDefault()).toInstant())
        logger.info("eDate: $eDate")

        return waterLevelRepository.getAvgWaterLevelBetweenDates(locationId, sDate, eDate) ?: 0F
    }

    @GetMapping("/waterLevelsLastHour/{locationId}")
    fun waterLevelsLastHour(@PathVariable("locationId") locationId: Long): List<WaterLevel> {
        val current = LocalDateTime.now()
        val start = current.minusHours(1)
        return waterLevelRepository.getAllWaterLevelsBetweenDateTimes(
                locationId,
                Date.from(start.atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(current.atZone(ZoneId.systemDefault()).toInstant())
        )
    }

    private fun getAvgForMonth(locationId: Long, year: Short, month: Short): Float {
        val sDate = Date.from(DateUtil.getFirstDayOfMonth(year, month).atZone(ZoneId.systemDefault()).toInstant())
        val eDate = Date.from(DateUtil.getLastDayOfMonth(year, month).atZone(ZoneId.systemDefault()).toInstant())
        logger.debug("Start date: $sDate")
        logger.debug("End date: $eDate")
        return waterLevelRepository.getAvgWaterLevelBetweenDates(
                locationId = locationId,
                startDate = sDate,
                endDate = eDate
        ) ?: 0F
    }

    @PostMapping
    fun addWaterLevelDetection(@Validated @RequestBody wl: WaterLevelDto) {
        val loc: Location = locationRepository.getLocationById(wl.locationId) ?: throw LocationNotFoundException()
        val waterlevel = wl.toDbModel(location = loc)

        val currentStartMin = LocalDateTime.now()
        val currentEndMin = LocalDateTime.from(currentStartMin)
        currentStartMin.withSecond(0)
        currentEndMin.withSecond(59)
        logger.info("Current Time: $currentStartMin")

        val res = waterLevelRepository.getAllWaterLevelsBetweenDateTimes(
                loc.id,
                Date.from(currentStartMin.atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(currentEndMin.atZone(ZoneId.systemDefault()).toInstant())
        )

        if (res.size <= 2) {
            var valid = true
            for (i in res) {
                val diff = i.level - waterlevel.level
                if (diff > 10 && diff < -10)
                    valid = false
            }
            if (valid)
                waterLevelRepository.save(waterlevel)
            else
                throw OutOfToleranceException()
        } else {
            throw MaxAmountOfMeasurementsReachedException()
        }
    }
}
