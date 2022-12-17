package movie.metropolis.app.screen.detail

import movie.metropolis.app.model.Filter
import java.util.Date

class MovieFacadeRecover(
    private val origin: MovieFacade
) : MovieFacade by origin {

    override suspend fun getAvailableFrom() =
        kotlin.runCatching { origin.getAvailableFrom().getOrThrow() }

    override suspend fun getMovie() =
        kotlin.runCatching { origin.getMovie().getOrThrow() }

    override suspend fun getPoster() =
        kotlin.runCatching { origin.getPoster().getOrThrow() }

    override suspend fun getTrailer() =
        kotlin.runCatching { origin.getTrailer().getOrThrow() }

    override suspend fun getShowings(
        date: Date,
        latitude: Double,
        longitude: Double
    ) = kotlin.runCatching { origin.getShowings(date, latitude, longitude).getOrThrow() }

    override suspend fun getOptions() =
        kotlin.runCatching { origin.getOptions().getOrThrow() }

    override fun toggle(filter: Filter) {
        kotlin.runCatching { origin.toggle(filter) }
    }

    override suspend fun isFavorite(): Result<Boolean> {
        return origin.runCatching { isFavorite().getOrThrow() }
    }

    override suspend fun toggleFavorite() {
        origin.runCatching { toggleFavorite() }
    }

}