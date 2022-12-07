package movie.core.nwk

import java.io.File
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

private class TimedCache<Key : Any>(
    private val expiresIn: Duration,
    private val origin: Cache<Any, String>
) : Cache<Key, String> {

    override suspend fun get(key: Key): String? {
        if (isExpired(key)) return null
        return origin.get(key)
    }

    override suspend fun put(key: Key, value: String?) {
        origin.put(key, value)
        if (value != null)
            writeExpiration(key)
    }

    override suspend fun clear() {
        origin.clear()
    }

    // ---

    private suspend fun isExpired(key: Key): Boolean {
        val expiration = origin.get(Expiration(key))
            ?.toLongOrNull()
            ?.let(::Date)
            ?: return false
        return expiration.before(Date())
    }

    private suspend fun writeExpiration(key: Key) {
        origin.put(Expiration(key), (Date() + expiresIn).time.toString())
    }

    private operator fun Date.plus(expiresIn: Duration) = apply {
        time += expiresIn.inWholeMilliseconds
    }

    class Expiration(
        val key: Any
    ) {

        override fun toString() = "$key-expiration"

    }

}

fun <Key : Any> timedCacheOf(dir: File, expiresIn: Duration = 1.days): Cache<Key, String> {
    return TimedCache(expiresIn, cacheOf(dir))
}