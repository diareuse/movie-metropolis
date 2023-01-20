package movie.metropolis.app.screen.listing

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import movie.core.ResultCallback
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.screen.OnChangedListener
import movie.metropolis.app.screen.asLoadable

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
            get() = flow {
                favoriteChangedFlow.collect {
                    getCurrent {
                        emit(it.asLoadable())
                    }
                }
            }

        val ListingFacade.upcomingFlow
            get() = flow {
                favoriteChangedFlow.collect {
                    getUpcoming {
                        emit(it.asLoadable())
                    }
                }
            }

    }

}