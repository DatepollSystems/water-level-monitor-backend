package at.fhj.features.waters.dto

import at.fhj.core.Dto
import kotlinx.serialization.Serializable

@Serializable
data class WaterDto(
    val id: String,
    var name: String
) : Dto
