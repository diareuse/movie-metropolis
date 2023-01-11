package movie.core.nwk

import movie.core.nwk.model.ShowingType
import java.util.Date

class EventServicePerformance(
    private val origin: EventService,
    private val tracer: PerformanceTracer
) : EventService {

    override suspend fun getEventsInCinema(
        cinema: String,
        date: Date
    ) = tracer.trace("api.events") {
        origin.getEventsInCinema(cinema, date).also { result ->
            it.setState(result.isSuccess)
        }
    }

    override suspend fun getNearbyCinemas(
        lat: Double,
        lng: Double
    ) = tracer.trace("api.nearby") {
        origin.getNearbyCinemas(lat, lng).also { result ->
            it.setState(result.isSuccess)
        }
    }

    override suspend fun getDetail(id: String) = tracer.trace("api.detail") {
        origin.getDetail(id).also { result ->
            it.setState(result.isSuccess)
        }
    }

    override suspend fun getMoviesByType(type: ShowingType) = tracer.trace("api.movies-typed") {
        origin.getMoviesByType(type).also { result ->
            it.setState(result.isSuccess)
        }
    }

}