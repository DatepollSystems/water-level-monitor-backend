package at.fhj

import at.fhj.plugins.configureRouting
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/api").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Running WaterLevelMonitor-API! ( ͡° ͜ʖ ͡°)", bodyAsText())
        }

        // Check if trailing slash is ignored
        client.get("/api/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Running WaterLevelMonitor-API! ( ͡° ͜ʖ ͡°)", bodyAsText())
        }
    }
}