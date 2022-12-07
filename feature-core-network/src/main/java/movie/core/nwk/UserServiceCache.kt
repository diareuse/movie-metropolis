package movie.core.nwk

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserServiceCache(
    private val origin: UserService,
    private val cache: Cache<String, String>,
    private val serializer: Json
) : UserService by origin {

    override suspend fun getPoints() = getCacheOrDefault("user-points") {
        origin.getPoints()
    }

    override suspend fun getUser() = getCacheOrDefault("user") {
        origin.getUser()
    }

    override suspend fun getBooking(id: String) = getCacheOrDefault("booking-$id") {
        origin.getBooking(id)
    }

    // ---

    private suspend inline fun <reified T> getCacheOrDefault(
        key: String,
        default: () -> Result<T>
    ): Result<T> {
        val value = cache.get(key)
        if (value != null) {
            val result = serializer.runCatching { decodeFromString<T>(value) }
            if (result.isSuccess) return result
        }

        return default().onSuccess {
            cache.put(key, serializer.encodeToString(it))
        }
    }

}