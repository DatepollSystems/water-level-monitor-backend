package org.waterlevelmonitor.backend.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.waterlevelmonitor.backend.model.UserToken

interface UserTokenRepository : JpaRepository<UserToken, Long>{

    fun getUserTokenByPurposeAndAndToken(purpose: String, token: String): UserToken?
}