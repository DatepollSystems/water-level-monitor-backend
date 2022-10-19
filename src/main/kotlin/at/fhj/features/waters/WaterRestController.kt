package at.fhj.features.waters

import at.fhj.core.respondOrNotFound
import at.fhj.core.toResponse
import at.fhj.features.locations.LocationService
import at.fhj.features.waters.dto.CreateWaterDto
import at.fhj.features.waters.dto.WaterDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.waterRoutes() {
    route("/waters") {
        getWaters()
        getWaterById()
        getLocationsForWater()

        authenticate("jwt") {
            createWater()
            updateWater()
            deleteWater()
        }
    }
}

private fun Route.getWaters() {
    get {
        call.respond(HttpStatusCode.OK, WaterService.getWaters().toResponse())
    }
}

private fun Route.getWaterById() {
    get("/{id}") {
        call.respondOrNotFound(WaterService.getWaterById(call.parameters["id"]!!)?.toResponse())
    }
}

private fun Route.getLocationsForWater() {
    get("/{id}/locations") {
        call.respond(HttpStatusCode.OK, LocationService.getLocationsForWater(call.parameters["id"]!!).toResponse())
    }
}

private fun Route.createWater() {
    post {
        val waterDto = call.receive<CreateWaterDto>()
        val water = WaterService.addWater(waterDto)
        call.respond(HttpStatusCode.Created, water.toResponse())
    }
}

private fun Route.updateWater() {
    put {
        val waterDto = call.receive<WaterDto>()
        val water = WaterService.updateWater(waterDto)
        call.respondOrNotFound(water?.toResponse())
    }
}

private fun Route.deleteWater() {
    delete("/{id}") {
        WaterService.deleteWater(call.parameters["id"]!!)
        call.respond(HttpStatusCode.NoContent)
    }
}