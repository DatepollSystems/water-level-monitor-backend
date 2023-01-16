package at.fhj.features.measurement

import at.fhj.database.Database
import at.fhj.features.measurement.db.Measurement
import at.fhj.features.measurement.dto.AddMeasurementDto
import at.fhj.features.measurement.dto.MeasurementDto
import com.moshbit.katerbase.equal
import java.time.Instant

object MeasurementService {
    fun addMeasurement(dto: AddMeasurementDto): Measurement {
        val model = Measurement().apply {
            value = dto.value
            unit = dto.unit
            locationId = dto.locationId
            timestamp = dto.timestamp ?: Instant.now().toEpochMilli()
        }

        Database.measurements.insertOne(model, upsert = true)
        return model
    }

    fun getMeasurements(): List<Measurement> {
        return Database.measurements.find().toList()
    }

    fun getMeasurement(id: String): Measurement? {
        return Database.measurements.findOne(Measurement::_id equal id)
    }

    fun deleteMeasurement(id: String) {
        Database.measurements.deleteOne(Measurement::_id equal id)
    }

    fun updateMeasurement(measurement: MeasurementDto): Measurement? {
        return Database.measurements.updateOneAndFind(Measurement::_id equal measurement.id) {
            Measurement::value setTo measurement.value
            Measurement::unit setTo measurement.unit
            Measurement::timestamp setTo measurement.timestamp
            Measurement::locationId setTo measurement.locationId
        }
    }

    fun getMeasurementsForLocation(id: String): List<Measurement> {
        return Database.measurements.find(Measurement::locationId equal id).toList()
    }
}