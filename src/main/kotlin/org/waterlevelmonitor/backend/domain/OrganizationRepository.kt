package org.waterlevelmonitor.backend.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.waterlevelmonitor.backend.model.Organization

interface OrganizationRepository : JpaRepository<Organization, Long>{

    fun getOrganizationById(id: Long): Organization?
}