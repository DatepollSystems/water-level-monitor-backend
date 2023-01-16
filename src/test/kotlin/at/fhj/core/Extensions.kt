package at.fhj.core

import at.fhj.config
import at.fhj.features.auth.dto.LoginRequestDto
import at.fhj.features.auth.dto.LoginResponseDto
import at.fhj.features.auth.dto.RefreshRequestDto
import at.fhj.features.auth.dto.RefreshResponseDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*

val validLoginCreds = LoginRequestDto("admin@wlm.org", "admin")

fun ApplicationTestBuilder.createJsonClient() = this.createClient {
    install(ContentNegotiation) {
        json()
    }
}

fun ApplicationTestBuilder.createAuthenticatedJsonClient() = this.createClient {
    install(ContentNegotiation) {
        json()
    }
    install(Auth) {
        bearer {
            loadTokens {
                val tokens = this@createAuthenticatedJsonClient.createJsonClient()
                    .post("api/auth/login", validLoginCreds)
                    .body<LoginResponseDto>()

                BearerTokens(tokens.accessToken, tokens.refreshToken)
            }
            refreshTokens {
                val tokens = client.post("/api/auth/refresh", RefreshRequestDto(oldTokens!!.refreshToken))
                    .body<RefreshResponseDto>()

                BearerTokens(tokens.accessToken, tokens.refreshToken)
            }
        }
    }
}


fun testApplicationJsonClient(
    block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit
) = testApplication {
    application { config() }
    block(createJsonClient())
}

fun testApplicationAuthJsonClient(
    block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit
) = testApplication {
    application { config() }
    block(createAuthenticatedJsonClient())
}

suspend inline fun <reified T : Any> HttpClient.post(url: String, jsonBody: T) = this.post(urlString = url) {
    contentType(ContentType.Application.Json)
    setBody(jsonBody)
}

suspend inline fun <reified T : Any> HttpClient.put(url: String, jsonBody: T) = this.put(urlString = url) {
    contentType(ContentType.Application.Json)
    setBody(jsonBody)
}