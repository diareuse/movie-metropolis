package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow

class MovieFacadeReactive(
    private val origin: MovieFacade
) : MovieFacade by origin {

    private val trigger = Channel<Any>(Channel.RENDEZVOUS)

    override val favorite: Flow<Boolean> = trigger.receiveAsFlow()
        .flatMapLatest { origin.favorite }
        .onStart { emit(origin.favorite.first()) }

    override suspend fun toggleFavorite() {
        origin.toggleFavorite()
        trigger.send(Any())
    }

}