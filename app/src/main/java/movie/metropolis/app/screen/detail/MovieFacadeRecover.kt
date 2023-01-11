package movie.metropolis.app.screen.detail

import movie.log.logCatching
import movie.log.logCatchingResult
import movie.metropolis.app.model.Filter
import java.util.Date

class MovieFacadeRecover(
    private val origin: MovieFacade
) : MovieFacade by origin {

    override suspend fun getAvailableFrom() =
        origin.logCatchingResult("movie-available-from") { getAvailableFrom() }

    override suspend fun getMovie() =
        origin.logCatchingResult("movie") { getMovie() }

    override suspend fun getPoster() =
        origin.logCatchingResult("movie-poster") { getPoster() }

    override suspend fun getTrailer() =
        origin.logCatchingResult("movie-trailer") { getTrailer() }

    override suspend fun getShowings(
        date: Date,
        latitude: Double,
        longitude: Double
    ) = origin.logCatchingResult("movie-showings") { getShowings(date, latitude, longitude) }

    override suspend fun getOptions() =
        origin.logCatchingResult("movie-options") { getOptions() }

    override fun toggle(filter: Filter) {
        origin.logCatching("movie-toggle-filter") { toggle(filter) }
    }

    override suspend fun isFavorite() =
        origin.logCatchingResult("movie-favorite") { isFavorite() }

    override suspend fun toggleFavorite() {
        origin.logCatching("movie-toggle-favorite") { toggleFavorite() }
    }

}