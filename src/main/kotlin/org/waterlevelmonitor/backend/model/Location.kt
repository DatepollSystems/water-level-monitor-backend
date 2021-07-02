package org.waterlevelmonitor.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@NamedEntityGraph(
        name = "locationWithWaterLevels",
        attributeNodes = [NamedAttributeNode("waterLevels")]
)
@Table(name = "locations")
@Entity
data class Location(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @NotBlank
        @Column(length = 100)
        var name: String,

        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "river_id", nullable = false)
        var river: River,

        @JsonIgnore
        @OneToMany(fetch = FetchType.LAZY)
        @JoinColumn(name = "location_id")
        val waterLevels: List<WaterLevel> = ArrayList(),

        @ManyToOne
        @JoinColumn(name = "organization_id")
        val organizations: Organization,


        @Column(name = "is_public", nullable = false, columnDefinition = "boolean default false")
        var isPublic: Boolean = false,

        @JsonIgnore
        @Version
        val version: Long? = null
)

data class LocationDto(
        @NotBlank
        val name: String,

        @JsonProperty("river_id")
        @NotNull
        val riverId: Long,

        @JsonProperty("organization_id")
        @NotNull
        val organizationId: Long
)