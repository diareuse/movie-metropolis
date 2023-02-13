package movie.metropolis.app.presentation.detail

import movie.core.Recoverable
import movie.core.ResultCallback
import movie.core.result
import movie.log.flatMapCatching
import movie.log.log
import movie.log.logSevere
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.VideoView
import java.util.Date

class MovieFacadeRecover(
    private val origin: MovieFacade
) : MovieFacade by origin, Recoverable {

    override suspend fun getAvailableFrom(callback: ResultCallback<Date>) {
        runCatchingResult(callback.result { it.logSevere() }) {
            origin.getAvailableFrom(it)
        }
    }

    override suspend fun getMovie(callback: ResultCallback<MovieDetailView>) {
        runCatchingResult(callback.result { it.logSevere() }) {
            origin.getMovie(it)
        }
    }

    override suspend fun getPoster(callback: ResultCallback<ImageView>) {
        runCatchingResult(callback.result { it.logSevere() }) {
            origin.getPoster(it)
        }
    }

    override suspend fun getTrailer(callback: ResultCallback<VideoView>) {
        runCatchingResult(callback.result { it.logSevere() }) {
            origin.getTrailer(it)
        }
    }

    override suspend fun getShowings(
        date: Date,
        latitude: Double,
        longitude: Double,
        callback: ResultCallback<List<CinemaBookingView>>
    ) = runCatchingResult(callback.result { it.logSevere() }) {
        origin.getShowings(date, latitude, longitude, it)
    }

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