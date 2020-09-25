package org.waterlevelmonitor.backend.model

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
data class River(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        @Column(nullable = false, length = 100)
        @Size(min = 1, max = 100)
        var name: String,

        @OneToMany
        @JoinColumn(name = "river_id")
        val locations: List<Location>,

        @Version
        val version: Long?
)

data class RiverDto(
        var name: String?
)