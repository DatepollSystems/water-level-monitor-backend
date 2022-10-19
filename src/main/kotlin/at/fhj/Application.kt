package at.fhj

import at.fhj.core.Config
import at.fhj.database.configureDB
import at.fhj.features.auth.configureAuth
import at.fhj.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = Config.port, host = Config.host) {
        config()
    }.start(wait = true)
}

fun Application.config() {
    // Database
    configureDB()

    // Endpoints
    configureRouting()

    // Plugins
    configureAuth()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureExceptionHandling()
}

