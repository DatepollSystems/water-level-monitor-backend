package at.fhj.features.auth.dto

import at.fhj.core.Dto
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(val email: String, val password: String) : Dto

@Serializable
data class LoginResponseDto(val accessToken: String, val refreshToken: String) : Dto
