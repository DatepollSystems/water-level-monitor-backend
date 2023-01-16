package at.fhj.features

import at.fhj.core.post
import at.fhj.core.put
import at.fhj.core.testApplicationAuthJsonClient
import at.fhj.core.testApplicationJsonClient
import at.fhj.features.locations.LocationService
import at.fhj.features.locations.db.Location
import at.fhj.features.locations.dto.CreateLocationDto
import at.fhj.features.locations.dto.LocationDto
import at.fhj.features.measurement.MeasurementService
import at.fhj.features.measurement.db.Measurement
import at.fhj.features.measurement.dto.AddMeasurementDto
import at.fhj.features.measurement.dto.MeasurementDto
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.test.*

class LocationTest {

    private lateinit var locationModel: Location
    private lateinit var measurementModel: Measurement

    @BeforeTest
    fun setup() {
        locationModel = LocationService.addLocation(
            CreateLocationDto(
                name = "Testname",
                city = "Testcity",
                waterId = "xxx"
            )
        )

        measurementModel = MeasurementService.addMeasurement(
            AddMeasurementDto(
                value = 10.0,
                unit = "m",
                locationId = locationModel._id
            )
        )
    }

    @Test
    fun `create location`() = testApplicationAuthJsonClient { client ->
        val dto = CreateLocationDto("Test Location", "Sample City", "123456789")
        client.post("/api/locations", dto)
            .apply {
                val body = body<LocationDto>()
                assertEquals(HttpStatusCode.Created, status)
                assert(body.id.isNotEmpty())
                assertEquals(dto.name, body.name)
                assertEquals(dto.city, body.city)
                assertEquals(dto.waterId, body.waterId)

                LocationService.deleteLocation(body.id)
            }
    }

    @Test
    fun `get one location`() = testApplicationJsonClient { client ->
        client.get("/api/locations/${locationModel._id}")
            .apply {
                val body = body<LocationDto>()
                assertEquals(HttpStatusCode.OK, status)
                assertEquals(locationModel.toResponse(), body)
            }
    }

    @Test
    fun `get non existing location`() = testApplicationJsonClient { client ->
        client.get("/api/locations/invalidId")
            .apply {
                assertEquals(HttpStatusCode.NotFound, status)
            }
    }

    @Test
    fun `get all locations`() = testApplicationJsonClient { client ->
        client.get("/api/locations")
            .apply {
                val body = body<List<LocationDto>>()

                assertEquals(HttpStatusCode.OK, status)
                assertContains(body, locationModel.toResponse())
            }
    }

    @Test
    fun `get measurements for location`() = testApplicationJsonClient { client ->
        client.get("/api/locations/${locationModel._id}/measurements")
            .apply {
                val body = body<List<MeasurementDto>>()

                assertEquals(HttpStatusCode.OK, status)
                assertContains(body, measurementModel.toResponse())
            }
    }

    @Test
    fun `update location`() = testApplicationAuthJsonClient { client ->
        val locationUpdateDto = locationModel.toResponse().apply {
            name = "updated name"
        }

        client.put("/api/locations", locationUpdateDto).apply {
            val body = body<LocationDto>()
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("updated name", body.name)

            // Ensure nothing else has changed
            assertEquals(locationModel._id, body.id)
            assertEquals(locationModel.city, body.city)
            assertEquals(locationModel.waterId, body.waterId)
        }
    }

    @Test
    fun `update non existing location`() = testApplicationAuthJsonClient { client ->
        val locationUpdateDto = locationModel.toResponse().copy(id = "invalidId")

        client.put("/api/locations", locationUpdateDto).apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `delete location`() = testApplicationAuthJsonClient { client ->
        client.delete("/api/locations/${locationModel._id}")
            .apply {
                assertEquals(HttpStatusCode.NoContent, status)
            }
    }

    @AfterTest
    fun teardown() {
        LocationService.deleteLocation(locationModel._id)
        MeasurementService.deleteMeasurement(measurementModel._id)
    }
}