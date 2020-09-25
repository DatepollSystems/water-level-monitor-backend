package org.waterlevelmonitor.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
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
    var password: String
)