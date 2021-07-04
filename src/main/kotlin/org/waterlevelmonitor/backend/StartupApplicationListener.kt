package org.waterlevelmonitor.backend

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import org.waterlevelmonitor.backend.domain.UserRepository
import org.waterlevelmonitor.backend.model.Role
import org.waterlevelmonitor.backend.model.User

/**
 * StartupApplication listener
 * handles all stuff which should be done at startup
 * @author kaulex
 * @since Version 0.0.2
 */
@Component
class StartupApplicationListener @Autowired constructor(
    private val userRepo: UserRepository,
    private val bcrypt: BCryptPasswordEncoder
) : ApplicationListener<ContextRefreshedEvent> {

    private val log: Logger = LoggerFactory.getLogger(StartupApplicationListener::class.java)

    @Value("\${admin.password}")
    var password: String = "admin"

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        checkAdminUser()
    }

    /**
     * Checks if admin user exists
     * --> if not - admin is created by provided password in config
     * @author kaulex
     */
    private fun checkAdminUser() {
        log.info("Checking admin user")
        if (userRepo.findUserByUsername("admin") == null) {
            // Create user
            log.info("Creating admin user")
            val user = User(
                id = 1L,
                username = "admin",
                name = "admin",
                role = Role.ADMIN,
                password = bcrypt.encode(password)
            )

            userRepo.save(user)
            log.info("Admin saved to DB")

            if (userRepo.findUserByUsername("admin") != null)
                log.info("Admin successfully created")
            else
                log.error("Admin creation failed!")
        } else {
            log.info("Admin already exists")
        }
    }
}