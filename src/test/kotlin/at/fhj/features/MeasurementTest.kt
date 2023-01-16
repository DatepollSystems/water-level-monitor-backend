package at.fhj.features

import at.fhj.core.post
import at.fhj.core.put
import at.fhj.core.testApplicationAuthJsonClient
import at.fhj.core.testApplicationJsonClient
import at.fhj.features.measurement.MeasurementService
import at.fhj.features.measurement.db.Measurement
import at.fhj.features.measurement.dto.AddMeasurementDto
import at.fhj.features.measurement.dto.MeasurementDto
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.test.*

class MeasurementTest {

    private lateinit var model: Measurement

    @BeforeTest
    fun setup() {
        model = MeasurementService.addMeasurement(
            AddMeasurementDto(
                value = 10.0,
                unit = "m",
                locationId = "testLocationId"
            )
        )
    }

    @Test
    fun `add measurement`() = testApplicationAuthJsonClient { client ->
        client.post("api/measurements", AddMeasurementDto(10.0, "m", "123456789"))
            .apply {
                assertEquals(HttpStatusCode.Created, status)
                val body = body<MeasurementDto>()
                assertEquals(10.0, body.value)
                assertEquals("m", body.unit)
                assertEquals("123456789", body.locationId)
                assert(body.id.isNotEmpty())

                MeasurementService.deleteMeasurement(body.id)
            }
    }

    @Test
    fun `get all measurements`() = testApplicationJsonClient { client ->
        client.get("/api/measurements").apply {
            val body = body<List<MeasurementDto>>()
            assertEquals(HttpStatusCode.OK, status)
            assertContains(body, model.toResponse())
        }
    }

    @Test
    fun `get one measurement`() = testApplicationJsonClient { client ->
        client.get("/api/measurements/${model._id}").apply {
            val body = body<MeasurementDto>()
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(model.toResponse(), body)
        }
    }

    @Test
    fun `get non existing measurement`() = testApplicationJsonClient { client ->
        client.get("/api/measurements/invalidId").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `update measurement`() = testApplicationAuthJsonClient { client ->
        val dto = model.toResponse().apply {
            locationId = "updated location"
        }

        client.put("/api/measurements", dto).apply {
            val body = body<MeasurementDto>()
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("updated location", body.locationId)

            // Ensure nothing else has changed
            assertEquals(model.value, body.value)
            assertEquals(model._id, body.id)
            assertEquals(model.unit, body.unit)
            assertEquals(model.timestamp, body.timestamp)
        }
    }

    @Test
    fun `update non existing measurement`() = testApplicationAuthJsonClient { client ->
        val dto = model.toResponse().copy(id = "invalidId")

        client.put("/api/measurements", dto).apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `delete measurement`() = testApplicationAuthJsonClient { client ->
        client.delete("/api/measurements/${model._id}")
            .apply {
                assertEquals(HttpStatusCode.NoContent, status)
            }
    }

    @AfterTest
    fun teardown() {
        MeasurementService.deleteMeasurement(model._id)
    }
}