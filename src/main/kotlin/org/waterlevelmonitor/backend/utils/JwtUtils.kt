package org.waterlevelmonitor.backend.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*


class JwtUtils {
    companion object {
        fun generateJWTToken(userId: Long, userSignToken: String): String {
            return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(Date(System.currentTimeMillis() + 864_000_000))
                .signWith(SignatureAlgorithm.HS512, userSignToken.toByteArray())
                .compact()
        }
    }
}