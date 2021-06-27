package org.waterlevelmonitor.backend.auth

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.waterlevelmonitor.backend.domain.UserRepository
import java.util.*

@Component
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findUserByUsername(username)
        if (user == null) {
            throw UsernameNotFoundException(username)
        } else {
            return User(user.username, user.password, Collections.emptyList())
        }
    }


}