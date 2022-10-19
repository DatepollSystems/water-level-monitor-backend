package at.fhj.features.auth

import at.fhj.features.auth.dto.LoginRequestDto
import at.fhj.features.auth.dto.RefreshRequestDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {
    route("/auth") {
        postLogin()
        postRefresh()

        authenticate("jwt") {
            get("/verify") {
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

private fun Route.postLogin() {
    post("/login") {
        val requestDto = call.receive<LoginRequestDto>()
        call.respond(AuthService.login(requestDto))
    }
}

private fun Route.postRefresh() {
    post("/refresh") {
        val requestDto = call.receive<RefreshRequestDto>()
        call.respond(AuthService.refreshToken(requestDto))
    }
}