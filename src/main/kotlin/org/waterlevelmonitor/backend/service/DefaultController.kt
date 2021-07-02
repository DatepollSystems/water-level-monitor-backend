package org.waterlevelmonitor.backend.service

import org.springframework.boot.info.BuildProperties
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@CrossOrigin
@RestController
@RequestMapping("/api")
class DefaultController(
    val buildProperties: BuildProperties
) {
    @GetMapping
    fun default(): String { return "Running ${buildProperties.name} v${buildProperties.version}" }
}