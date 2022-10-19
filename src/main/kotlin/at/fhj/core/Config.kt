package at.fhj.core

import at.fhj.exceptions.MissingInitialContextException

object Config {
    val isLocal = System.getenv("ENVIRONMENT") != "production"
    val isProd = !isLocal

    val dbUri = getEnvOrDefaultOnLocal("DB_URI", "mongodb://localhost:27017/wlmdb")

    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    val host = System.getenv("HOST") ?: "0.0.0.0"

    val jwtRealm = getEnvOrDefaultOnLocal("JWT_REALM", "realm")
    val jwtSecret = getEnvOrDefaultOnLocal("JWT_SECRET", "s3cr3t")
    val jwtAudience = getEnvOrDefaultOnLocal("JWT_AUDIENCE", "api.wlm.local")
    val jwtIssuer = getEnvOrDefaultOnLocal("JWT_ISSUER", "api.wlm.local")
    val jwtAccessTokenLifetimeMs =
        getEnvOrDefaultOnLocal("JWT_ACCESS_TOKEN_LIFETIME_MS", (60 * 60 * 1000).toString()).toLong()
    val jwtRefreshTokenLifetimeMs =
        getEnvOrDefaultOnLocal("JWT_REFRESH_TOKEN_LIFETIME_MS", (24 * 60 * 60 * 1000).toString()).toLong()

    private fun getEnvOrDefaultOnLocal(name: String, default: String): String {
        return System.getenv(name)
            ?: if (isLocal) default else throw MissingInitialContextException("$name is required in production")
    }
}