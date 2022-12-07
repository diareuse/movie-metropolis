package movie.core.nwk

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import movie.core.nwk.model.CinemaResponse
import movie.core.nwk.model.ResultsResponse

typealias CinemasResponse = ResultsResponse<List<CinemaResponse>>

class CinemaServiceCache(
    private val origin: CinemaService,
    private val cache: Cache<String, String>,
    private val serializer: Json
) : CinemaService {

    override suspend fun getCinemas(): Result<CinemasResponse> {
        return getCacheOrDefault("cinemas") { origin.getCinemas() }
    }

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