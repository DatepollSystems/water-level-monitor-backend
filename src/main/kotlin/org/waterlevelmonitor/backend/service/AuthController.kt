package org.waterlevelmonitor.backend.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import org.waterlevelmonitor.backend.domain.UserRepository
import org.waterlevelmonitor.backend.domain.UserTokenRepository
import org.waterlevelmonitor.backend.exceptions.NotFoundException
import org.waterlevelmonitor.backend.exceptions.PasswordChangeRequiredException
import org.waterlevelmonitor.backend.exceptions.UsernameOrPasswordIncorrectException
import org.waterlevelmonitor.backend.model.*
import org.waterlevelmonitor.backend.utils.JwtUtils
import java.util.*
import javax.validation.Valid

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    val bCryptPasswordEncoder: BCryptPasswordEncoder,
    val userRepository: UserRepository,
    val userTokenRepository: UserTokenRepository
) {

    @Value("\${user.jwt.sign.token}")
    val userSignToken: String = "anytoken"

    @PostMapping("/signin")
    fun signIn(@RequestBody @Valid request: SignInDto): JWTResponse {
        val user = userRepository.findUserByUsername(request.username) ?: throw UsernameOrPasswordIncorrectException()

        if (user.password != bCryptPasswordEncoder.encode(request.password)) {
            throw UsernameOrPasswordIncorrectException()
        }

        if (user.forcePasswordChange) {
            throw PasswordChangeRequiredException()
        }

        return checkLoginDetails(user, request.stay_logged_in, request.session_information)
    }

    @PutMapping("/signin")
    fun signIn(@RequestBody @Valid request: SignInWithPasswordChangeDto): JWTResponse {
        val user = userRepository.findUserByUsername(request.username) ?: throw UsernameOrPasswordIncorrectException()

        if (user.password != bCryptPasswordEncoder.encode(request.old_password)) {
            throw UsernameOrPasswordIncorrectException()
        }

        user.password = bCryptPasswordEncoder.encode(request.new_password)
        userRepository.save(user)

        return checkLoginDetails(user, request.stay_logged_in, request.session_information)
    }

    private fun checkLoginDetails(user: User, stayLoggedIn: Boolean?, sessionInformation: String?): JWTResponse {
        if (stayLoggedIn == true && sessionInformation?.isNotBlank()!!) {
            val session = UserToken(
                0,
                UUID.randomUUID().toString(),
                sessionInformation,
                user,
                UserTokenPurpose.STAY_LOGGED_IN
            )
            userTokenRepository.save(session)

            return JWTResponse(JwtUtils.generateJWTToken(user.id, userSignToken), session.token)
        }

        return JWTResponse(JwtUtils.generateJWTToken(user.id, userSignToken), null)
    }

    @PostMapping("/refresh")
    fun refreshJWTWithSessionToken(@RequestBody @Valid request: RefreshJWTWithSessionTokenDto): JWTResponse {
        val session = userTokenRepository.getUserTokenByPurposeAndAndToken(
            UserTokenPurpose.STAY_LOGGED_IN.toString(),
            request.session_token
        ) ?: throw NotFoundException("User token")

        session.description = request.session_information
        userTokenRepository.save(session)

        return JWTResponse(JwtUtils.generateJWTToken(session.user.id, userSignToken), null)
    }


    @PostMapping("/signup")
    fun signup(@RequestBody u: UserDto) {
        val user = u.toDbModel()
        user.password = bCryptPasswordEncoder.encode(user.password)
        userRepository.save(user)
    }
}