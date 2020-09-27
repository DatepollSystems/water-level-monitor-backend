package org.waterlevelmonitor.backend.model

import com.sun.istack.NotNull
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

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name ="location_id")
        val location: Location?,

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