package org.waterlevelmonitor.backend.service

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/waterlevel")
class WaterLevelController {

    @GetMapping
    fun secureTest(): String{
        return "works"
    }
}
