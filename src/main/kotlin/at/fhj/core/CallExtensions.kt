package at.fhj.core

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend inline fun <reified T : Any> ApplicationCall.respondOrNotFound(message: T?) {
    if (message == null) {
        this.respond(HttpStatusCode.NotFound)
    } else {
        this.respond(HttpStatusCode.OK, message)
    }
}