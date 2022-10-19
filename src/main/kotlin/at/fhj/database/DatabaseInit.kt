package at.fhj.database

import io.ktor.server.application.*

fun Application.configureDB() {
    log.info("Init db connection")
    Database // init object
    log.info("Db init successful")
}