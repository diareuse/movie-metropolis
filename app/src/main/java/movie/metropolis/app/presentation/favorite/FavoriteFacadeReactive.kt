package movie.metropolis.app.presentation.favorite

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import movie.metropolis.app.model.MovieView

class FavoriteFacadeReactive(
    private val origin: FavoriteFacade
) : FavoriteFacade {

    private val trigger = Channel<Unit>()

    override fun get() = trigger.receiveAsFlow()
        .onStart { emit(Unit) }
        .flatMapLatest { origin.get() }

    override suspend fun remove(view: MovieView) {
        origin.remove(view)
        trigger.send(Unit)
    }
}