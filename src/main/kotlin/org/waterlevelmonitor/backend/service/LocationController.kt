package org.waterlevelmonitor.backend.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.waterlevelmonitor.backend.domain.LocationRepository
import org.waterlevelmonitor.backend.domain.RiverRepository
import org.waterlevelmonitor.backend.exceptions.RiverNotFoundException
import org.waterlevelmonitor.backend.model.Location
import org.waterlevelmonitor.backend.model.LocationDto
import javax.validation.Valid

@CrossOrigin
@RestController
@RequestMapping("/api/v1/locations")
class LocationController(
        private val locationRepository: LocationRepository,
        private val riverRepository: RiverRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(LocationController::class.java)

    @GetMapping("/{riverId}")
    fun getAllLocationsByRiverId(@PathVariable riverId: Long): List<Location> {
        return locationRepository.getAllByRiverId(riverId)
    }

    @PostMapping
    fun createLocationForRiver(@Valid @RequestBody lDto: LocationDto) {

        val river = riverRepository.getRiverById(lDto.riverId)
                ?: throw RiverNotFoundException("River with id: ${lDto.riverId} not found.")

        val location = Location(
                name = lDto.name,
                river = river)

        locationRepository.save(location)
    }
}