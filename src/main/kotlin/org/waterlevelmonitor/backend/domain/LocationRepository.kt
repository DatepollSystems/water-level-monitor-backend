package org.waterlevelmonitor.backend.domain

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.waterlevelmonitor.backend.model.Location

interface LocationRepository : JpaRepository<Location, Long>{

    @EntityGraph("locationWithWaterLevels", type = EntityGraph.EntityGraphType.FETCH)
    fun getLocationById(id: Long): Location
}