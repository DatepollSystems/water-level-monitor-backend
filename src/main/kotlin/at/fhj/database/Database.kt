package at.fhj.database

import at.fhj.core.Config
import at.fhj.features.auth.db.Token
import at.fhj.features.auth.db.User
import at.fhj.features.locations.db.Location
import at.fhj.features.measurement.db.Measurement
import at.fhj.features.waters.db.Water
import com.moshbit.katerbase.MongoDatabase
import java.util.concurrent.TimeUnit

object Database {
    private val database: MongoDatabase = MongoDatabase(Config.dbUri) {
        collection<User>("users") {
            index(User::email.ascending(), indexOptions = { unique(true) })
        }
        collection<Token>("tokens") {
            index(Token::token.ascending())
            // Delete old expired tokens
            index(Token::expiresAt.ascending(), indexOptions = { expireAfter(1, TimeUnit.MINUTES) })
        }
        collection<Water>("waters")
        collection<Location>("locations")
        collection<Measurement>("measurements")
    }

    val users get() = database.getCollection<User>()
    val waters get() = database.getCollection<Water>()
    val tokens get() = database.getCollection<Token>()
    val locations get() = database.getCollection<Location>()
    val measurements get() = database.getCollection<Measurement>()

    init {
        if (Config.isLocal) {
            setupTestData()
        }
    }

    private fun setupTestData() {
        users.insertOne(User().apply {
            _id = "7e9fb51e86a33f27089c979451ca71fd" // Use always the same id -> so that there is only one test user
            name = "TestUser"
            email = "admin@wlm.org"
            setPassword("admin")
        }, upsert = true)
    }
}

