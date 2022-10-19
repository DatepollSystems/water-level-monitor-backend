package at.fhj.features.auth.db

import at.favre.lib.crypto.bcrypt.BCrypt
import com.moshbit.katerbase.MongoMainEntry

class User : MongoMainEntry() {
    var email: String = ""
    var name: String = ""
    private var hash: String = ""

    init {
        _id = randomId()
    }

    fun setPassword(rawPassword: String) {
        hash = BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray())
    }

    fun checkPassword(rawPassword: String) = BCrypt.verifyer().verify(rawPassword.toCharArray(), hash).verified
}
