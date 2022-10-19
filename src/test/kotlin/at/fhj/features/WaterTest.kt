package at.fhj.features

import at.fhj.core.post
import at.fhj.core.put
import at.fhj.core.testApplicationAuthJsonClient
import at.fhj.core.testApplicationJsonClient
import at.fhj.features.locations.LocationService
import at.fhj.features.locations.db.Location
import at.fhj.features.locations.dto.CreateLocationDto
import at.fhj.features.locations.dto.LocationDto
import at.fhj.features.waters.WaterService
import at.fhj.features.waters.db.Water
import at.fhj.features.waters.dto.CreateWaterDto
import at.fhj.features.waters.dto.WaterDto
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.test.*

class WaterTest {

    private lateinit var waterModel: Water
    private lateinit var locationModel: Location

    @BeforeTest
    fun setup() {
        waterModel = WaterService.addWater(CreateWaterDto(name = "TestWater"))
        locationModel = LocationService.addLocation(
            CreateLocationDto(
                name = "TestLocation",
                city = "TestCity",
                waterId = waterModel._id
            )
        )
    }

    @Test
    fun `create water`() = testApplicationAuthJsonClient { client ->
        client.post("/api/waters", CreateWaterDto("TestWaterOne"))
            .apply {
                val body = body<WaterDto>()
                assertEquals(HttpStatusCode.Created, status)
                assertEquals("TestWaterOne", body.name)
                assertTrue { body.id.isNotEmpty() }

                WaterService.deleteWater(body.id)
            }
    }

    @Test
    fun `get one water`() = testApplicationJsonClient { client ->
        client.get("/api/waters/${waterModel._id}").apply {
            val body = body<WaterDto>()
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(waterModel.toResponse(), body)
        }
    }

    @Test
    fun `get non existing water`() = testApplicationJsonClient { client ->
        client.get("/api/waters/invalidID").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `get all waters`() = testApplicationJsonClient { client ->
        client.get("/api/waters").apply {
            val body = body<List<WaterDto>>()
            assertEquals(HttpStatusCode.OK, status)
            assertContains(body, waterModel.toResponse())
        }
    }

    @Test
    fun `get locations for water`() = testApplicationJsonClient { client ->
        client.get("/api/waters/${waterModel._id}/locations").apply {
            val body = body<List<LocationDto>>()
            assertEquals(HttpStatusCode.OK, status)
            assertContains(body, locationModel.toResponse())
        }
    }

    @Test
    fun `update water`() = testApplicationAuthJsonClient { client ->
        val dto = waterModel.toResponse().apply {
            name = "Updated name"
        }

        client.put("/api/waters", dto).apply {
            val body = body<WaterDto>()
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Updated name", body.name)

            // Ensure nothing else has changed
            assertEquals(waterModel._id, body.id)
        }
    }

    @Test
    fun `update non existing water`() = testApplicationAuthJsonClient { client ->
        val dto = WaterDto("invalidID", "Updated name")

        client.put("/api/waters", dto).apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `delete water`() = testApplicationAuthJsonClient { client ->
        client.delete("/api/waters/${waterModel._id}").apply {
            assertEquals(HttpStatusCode.NoContent, status)
        }
    }

    @AfterTest
    fun teardown() {
        WaterService.deleteWater(waterModel._id)
        LocationService.deleteLocation(locationModel._id)
    }
}