package at.fhj.features.auth.dto

import at.fhj.core.Dto
import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequestDto(val refreshToken: String) : Dto

@Serializable
data class RefreshResponseDto(val accessToken: String, val refreshToken: String) : Dto