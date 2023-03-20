package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import movie.metropolis.app.model.Genre
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.OnChangedListener
import movie.metropolis.app.presentation.asLoadable

interface ListingFacade {

    suspend fun get(): Result<Action>
    suspend fun toggle(item: MovieView)

    fun addListener(listener: OnChangedListener): OnChangedListener
    fun removeListener(listener: OnChangedListener)

    interface Action {

        val promotions: Flow<Result<List<MovieView>>>
        val groups: Flow<Result<Map<Genre, List<MovieView>>>>

    }

    interface Factory {

        fun upcoming(): ListingFacade
        fun current(): ListingFacade

    }

    companion object {

        private val ListingFacade.toggleFlow
            get() = callbackFlow {
                val listener = addListener {
                    trySend(Unit)
                }
                awaitClose {
                    removeListener(listener)
                }
            }

        val ListingFacade.promotions: Flow<Loadable<List<MovieView>>>
            get() = flow {
                get().onSuccess { action ->
                    emitAll(action.promotions.map { it.asLoadable() })
                    toggleFlow.collect {
                        emitAll(action.promotions.map { it.asLoadable() })
                    }
                }.onFailure {
                    emit(Loadable.failure(it))
                }
            }

        val ListingFacade.groups: Flow<Loadable<Map<Genre, List<MovieView>>>>
            get() = flow {
                get().onSuccess { action ->
                    emitAll(action.groups.map { it.asLoadable() })
                    toggleFlow.collect {
                        emitAll(action.groups.map { it.asLoadable() })
                    }
                }.onFailure {
                    emit(Loadable.failure(it))
                }
            }

    }

}