package movie.core

import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import java.util.Date

class EventFeatureRecoverSecondary(
    private val local: EventFeature,
    private val remote: EventFeature
) : EventFeature {

    override suspend fun getShowings(cinema: Cinema, at: Date) = tryOrRecover {
        getShowings(cinema, at)
    }

    override suspend fun getCinemas(location: Location?) = when (location) {
        null -> tryOrRecover { getCinemas(null) }
        else -> remote.getCinemas(location)
            .recoverCatching { local.getCinemas(location).getOrThrow() }
    }

    override suspend fun getDetail(movie: Movie) = tryOrRecover {
        getDetail(movie)
    }

    override suspend fun getCurrent() = tryOrRecover {
        getCurrent()
    }

    override suspend fun getUpcoming() = tryOrRecover {
        getUpcoming()
    }

    // ---

    private inline fun <T> tryOrRecover(body: EventFeature.() -> Result<T>): Result<T> {
        return local.run(body).recoverCatching { remote.run(body).getOrThrow() }
    }

}