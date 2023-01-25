package movie.metropolis.app.screen.listing

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import movie.core.ResultCallback
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.screen.OnChangedListener
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.util.throttleWithTimeout
import kotlin.time.Duration.Companion.seconds

interface ListingFacade {

    suspend fun getCurrent(callback: ResultCallback<List<MovieView>>)
    suspend fun getUpcoming(callback: ResultCallback<List<MovieView>>)
    suspend fun toggleFavorite(movie: MovieView)

    fun addOnFavoriteChangedListener(listener: OnChangedListener): OnChangedListener
    fun removeOnFavoriteChangedListener(listener: OnChangedListener)

    companion object {

        private val ListingFacade.favoriteChangedFlow
            get() = callbackFlow {
                send(Any())
                val listener = addOnFavoriteChangedListener {
                    trySend(Any())
                }
                awaitClose {
                    removeOnFavoriteChangedListener(listener)
                }
            }

        val ListingFacade.currentFlow
            get() = channelFlow {
                favoriteChangedFlow.collect {
                    getCurrent {
                        send(it.asLoadable())
                    }
                }
            }.throttleWithTimeout(1.seconds)

        val ListingFacade.upcomingFlow
            get() = channelFlow {
                favoriteChangedFlow.collect {
                    getUpcoming {
                        send(it.asLoadable())
                    }
                }
            }.throttleWithTimeout(1.seconds)

    }

}