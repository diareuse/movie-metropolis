package movie.core

import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import movie.log.flatMapCatching
import java.util.Date

class EventFeatureRecover(
    private val origin: EventFeature
) : EventFeature {

    override suspend fun getShowings(cinema: Cinema, at: Date) =
        origin.flatMapCatching { getShowings(cinema, at) }

    override suspend fun getCinemas(location: Location?) =
        origin.flatMapCatching { getCinemas(location) }

    override suspend fun getDetail(movie: Movie) =
        origin.flatMapCatching { getDetail(movie) }

    override suspend fun getCurrent() =
        origin.flatMapCatching { getCurrent() }

    override suspend fun getUpcoming() =
        origin.flatMapCatching { getUpcoming() }

}