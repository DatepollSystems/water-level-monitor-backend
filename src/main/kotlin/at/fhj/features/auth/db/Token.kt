package at.fhj.features.auth.db

import com.moshbit.katerbase.MongoMainEntry
import java.util.*

data class Token(
    val token: String,
    val userId: String,
    val expiresAt: Date,
    val createdAt: Date = Date()
) : MongoMainEntry() {
    init {
        _id = generateId(token)
    }
}
