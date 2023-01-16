package at.fhj.features.waters.db

import at.fhj.core.ResponsePayload
import at.fhj.features.waters.dto.WaterDto
import com.moshbit.katerbase.MongoMainEntry

class Water : MongoMainEntry(), ResponsePayload<WaterDto> {
    var name: String = ""

    init {
        _id = randomId()
    }

    override fun toResponse() = WaterDto(
        id = _id,
        name = name
    )
}
