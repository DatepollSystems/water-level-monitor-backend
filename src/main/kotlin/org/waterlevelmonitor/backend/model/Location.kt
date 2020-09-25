package org.waterlevelmonitor.backend.model

import javax.persistence.*

@NamedEntityGraph(
        name = "locationWithWaterLevels",
        attributeNodes = [NamedAttributeNode("waterLevels")]
)
@Entity
data class Location(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column
        val name: String,

        @ManyToOne
        @JoinColumn(name = "river_id")
        val river: River,

        @OneToMany(fetch = FetchType.LAZY)
        @JoinColumn(name = "location_id")
        val waterLevels: List<WaterLevel>,

        @Version
        val version: Long?
)