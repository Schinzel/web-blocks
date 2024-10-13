package io.schinzel.my_package.db

import io.schinzel.crypto.encoding.base62.Base62
import java.nio.ByteBuffer
import java.util.*

/**
 * The purpose of this is to generate a GUID/UUID
 * Uses base62 encoding that generated GUIDs that are 22 or 23 chars long instead of the more
 * standard 36 chars
 * Example: BPjONMEYJ1Zmh23EZMJ2CD
 */
@Suppress("SpellCheckingInspection")
object GUID {
    fun generate(): String {
        val uuid = UUID.randomUUID()
        val bb: ByteBuffer = ByteBuffer.wrap(ByteArray(16))
        bb.putLong(uuid.mostSignificantBits)
        bb.putLong(uuid.leastSignificantBits)
        return Base62.encode(bb.array())
    }
}
