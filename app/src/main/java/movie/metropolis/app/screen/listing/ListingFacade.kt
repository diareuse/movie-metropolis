package movie.metropolis.app.screen.listing

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.screen.OnChangedListener
import movie.metropolis.app.screen.asLoadable

interface ListingFacade {

    suspend fun getCurrent(): Result<List<MovieView>>
    suspend fun getUpcoming(): Result<List<MovieView>>
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
                    emit(getCurrent().asLoadable())
                }
            }

        val ListingFacade.upcomingFlow
            get() = flow {
                favoriteChangedFlow.collect {
                    emit(getUpcoming().asLoadable())
                }
            }

    }

}