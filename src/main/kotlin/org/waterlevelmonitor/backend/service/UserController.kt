package org.waterlevelmonitor.backend.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.waterlevelmonitor.backend.domain.UserRepository
import org.waterlevelmonitor.backend.model.User

@RestController
@RequestMapping("/api/v1/users")
class UserController(val bCryptPasswordEncoder: BCryptPasswordEncoder, val userRepository: UserRepository) {

    /**@PostMapping("/signup")
    fun signup(@Validated @RequestBody user: User){
        user.password = bCryptPasswordEncoder.encode(user.password)
        userRepository.save(user)
    }**/
}