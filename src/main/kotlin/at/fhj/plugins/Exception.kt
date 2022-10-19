package at.fhj.plugins

import at.fhj.exceptions.HttpUnauthorizedException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException

fun Application.configureExceptionHandling() {
    install(StatusPages) {
        // Only register one exception handler as order of handlers for inheritance is not guaranteed/specified (uses find instance of on hashMap key)
        exception<Throwable> { call, cause ->
            // Make sure that comparisons are done from specific to abstract (inheritance)
            when (cause) {
                is HttpUnauthorizedException -> {
                    call.response.header("WWW-Authenticate", "Bearer")
                    call.respond(HttpStatusCode.Unauthorized, cause.message ?: HttpStatusCode.Unauthorized.description)
                }
                is CannotTransformContentToTypeException, is SerializationException -> {
                    call.respond(HttpStatusCode.BadRequest, "Unexpected JSON format")
                }
                else -> {
                    logError(call, cause)
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}