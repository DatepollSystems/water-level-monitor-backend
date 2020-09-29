package org.waterlevelmonitor.backend.service

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.waterlevelmonitor.backend.domain.WaterLevelRepository
import org.waterlevelmonitor.backend.model.WaterDateLevelDto
import org.waterlevelmonitor.backend.model.WaterLevelDto
import org.waterlevelmonitor.backend.utils.DateUtil
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap


@RestController
@RequestMapping("/waterlevels")
class WaterLevelController(private val waterLevelRepository: WaterLevelRepository) {

    private val logger = LoggerFactory.getLogger(WaterLevelController::class.java)

    @GetMapping("/avgAllMonth")
    fun avgAllMonths(
            @RequestParam("year") year: Short,
            @RequestParam("location_id") locationId: Long
    ): Map<Int, Float> {
        val map = HashMap<Int, Float>()

        for (i in 1..12) {
            map[i] = getAvgForMonth(locationId, year, i.toShort())
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

            list.add(WaterDateLevelDto(tmpDate,waterLevelRepository.getAvgWaterLevelBetweenDates(locationId,
                    Date.from(tmpDateStart.atZone(ZoneId.systemDefault()).toInstant()),
                    Date.from(tmpDateEnd.atZone(ZoneId.systemDefault()).toInstant()))
                    ?: 0F))
        }


        return list
    }

    @GetMapping("/avgLastTwentyFourHours/{locationId}")
    fun avgLastTwentyFourHours(@PathVariable("locationId") locationId: Long){
        TODO()
    }

    @GetMapping("/waterLevelsLastHour/{locationId}")
    fun waterLevelsLastHour(@PathVariable("locationId") locationId: Long){
        TODO()
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
}
