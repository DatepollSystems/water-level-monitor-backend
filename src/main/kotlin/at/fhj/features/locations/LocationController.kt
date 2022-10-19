package at.fhj.features.locations

import at.fhj.core.respondOrNotFound
import at.fhj.core.toResponse
import at.fhj.features.locations.dto.CreateLocationDto
import at.fhj.features.locations.dto.LocationDto
import at.fhj.features.measurement.MeasurementService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.locationRoutes() {
    route("/locations") {
        getAllLocations()
        getLocationById()
        getMeasurementsForLocation()

        authenticate("jwt") {
            updateLocation()
            createLocation()
            deleteLocationById()
        }
    }
}

private fun Route.getAllLocations() {
    get {
        call.respond(HttpStatusCode.OK, LocationService.getLocations().toResponse())
    }
}

private fun Route.getLocationById() {
    get("/{id}") {
        call.respondOrNotFound(LocationService.getLocation(call.parameters["id"]!!)?.toResponse())
    }
}

private fun Route.getMeasurementsForLocation() {
    get("/{id}/measurements") {
        call.respond(
            HttpStatusCode.OK,
            MeasurementService.getMeasurementsForLocation(call.parameters["id"]!!).toResponse()
        )
    }
}

private fun Route.createLocation() {
    post {
        val body = call.receive<CreateLocationDto>()
        val location = LocationService.addLocation(body)
        call.respond(HttpStatusCode.Created, location.toResponse())
    }
}

private fun Route.updateLocation() {
    put {
        val body = call.receive<LocationDto>()
        val location = LocationService.updateLocation(body)
        call.respondOrNotFound(location?.toResponse())
    }
}

private fun Route.deleteLocationById() {
    delete("/{id}") {
        LocationService.deleteLocation(call.parameters["id"]!!)
        call.respond(HttpStatusCode.NoContent)
    }
}
