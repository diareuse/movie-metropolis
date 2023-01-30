package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import movie.core.ResultCallback
import movie.metropolis.app.model.Genre
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.asLoadable
import movie.metropolis.app.util.throttleWithTimeout
import kotlin.time.Duration.Companion.seconds

interface ListingAltFacade {

    suspend fun get(callback: ResultCallback<Action>)

    interface Action {

        suspend fun promotions(callback: ResultCallback<List<MovieView>>)
        suspend fun groupUp(callback: ResultCallback<Map<Genre, List<MovieView>>>)

    }

    interface Factory {

        fun upcoming(): ListingAltFacade
        fun current(): ListingAltFacade

    }

    companion object {

        val ListingAltFacade.actionsFlow
            get() = channelFlow {
                get {
                    send(it.asLoadable())
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