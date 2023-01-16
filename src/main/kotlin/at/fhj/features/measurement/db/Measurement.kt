package at.fhj.features.measurement.db

import at.fhj.core.ResponsePayload
import at.fhj.features.measurement.dto.MeasurementDto
import com.moshbit.katerbase.MongoMainEntry
import java.time.Instant

class Measurement : MongoMainEntry(), ResponsePayload<MeasurementDto> {
    var value: Double = Double.NaN
    var unit: String = ""
    var locationId: String = ""
    var timestamp: Long = 0 // UTC (ms)

    init {
        _id = randomId()
        timestamp = Instant.now().toEpochMilli()
    }

    override fun toResponse() = MeasurementDto(
        id = _id,
        value = value,
        unit = unit,
        locationId = locationId,
        timestamp = timestamp
    )
}