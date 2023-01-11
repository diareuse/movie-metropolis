package movie.metropolis.app.screen.detail

import movie.log.flatMapCatching
import movie.log.log
import movie.log.logSevere
import movie.metropolis.app.model.Filter
import java.util.Date

class MovieFacadeRecover(
    private val origin: MovieFacade
) : MovieFacade by origin {

    override suspend fun getAvailableFrom() =
        origin.flatMapCatching { getAvailableFrom() }.logSevere()

    override suspend fun getMovie() =
        origin.flatMapCatching { getMovie() }.logSevere()

    override suspend fun getPoster() =
        origin.flatMapCatching { getPoster() }.logSevere()

    override suspend fun getTrailer() =
        origin.flatMapCatching { getTrailer() }.logSevere()

    override suspend fun getShowings(
        date: Date,
        latitude: Double,
        longitude: Double
    ) = origin.flatMapCatching { getShowings(date, latitude, longitude) }.logSevere()

    override suspend fun getOptions() =
        origin.flatMapCatching { getOptions() }.log()

    override fun toggle(filter: Filter) {
        origin.runCatching { toggle(filter) }.logSevere()
    }

    override suspend fun isFavorite() =
        origin.flatMapCatching { isFavorite() }.logSevere()

    override suspend fun toggleFavorite() {
        origin.runCatching { toggleFavorite() }.logSevere()
    }

}