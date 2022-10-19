package at.fhj

import at.fhj.core.post
import at.fhj.core.testApplicationAuthJsonClient
import at.fhj.core.testApplicationJsonClient
import at.fhj.core.validLoginCreds
import at.fhj.features.auth.dto.LoginRequestDto
import at.fhj.features.auth.dto.LoginResponseDto
import at.fhj.features.auth.dto.RefreshRequestDto
import at.fhj.features.auth.dto.RefreshResponseDto
import at.fhj.features.auth.utils.generateJWT
import at.fhj.features.auth.utils.generateRefreshToken
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun `Login with right credentials`() = testApplicationJsonClient { client ->
        var accessToken: String
        client.post("/api/auth/login", validLoginCreds)
            .apply {
                assertEquals(HttpStatusCode.OK, status)

                val dto = body<LoginResponseDto>()
                accessToken = dto.accessToken

                assert(accessToken.isNotBlank())
                assert(dto.refreshToken.isNotBlank())
            }

        client.get("/api/auth/verify") {
            bearerAuth(accessToken)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun `Fail login on wrong password`() = testApplicationJsonClient { client ->
        client.post("/api/auth/login", LoginRequestDto("admin@wlm.org", "admin2"))
            .apply {
                assertEquals(HttpStatusCode.Unauthorized, status)
            }
    }

    @Test
    fun `Fail login on wrong email`() = testApplicationJsonClient { client ->
        client.post("/api/auth/login", LoginRequestDto("invalid@email.org", "admin"))
            .apply {
                assertEquals(HttpStatusCode.Unauthorized, status)
            }
    }

    @Test
    fun `Unauthorized on wrong accessToken`() = testApplicationJsonClient { client ->
        client.get("/api/auth/verify") {
            bearerAuth("invalidAccessToken")
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }

        client.get("/api/auth/verify").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `Unauthorized on expired accessToken`() = testApplicationJsonClient { client ->
        client.get("/api/auth/verify") {
            bearerAuth(generateJWT("user", -1000))
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `Refreshing token with valid token`() = testApplicationJsonClient { client ->
        val refreshToken = client
            .post("/api/auth/login", LoginRequestDto("admin@wlm.org", "admin"))
            .body<LoginResponseDto>()
            .refreshToken

        var newAccessToken: String
        client.post("/api/auth/refresh", RefreshRequestDto(refreshToken))
            .apply {
                assertEquals(HttpStatusCode.OK, status)

                val dto = body<RefreshResponseDto>()
                assert(dto.accessToken.isNotBlank())
                assert(dto.refreshToken.isNotBlank())

                newAccessToken = dto.accessToken
            }

        client.get("/api/auth/verify") {
            bearerAuth(newAccessToken)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun `Fail refresh token with invalid token`() = testApplicationJsonClient { client ->
        client.post("/api/auth/refresh", RefreshRequestDto("imForSureNotValid"))
            .apply {
                assertEquals(HttpStatusCode.Unauthorized, status)
            }
    }

    @Test
    fun `Fail refresh token with expired token`() = testApplicationJsonClient { client ->
        val token = generateRefreshToken("user", -1000).token
        client.post("/api/auth/refresh", RefreshRequestDto(token))
            .apply {
                assertEquals(HttpStatusCode.Unauthorized, status)
            }
    }

    @Test
    fun `Test that token can only be used once`() = testApplicationJsonClient { client ->
        val refreshToken = client
            .post("/api/auth/login", LoginRequestDto("admin@wlm.org", "admin"))
            .body<LoginResponseDto>()
            .refreshToken

        client.post("/api/auth/refresh", RefreshRequestDto(refreshToken))
            .apply {
                assertEquals(HttpStatusCode.OK, status)
            }

        client.post("/api/auth/refresh", RefreshRequestDto(refreshToken))
            .apply {
                assertEquals(HttpStatusCode.Unauthorized, status)
            }
    }

    @Test
    fun `Test authenticatedJsonClient`() = testApplicationAuthJsonClient { client ->
        client.get("/api/auth/verify").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}