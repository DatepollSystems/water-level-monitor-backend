package org.waterlevelmonitor.backend.model

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
data class User (

    @Id
    @GeneratedValue
    var id: Long,

    @NotBlank
    @Column(nullable = false, unique = true)
    var username: String,

    @NotBlank
    @Column(nullable = false)
    var password: String,

    @Version
    val version: Long?
)