package at.fhj.features.measurement

import at.fhj.core.respondOrNotFound
import at.fhj.core.toResponse
import at.fhj.features.measurement.dto.AddMeasurementDto
import at.fhj.features.measurement.dto.MeasurementDto
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.measurementRoutes() {
    route("/measurements") {
        getAllMeasurements()
        getMeasurementById()

        authenticate("jwt") {
            updateMeasurement()
            addMeasurement()
            deleteMeasurement()
        }
    }
}

private fun Route.getAllMeasurements() {
    get {
        call.respond(HttpStatusCode.OK, MeasurementService.getMeasurements().toResponse())
    }
}

private fun Route.getMeasurementById() {
    get("/{id}") {
        call.respondOrNotFound(MeasurementService.getMeasurement(call.parameters["id"]!!)?.toResponse())
    }
}

private fun Route.addMeasurement() {
    post {
        val body = call.receive<AddMeasurementDto>()
        val measurement = MeasurementService.addMeasurement(body)
        call.respond(HttpStatusCode.Created, measurement.toResponse())
    }
}

private fun Route.updateMeasurement() {
    put {
        val body = call.receive<MeasurementDto>()
        val measurement = MeasurementService.updateMeasurement(body)
        call.respondOrNotFound(measurement?.toResponse())
    }
}

private fun Route.deleteMeasurement() {
    delete("/{id}") {
        MeasurementService.deleteMeasurement(call.parameters["id"]!!)
        call.respond(HttpStatusCode.NoContent)
    }
}
