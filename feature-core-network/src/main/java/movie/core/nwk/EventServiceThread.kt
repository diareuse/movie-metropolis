package movie.core.nwk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.core.nwk.model.ShowingType
import java.util.Date

class EventServiceThread(
    private val origin: EventService
) : EventService {
    override suspend fun getEventsInCinema(
        cinema: String,
        date: Date
    ) = withContext(Dispatchers.IO) {
        origin.getEventsInCinema(cinema, date)
    }

    override suspend fun getNearbyCinemas(
        lat: Double,
        lng: Double
    ) = withContext(Dispatchers.IO) {
        origin.getNearbyCinemas(lat, lng)
    }

    override suspend fun getDetail(id: String) = withContext(Dispatchers.IO) {
        origin.getDetail(id)
    }

    override suspend fun getMoviesByType(type: ShowingType) = withContext(Dispatchers.IO) {
        origin.getMoviesByType(type)
    }
}