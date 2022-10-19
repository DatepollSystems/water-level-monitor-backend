package at.fhj.features.auth.utils

import at.fhj.core.Config
import at.fhj.database.Database
import at.fhj.features.auth.db.Token
import at.fhj.utils.Algorithms
import at.fhj.utils.sha256
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

fun generateRefreshToken(userId: String, lifetime: Long = Config.jwtRefreshTokenLifetimeMs): Token {
    val random = Algorithms.secureRandomString(32)
    val token = Token(
        token = ("_salt_" + Date().time + "_other_salt_" + random).sha256(),
        userId = userId,
        expiresAt = Date(System.currentTimeMillis() + lifetime),
        createdAt = Date()
    )

    Database.tokens.insertOne(token, false)

    return token
}

fun generateJWT(userId: String, lifetime: Long = Config.jwtAccessTokenLifetimeMs): String {
    return JWT.create()
        .withAudience(Config.jwtAudience)
        .withIssuer(Config.jwtIssuer)
        .withSubject(userId)
        .withExpiresAt(Date(System.currentTimeMillis() + lifetime))
        .sign(Algorithm.HMAC256(Config.jwtSecret))
}