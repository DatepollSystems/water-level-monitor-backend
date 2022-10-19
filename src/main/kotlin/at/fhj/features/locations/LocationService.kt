package at.fhj.features.locations

import at.fhj.database.Database
import at.fhj.features.locations.db.Location
import at.fhj.features.locations.dto.CreateLocationDto
import at.fhj.features.locations.dto.LocationDto
import com.moshbit.katerbase.equal

object LocationService {
    fun addLocation(dto: CreateLocationDto): Location {
        val model = Location().apply {
            name = dto.name
            city = dto.city
            waterId = dto.waterId
        }

        Database.locations.insertOne(model, upsert = true)

        return model
    }

    fun getLocations(): List<Location> {
        return Database.locations.find().toList()
    }

    fun getLocation(id: String): Location? {
        return Database.locations.findOne(Location::_id equal id)
    }

    fun getLocationsForWater(waterId: String): List<Location> {
        return Database.locations.find(Location::waterId equal waterId).toList()
    }

    fun deleteLocation(id: String) {
        Database.locations.deleteOne(Location::_id equal id)
    }

    fun updateLocation(dto: LocationDto): Location? {
        return Database.locations.updateOneAndFind(Location::_id equal dto.id) {
            Location::name setTo dto.name
            Location::city setTo dto.city
            Location::waterId setTo dto.waterId
        }
    }
}