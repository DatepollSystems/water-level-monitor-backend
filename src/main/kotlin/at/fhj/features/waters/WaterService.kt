package at.fhj.features.waters

import at.fhj.database.Database
import at.fhj.features.waters.db.Water
import at.fhj.features.waters.dto.CreateWaterDto
import at.fhj.features.waters.dto.WaterDto
import com.moshbit.katerbase.equal

object WaterService {

    fun addWater(dto: CreateWaterDto): Water {
        val model = Water().apply {
            name = dto.name
        }
        Database.waters.insertOne(model, upsert = true)
        return model
    }

    fun getWaters(): List<Water> {
        return Database.waters.find().toList()
    }

    fun getWaterById(id: String): Water? {
        return Database.waters.findOne(Water::_id equal id)
    }

    fun deleteWater(id: String) {
        Database.waters.deleteOne(Water::_id equal id)
    }

    fun updateWater(dto: WaterDto): Water? {
        return Database.waters.updateOneAndFind(Water::_id equal dto.id) {
            Water::name setTo dto.name
        }
    }
}