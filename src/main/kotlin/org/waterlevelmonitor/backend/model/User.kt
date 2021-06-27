package org.waterlevelmonitor.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Table(name = "user_tokens")
@Entity
data class UserToken(
    @Id
    @GeneratedValue
    var id: Long,

    @NotBlank
    @Column(nullable = false, unique = true)
    var token: String,

    @NotBlank
    @Column(nullable = false)
    var description: String,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(nullable = false)
    var purpose: UserTokenPurpose = UserTokenPurpose.STAY_LOGGED_IN
)

enum class UserTokenPurpose {
    STAY_LOGGED_IN
}

@Table(name = "users")
@Entity
data class User(

    @Id
    @GeneratedValue
    val id: Long = 0,

    @NotBlank
    @Column(nullable = false, unique = true)
    var username: String,

    @NotBlank
    @Column(nullable = false)
    var name: String,

    @NotBlank
    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var role: Role = Role.NONE,

    @Column(nullable = false)
    var forcePasswordChange: Boolean = false,

    @OneToMany
    var organisations: List<Organization> = ArrayList(),

    @OneToMany
    var tokens: List<UserToken> = ArrayList(),

    @OneToMany
    var apiTokens: List<UserToken> = ArrayList(),

    @Version
    val version: Long? = null
)

data class SignInDto(
    @NotBlank
    val username: String,
    @NotBlank
    val password: String,
    val session_information: String?,
    val stay_logged_in: Boolean?
)

data class SignInWithPasswordChangeDto(
    @NotBlank
    val username: String,
    @NotBlank
    val new_password: String,
    @NotBlank
    val old_password: String,
    val session_information: String?,
    val stay_logged_in: Boolean?
)

data class RefreshJWTWithSessionTokenDto(
    @NotBlank
    val session_token: String,
    @NotBlank
    val session_information: String
)

data class JWTResponse(
    val token: String?,
    val session_token: String?
)

data class UserDto(
    val username: String,
    val name: String,
    val password: String,
    val creationToken: String
) {
    fun toDbModel(): User {
        return User(0, username, name, password, Role.NONE)
    }
}


enum class Role {
    ADMIN,
    ORGANIZATION_ADMIN,
    NONE
}

