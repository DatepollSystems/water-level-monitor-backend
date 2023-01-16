package at.fhj.features.measurement.dto

import at.fhj.core.Dto
import kotlinx.serialization.Serializable

@Serializable
data class MeasurementDto(
    val id: String,
    val value: Double,
    val unit: String,
    var locationId: String,
    val timestamp: Long? = null
) : Dto
