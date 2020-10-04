package org.waterlevelmonitor.backend.domain

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.waterlevelmonitor.backend.model.Location

interface LocationRepository : JpaRepository<Location, Long>{

    fun getLocationById(id: Long): Location?

    @Query("select l from Location l where l.river.id = :riverId")
    fun getAllByRiverId(riverId: Long): List<Location>
}