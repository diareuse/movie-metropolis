package movie.metropolis.app.screen.detail

import android.location.Location
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import movie.core.ResultCallback
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.OnChangedListener
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.screen.cinema.BookingFilterable
import movie.metropolis.app.screen.cinema.BookingFilterable.Companion.optionsChangedFlow
import java.util.Date

interface MovieFacade : BookingFilterable {

    suspend fun isFavorite(): Result<Boolean>
    suspend fun getAvailableFrom(callback: ResultCallback<Date>)
    suspend fun getMovie(callback: ResultCallback<MovieDetailView>)
    suspend fun getPoster(callback: ResultCallback<ImageView>)
    suspend fun getTrailer(callback: ResultCallback<VideoView>)
    suspend fun getShowings(
        date: Date,
        latitude: Double,
        longitude: Double,
        callback: ResultCallback<List<CinemaBookingView>>
    )

    suspend fun toggleFavorite()
    fun addOnFavoriteChangedListener(listener: OnChangedListener): OnChangedListener
    fun removeOnFavoriteChangedListener(listener: OnChangedListener)

    fun interface Factory {
        fun create(id: String): MovieFacade
    }

    companion object {

        private val MovieFacade.favoriteChangedFlow
            get() = callbackFlow {
                send(Any())
                val listener = addOnFavoriteChangedListener {
                    trySend(Any())
                }
                awaitClose {
                    removeOnFavoriteChangedListener(listener)
                }
            }

        val MovieFacade.favoriteFlow
            get() = flow {
                emit(Loadable.loading())
                favoriteChangedFlow.collect {
                    emit(isFavorite().asLoadable())
                }
            }

        val MovieFacade.availableFromFlow
            get() = flow {
                emit(Loadable.loading())
                getAvailableFrom {
                    emit(it.asLoadable())
                }
            }

        val MovieFacade.movieFlow
            get() = flow {
                emit(Loadable.loading())
                getMovie {
                    emit(it.asLoadable())
                }
            }

        val MovieFacade.posterFlow
            get() = flow {
                emit(Loadable.loading())
                getPoster {
                    emit(it.asLoadable())
                }
            }

        val MovieFacade.trailerFlow
            get() = flow {
                emit(Loadable.loading())
                getTrailer {
                    emit(it.asLoadable())
                }
            }

        fun MovieFacade.showingsFlow(
            date: Flow<Date>,
            location: Flow<Location>
        ) = date
            .combine(location) { date, location -> date to location }
            .flatMapLatest { (date, location) ->
                flow {
                    emit(Loadable.loading())
                    optionsChangedFlow.collect {
                        getShowings(date, location.latitude, location.longitude) {
                            emit(it.asLoadable())
                        }
                    }
                }
            }

    }

}