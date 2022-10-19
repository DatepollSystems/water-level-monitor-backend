package at.fhj.plugins

import at.fhj.features.auth.authRoutes
import at.fhj.features.locations.locationRoutes
import at.fhj.features.measurement.measurementRoutes
import at.fhj.features.waters.waterRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    // Make no difference between e.g. ".../api" and ".../api/"
    install(IgnoreTrailingSlash)

    routing {
        route("/api") {
            get { call.respondText("Running WaterLevelMonitor-API! ( ͡° ͜ʖ ͡°)") }
            authRoutes()
            waterRoutes()
            locationRoutes()
            measurementRoutes()
        }
    }
}
