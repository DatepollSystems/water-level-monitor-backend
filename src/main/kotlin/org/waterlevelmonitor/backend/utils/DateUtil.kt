package org.waterlevelmonitor.backend.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import javax.validation.constraints.Max
import javax.validation.constraints.Min

class DateUtil {
    companion object {
        fun getFirstDayOfMonth(
                @Min(1900)
                @Max(2100)
                year: Short,

                @Min(1)
                @Max(2)
                month: Short): LocalDateTime {

            return LocalDate.of(year.toInt(), month.toInt(), 1).atTime(LocalTime.MIN)
        }

        fun getLastDayOfMonth(
                @Min(1900)
                @Max(2100)
                year: Short,

                @Min(1)
                @Max(2)
                month: Short): LocalDateTime {
            return LocalDate.of(year.toInt(), month.toInt(), 1).with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX)
        }
    }
}