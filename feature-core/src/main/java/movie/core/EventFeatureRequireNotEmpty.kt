package movie.core

import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import java.util.Date

class EventFeatureRequireNotEmpty(
    private val origin: EventFeature
) : EventFeature by origin {

    override suspend fun getShowings(cinema: Cinema, at: Date) = origin.getShowings(cinema, at)
        .mapCatching { require(it.isNotEmpty()); it }

    @Suppress("ReplaceSizeCheckWithIsNotEmpty")
    override suspend fun getCinemas(location: Location?) = origin.getCinemas(location)
        .mapCatching { require(it.count() > 0); it }

    @Suppress("RedundantRequireNotNullCall")
    override suspend fun getDetail(movie: Movie) = origin.getDetail(movie)
        .mapCatching { requireNotNull(it); it }

    @Suppress("ReplaceSizeCheckWithIsNotEmpty")
    override suspend fun getCurrent() = origin.getCurrent()
        .mapCatching { require(it.count() > 0); it }

    @Suppress("ReplaceSizeCheckWithIsNotEmpty")
    override suspend fun getUpcoming() = origin.getUpcoming()
        .mapCatching { require(it.count() > 0); it }

}