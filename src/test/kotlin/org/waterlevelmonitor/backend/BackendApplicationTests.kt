package org.waterlevelmonitor.backend

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.waterlevelmonitor.backend.domain.RiverRepository

@ActiveProfiles(profiles = ["test"])
@SpringBootTest
class BackendApplicationTests @Autowired constructor (
    private val repo: RiverRepository
) {

    var logger = LoggerFactory.getLogger(BackendApplicationTests::class.java)

    @Test
    fun contextLoads() {
        assertEquals("Donau", repo.getOne(1L).name)
    }
}
