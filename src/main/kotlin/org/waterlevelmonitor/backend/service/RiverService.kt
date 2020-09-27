package org.waterlevelmonitor.backend.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.waterlevelmonitor.backend.domain.RiverRepository
import org.waterlevelmonitor.backend.model.River

@RestController
@RequestMapping("/rivers")
class RiverService {

    private val log = LoggerFactory.getLogger(RiverService::class.java)

    @Autowired
    private lateinit var riverRepo: RiverRepository

    @GetMapping
    fun getAllRivers(): List<River>{
        return riverRepo.findAll()
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createRiver(@Validated @RequestBody river: River): River{
        log.info("Save $river")
        return riverRepo.save(river)
    }
}