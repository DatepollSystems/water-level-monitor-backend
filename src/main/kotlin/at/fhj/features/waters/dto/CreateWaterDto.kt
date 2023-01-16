package at.fhj.features.waters.dto

import at.fhj.core.Dto
import kotlinx.serialization.Serializable

@Serializable
data class CreateWaterDto(val name: String) : Dto
