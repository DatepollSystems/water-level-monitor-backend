package org.waterlevelmonitor.backend

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.BuildProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.RestTemplate

@ActiveProfiles(profiles = ["test"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DefaultIntegrationTest @Autowired constructor(
    private val buildProperties: BuildProperties
) {

    val requestURL = "http://127.0.0.1:8181"

    @Test
    fun testVersion(){
        val restClient = RestTemplate()

        val result = restClient.getForEntity("$requestURL/api", String::class.java)

        assertEquals("Running ${buildProperties.name} v${buildProperties.version}", result.body)
    }

}