package movie.metropolis.app.screen.detail

import movie.core.Recoverable
import movie.core.ResultCallback
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
        runCatchingResult(callback) {
            origin.getAvailableFrom { result ->
                it(result.logSevere())
            }
        }
    }

    override suspend fun getMovie(callback: ResultCallback<MovieDetailView>) {
        runCatchingResult(callback) {
            origin.getMovie { result ->
                it(result.logSevere())
            }
        }
    }

    override suspend fun getPoster(callback: ResultCallback<ImageView>) {
        runCatchingResult(callback) {
            origin.getPoster { result ->
                it(result.logSevere())
            }
        }
    }

    override suspend fun getTrailer(callback: ResultCallback<VideoView>) {
        runCatchingResult(callback) {
            origin.getTrailer { result ->
                it(result.logSevere())
            }
        }
    }

    override suspend fun getShowings(
        date: Date,
        latitude: Double,
        longitude: Double,
        callback: ResultCallback<List<CinemaBookingView>>
    ) = runCatchingResult(callback) {
        origin.getShowings(date, latitude, longitude) { result ->
            it(result.logSevere())
        }
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