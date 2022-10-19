package at.fhj.features.locations.db

import at.fhj.core.ResponsePayload
import at.fhj.features.locations.dto.LocationDto
import com.moshbit.katerbase.MongoMainEntry

class Location : MongoMainEntry(), ResponsePayload<LocationDto> {
    var name: String = ""
    var city: String = ""
    var waterId: String = ""

    init {
        _id = randomId()
    }

    override fun toResponse() = LocationDto(
        id = _id,
        name = name,
        city = city,
        waterId = waterId
    )
}