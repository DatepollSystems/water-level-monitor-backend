package org.waterlevelmonitor.backend.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.waterlevelmonitor.backend.domain.UserRepository
import org.waterlevelmonitor.backend.exceptions.NotAuthorizedException
import org.waterlevelmonitor.backend.model.User
import org.waterlevelmonitor.backend.model.UserDto

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
class UserController(val bCryptPasswordEncoder: BCryptPasswordEncoder, val userRepository: UserRepository) {

    @Value("\${user.creation.enabled}")
    var creationEnabled: String = "false"

    @Value("\${user.creation.token}")
    var creationToken: String = ""


    @PostMapping("/signup")
    fun signup(@RequestBody u: UserDto){
        val valid = creationEnabled.toBoolean()

        if(valid && u.creationToken == creationToken){
            val user = u.toDbModel()
            user.password = bCryptPasswordEncoder.encode(user.password)
            userRepository.save(user)
            return
        }

        throw NotAuthorizedException()
    }
}