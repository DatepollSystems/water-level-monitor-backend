package org.waterlevelmonitor.backend.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.waterlevelmonitor.backend.model.WaterLevel
import org.waterlevelmonitor.backend.utils.DateUtil
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

interface WaterLevelRepository : JpaRepository<WaterLevel, Long> {

    @Query("select avg(w.level) from WaterLevel w where w.location.id = :lid and w.timestamp between :sDate and :eDate")
    fun getAvgWaterLevelBetweenDates(
            @Param("lid") locationId: Long,
            @Param("sDate") startDate: Date,
            @Param("eDate") endDate: Date): Float?

    @Query("select w from WaterLevel w where w.location.id = :lid and w.timestamp between :sTime and :eTime")
    fun getAllWaterLevelsBetweenDateTimes(
            @Param("lid") locationId: Long,
            @Param("sTime") startDate: Date,
            @Param("eTime") endDate: Date
    ): List<WaterLevel>
}