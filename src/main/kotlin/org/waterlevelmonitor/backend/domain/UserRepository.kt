package org.waterlevelmonitor.backend.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.waterlevelmonitor.backend.model.User

interface UserRepository: JpaRepository<User, Long> {

    fun findUserByUsername(username: String): User?
}