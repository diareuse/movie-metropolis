package movie.core.nwk

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import movie.core.nwk.model.ShowingType
import java.util.Date
import kotlin.math.round

class EventServiceCache(
    private val origin: EventService,
    private val cache: Cache<String, String>,
    private val serializer: Json
) : EventService {

    override suspend fun getEventsInCinema(
        cinema: String,
        date: Date
    ) = getCacheOrDefault("event-$cinema-${date.time}") {
        origin.getEventsInCinema(cinema, date)
    }

    override suspend fun getNearbyCinemas(
        lat: Double,
        lng: Double
    ) = getCacheOrDefault("nearby-cinemas-${round(lat * 100) / 100}-${round(lng * 100) / 100}") {
        origin.getNearbyCinemas(lat, lng)
    }

    override suspend fun getDetail(
        id: String
    ) = getCacheOrDefault("detail-$id") {
        origin.getDetail(id)
    }

    override suspend fun getMoviesByType(
        type: ShowingType
    ) = getCacheOrDefault("type-${type.value}") {
        origin.getMoviesByType(type)
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