package movie.core

import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import java.util.Date

class EventFeatureRecover(
    private val origin: EventFeature
) : EventFeature {

    override suspend fun getShowings(cinema: Cinema, at: Date) =
        origin.runCatching { getShowings(cinema, at).getOrThrow() }

    override suspend fun getCinemas(location: Location?) =
        origin.runCatching { getCinemas(location).getOrThrow() }

    override suspend fun getDetail(movie: Movie) =
        origin.runCatching { getDetail(movie).getOrThrow() }

    override suspend fun getCurrent() =
        origin.runCatching { getCurrent().getOrThrow() }

    override suspend fun getUpcoming() =
        origin.runCatching { getUpcoming().getOrThrow() }

}