package org.waterlevelmonitor.backend.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.waterlevelmonitor.backend.domain.RiverRepository
import org.waterlevelmonitor.backend.model.River
import org.waterlevelmonitor.backend.model.RiverDto

@RestController
@RequestMapping("/rivers")
class TestService {

    private val log = LoggerFactory.getLogger(TestService::class.java);

    @Autowired
    private lateinit var riverRepo: RiverRepository

    @GetMapping
    fun loadTest(): List<River>{
        return riverRepo.getAll()
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun postRiver(@Validated @RequestBody river: River): River{
        log.info("Save $river")
        return riverRepo.save(river)
    }


}