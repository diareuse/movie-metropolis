package movie.core

import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import movie.log.logCatchingResult
import java.util.Date

class EventFeatureRecover(
    private val origin: EventFeature
) : EventFeature {

    override suspend fun getShowings(cinema: Cinema, at: Date) =
        origin.logCatchingResult("event-showings") { getShowings(cinema, at) }

    override suspend fun getCinemas(location: Location?) =
        origin.logCatchingResult("event-cinemas") { getCinemas(location) }

    override suspend fun getDetail(movie: Movie) =
        origin.logCatchingResult("event-detail") { getDetail(movie) }

    override suspend fun getCurrent() =
        origin.logCatchingResult("event-current") { getCurrent() }

    override suspend fun getUpcoming() =
        origin.logCatchingResult("event-upcoming") { getUpcoming() }

}