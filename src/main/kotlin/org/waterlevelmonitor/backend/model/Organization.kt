package org.waterlevelmonitor.backend.model

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Table(name = "organizations")
@Entity
data class Organization(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @NotBlank
    @Column(name = "name", nullable = false)
    val name: String = ""

    // Image/logo?

)