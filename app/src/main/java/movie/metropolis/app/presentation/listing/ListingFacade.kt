package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import movie.core.ResultCallback
import movie.metropolis.app.model.Genre
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.OnChangedListener
import movie.metropolis.app.presentation.asLoadable
import movie.metropolis.app.util.throttleWithTimeout
import kotlin.time.Duration.Companion.seconds

interface ListingFacade {

    suspend fun get(callback: ResultCallback<Action>)
    suspend fun toggle(item: MovieView)

    fun addListener(listener: OnChangedListener): OnChangedListener
    fun removeListener(listener: OnChangedListener)

    interface Action {

        suspend fun promotions(callback: ResultCallback<List<MovieView>>)
        suspend fun groupUp(callback: ResultCallback<Map<Genre, List<MovieView>>>)

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

        val ListingFacade.actionsFlow
            get() = channelFlow {
                get {
                    send(it.asLoadable())
                }
                toggleFlow.collect {
                    get {
                        send(it.asLoadable())
                    }
                }
            }

        private val Action.promotionsFlow
            get() = channelFlow {
                promotions {
                    send(it.asLoadable())
                }
            }

        private val Action.groupFlow
            get() = channelFlow {
                groupUp {
                    send(it.asLoadable())
                }
            }

        fun promotionsFlow(actions: Flow<Loadable<Action>>) = actions
            .flatMapLatest { result ->
                result.getOrNull()?.promotionsFlow ?: flow {
                    emit(Loadable.failure(result.exceptionOrNull() ?: IllegalStateException()))
                }
            }
            .throttleWithTimeout(1.seconds)

        fun groupFlow(actions: Flow<Loadable<Action>>) = actions
            .flatMapLatest { result ->
                result.getOrNull()?.groupFlow ?: flow {
                    emit(Loadable.failure(result.exceptionOrNull() ?: IllegalStateException()))
                }
            }
            .throttleWithTimeout(1.seconds)

    }

}