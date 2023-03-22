package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow

class MovieFacadeReactive(
    private val origin: MovieFacade
) : MovieFacade by origin {

    private val trigger = Channel<Any>(Channel.RENDEZVOUS)

    override val favorite: Flow<Boolean> = combine(
        trigger.receiveAsFlow().onStart { emit(Any()) },
        origin.favorite
    ) { _, favorite ->
        favorite
    }

    override suspend fun toggleFavorite() {
        origin.toggleFavorite()
        trigger.trySend(Any())
    }

}