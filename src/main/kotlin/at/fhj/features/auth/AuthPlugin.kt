package at.fhj.features.auth

import at.fhj.core.Config
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.impl.PublicClaims
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureAuth() {
    authentication {
        jwt("jwt") {
            realm = Config.jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(Config.jwtSecret))
                    .withAudience(Config.jwtAudience)
                    .withIssuer(Config.jwtIssuer)
                    .withClaimPresence(PublicClaims.SUBJECT)
                    .build()
            )
            validate { credential ->
                // Additional validations (return null for invalid)
                JWTPrincipal(credential.payload)
            }
        }
    }
}
