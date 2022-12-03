package movie.metropolis.app.screen.detail

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import java.util.Date

interface MovieFacade {

    suspend fun getAvailableFrom(): Result<Date>
    suspend fun getMovie(): Result<MovieDetailView>
    suspend fun getPoster(): Result<ImageView>
    suspend fun getTrailer(): Result<VideoView>
    suspend fun getShowings(
        date: Date,
        latitude: Double,
        longitude: Double
    ): Result<List<CinemaBookingView>>

    fun interface Factory {
        fun create(id: String): MovieFacade
    }

    companion object {

        val MovieFacade.availableFromFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getAvailableFrom().asLoadable())
            }

        val MovieFacade.movieFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getMovie().asLoadable())
            }

        val MovieFacade.posterFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getPoster().asLoadable())
            }

        val MovieFacade.trailerFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getTrailer().asLoadable())
            }

        fun MovieFacade.showingsFlow(
            date: Flow<Date>,
            location: Flow<Location>
        ) = date
            .combine(location) { date, location -> date to location }
            .flatMapLatest { (date, location) ->
                flow {
                    emit(Loadable.loading())
                    emit(getShowings(date, location.latitude, location.longitude).asLoadable())
                }
            }

    }

}