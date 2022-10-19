package at.fhj.features.measurement.dto

import at.fhj.core.Dto
import kotlinx.serialization.Serializable

@Serializable
data class AddMeasurementDto(
    val value: Double,
    val unit: String,
    val locationId: String,
    val timestamp: Long? = null // Fallback to now if not provided
) : Dto