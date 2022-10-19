package at.fhj.utils

import at.favre.lib.bytes.BinaryToTextEncoding.Hex
import java.nio.ByteOrder
import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.streams.asSequence

object Algorithms {
    private val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    private val secureRandom = SecureRandom()
    private val hex = Hex()

    fun secureRandomString(length: Long): String {
        return secureRandom.ints(length, 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")
    }

    fun sha256(value: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hexBytes = md.digest(value.toByteArray())
        return hex.encode(hexBytes, ByteOrder.BIG_ENDIAN).lowercase()
    }
}

fun String.sha256() = Algorithms.sha256(this)