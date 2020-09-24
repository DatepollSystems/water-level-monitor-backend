package org.waterlevelmonitor.backend.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.waterlevelmonitor.backend.model.River

interface RiverRepository: JpaRepository<River, Long> {

    fun getRiverById(id: Long): River

    @Query("select r from River r")
    fun getAll(): List<River>
}