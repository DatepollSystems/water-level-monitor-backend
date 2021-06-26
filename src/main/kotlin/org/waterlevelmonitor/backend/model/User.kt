package org.waterlevelmonitor.backend.model

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Table(name = "users")
@Entity
data class User(

        @Id
        @GeneratedValue
        var id: Long,

        @NotBlank
        @Column(nullable = false, unique = true)
        var username: String,

        @NotBlank
        @Column(nullable = false)
        var password: String,

        @Column(nullable = false)
        var role: Role = Role.NONE,

        @Version
        val version: Long? = null
)

data class UserDto(
        val username: String,
        val password: String,
        val creationToken: String
) {
    fun toDbModel(): User {
        return User(0, username, password, Role.NONE)
    }
}


enum class Role {
        ADMIN,
        ORGANIZATION_ADMIN,
        NONE
}