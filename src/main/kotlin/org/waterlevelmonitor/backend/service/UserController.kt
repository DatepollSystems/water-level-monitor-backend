package org.waterlevelmonitor.backend.service

import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.waterlevelmonitor.backend.domain.UserRepository
import org.waterlevelmonitor.backend.model.User

@RestController
@RequestMapping("/users")
class UserController(val bCryptPasswordEncoder: BCryptPasswordEncoder, val userRepository: UserRepository) {

    @PostMapping("/signup")
    fun signup(@Validated @RequestBody user: User){
        user.password = bCryptPasswordEncoder.encode(user.password)
        userRepository.save(user)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun test(): List<User>{
        return userRepository.findAll()
    }
}