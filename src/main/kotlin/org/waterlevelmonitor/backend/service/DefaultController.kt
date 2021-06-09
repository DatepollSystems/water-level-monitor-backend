package org.waterlevelmonitor.backend.service

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
@RequestMapping("/api")
class DefaultController {

    @GetMapping
    fun default(): String { return "Running WaterLevelMonitor v0.0.2" }
}