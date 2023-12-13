package movie.metropolis.app.presentation.listing

import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import movie.core.EventPreviewFeature
import movie.core.FavoriteFeature
import movie.metropolis.app.model.ListingView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewFromFeature

class ListingFacadeFromFeature(
    private val preview: EventPreviewFeature,
    private val favorite: FavoriteFeature
) : ListingFacade {

    private val channel = Channel<Unit>()

    override fun get() = channel.receiveAsFlow().onStart { emit(Unit) }.map {
        val items = preview.get()
            .map { MovieViewFromFeature(it, false) }
            .toPersistentList()
        ListingView(items)
    }

    override suspend fun toggle(item: MovieView) {
        favorite.toggle(item.getBase())
        channel.trySend(Unit)
    }

}