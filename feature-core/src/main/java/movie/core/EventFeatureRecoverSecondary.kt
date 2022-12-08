package movie.core

import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import java.util.Date

class EventFeatureRecoverSecondary(
    private val primary: EventFeature,
    private val secondary: EventFeature
) : EventFeature {

    override suspend fun getShowings(cinema: Cinema, at: Date) = tryOrRecover {
        getShowings(cinema, at)
    }

    override suspend fun getCinemas(location: Location?) = tryOrRecover {
        getCinemas(location)
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
        return primary.run(body).recoverCatching { secondary.run(body).getOrThrow() }
    }

}