package movie.metropolis.app.screen.detail

import android.location.Location
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
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
import kotlin.time.Duration.Companion.seconds

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
            }.debounce(1.seconds)

        val MovieFacade.favoriteFlow
            get() = channelFlow {
                send(Loadable.loading())
                favoriteChangedFlow.collect {
                    send(isFavorite().asLoadable())
                }
            }.debounce(1.seconds)

        val MovieFacade.availableFromFlow
            get() = channelFlow {
                send(Loadable.loading())
                getAvailableFrom {
                    send(it.asLoadable())
                }
            }.debounce(1.seconds)

        val MovieFacade.movieFlow
            get() = channelFlow {
                send(Loadable.loading())
                getMovie {
                    send(it.asLoadable())
                }
            }.debounce(1.seconds)

        val MovieFacade.posterFlow
            get() = channelFlow {
                send(Loadable.loading())
                getPoster {
                    send(it.asLoadable())
                }
            }.debounce(1.seconds)

        val MovieFacade.trailerFlow
            get() = channelFlow {
                send(Loadable.loading())
                getTrailer {
                    send(it.asLoadable())
                }
            }.debounce(1.seconds)

        fun MovieFacade.showingsFlow(
            date: Flow<Date>,
            location: Flow<Location>
        ) = date
            .combine(location) { date, location -> date to location }
            .flatMapLatest { (date, location) ->
                channelFlow {
                    send(Loadable.loading())
                    optionsChangedFlow.collect {
                        getShowings(date, location.latitude, location.longitude) {
                            send(it.asLoadable())
                        }
                    }
                }
            }
            .debounce(1.seconds)

    }

}