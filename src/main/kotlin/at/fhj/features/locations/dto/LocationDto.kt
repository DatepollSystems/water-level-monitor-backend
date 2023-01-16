package at.fhj.features.locations.dto

import at.fhj.core.Dto
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val id: String,
    var name: String,
    val city: String,
    val waterId: String,
) : Dto
