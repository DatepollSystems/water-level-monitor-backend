package org.waterlevelmonitor.backend.model

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
data class River(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        @Column(nullable = false)
        @Size(min = 1, max = 100)
        var name: String
)

data class RiverDto(
        var name: String?
)