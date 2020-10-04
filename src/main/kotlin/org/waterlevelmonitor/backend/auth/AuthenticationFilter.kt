package org.waterlevelmonitor.backend.auth

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.lang.RuntimeException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthenticationFilter(
        authenticationManager: AuthenticationManager
): UsernamePasswordAuthenticationFilter() {

    init {
        setAuthenticationManager(authenticationManager)
        setFilterProcessesUrl("/api/v1/login")
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        try {
            val user: org.waterlevelmonitor.backend.model.User = ObjectMapper().readValue(request!!.inputStream, org.waterlevelmonitor.backend.model.User::class.java)
            return authenticationManager.authenticate(UsernamePasswordAuthenticationToken(user.username, user.password))
        } catch (e: IOException) {
            throw RuntimeException("Could not read request $e");
        }
    }

    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?, authResult: Authentication?) {
        val token = Jwts.builder()
                .setSubject((authResult!!.principal as User).username)
                .setExpiration(Date(System.currentTimeMillis() + 864_000_000))
                .signWith(SignatureAlgorithm.HS512, "SecretKeyToGenJWTs".toByteArray())
                .compact()
        response!!.addHeader("Authorization", "Bearer $token")
    }
}