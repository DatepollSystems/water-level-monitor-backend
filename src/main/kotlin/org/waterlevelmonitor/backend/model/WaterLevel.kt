package org.waterlevelmonitor.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.sun.istack.NotNull
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Table(name = "waterLevels")
@Entity
data class WaterLevel(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false)
        @NotNull
        val level: Int,

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "timestamp")
        val timestamp: Date,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name ="location_id")
        val location: Location?,

        @JsonIgnore
        @Version
        val version: Long? = null
)

data class WaterLevelDto(
        val level: Int,
        val locationId: Long,
        val timestamp: Date
) {
        fun toDbModel(location: Location): WaterLevel{
                return WaterLevel(
                        level = level,
                        timestamp = timestamp,
                        location = location
                )
        }
}

data class WaterDateLevelDto(
        val date: LocalDate,
        val level: Float
)

data class WaterDateTimeLevelDto(
        val date: LocalDateTime,
        val level: Float
)


