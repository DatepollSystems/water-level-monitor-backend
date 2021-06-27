package org.waterlevelmonitor.backend.model

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Table(name = "organizations")
@Entity
data class Organization(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    /**
     * Url part of 'https://pegel.info/{shortName}/...'
     */
    @NotBlank
    @Column(name = "shortName", nullable = false, unique = true)
    var shortName: String = "",

    /**
     * Display name on 'https://pegel.info/{shortName}' page.
     */
    @NotBlank
    @Column(name = "name", nullable = true)
    var name: String = "",

    @Column(name = "is_public", nullable = false)
    var isPublic: Boolean = false,

    @OneToMany
    val users: List<User> = ArrayList(),

    @Version
    val version: Long
) {
    fun getDisplayName() = if (users.size > 1) name else users.first().name
}