package org.waterlevelmonitor.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.Size

@Table(name = "rivers")
@Entity
data class River(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        @Column(nullable = false, length = 100)
        @Size(min = 1, max = 100)
        var name: String,

        @JsonIgnore
        @OneToMany
        @JoinColumn(name = "river_id")
        val locations: List<Location> = ArrayList(),

        @JsonIgnore
        @Version
        val version: Long? = null
)

data class RiverDto(
        var name: String?
)