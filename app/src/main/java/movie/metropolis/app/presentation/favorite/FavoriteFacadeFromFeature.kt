package movie.metropolis.app.presentation.favorite

import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.core.EventDetailFeature
import movie.core.FavoriteFeature
import movie.core.model.MovieDetail
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewFromMovie

class FavoriteFacadeFromFeature(
    private val favorite: FavoriteFeature,
    private val detail: EventDetailFeature
) : FavoriteFacade {

    private val cache = mutableMapOf<String, MovieDetail>()

    override fun get() = channelFlow {
        val input = favorite.getAll()
            .getOrDefault(emptyList())
            .map { MovieViewFromMovie(it, emptyList()) }
            .toMutableList()
        val mutex = Mutex()
        for ((index, it) in input.withIndex()) launch {
            val detail = cache.getOrPut(it.id) { detail.get(it.getBase()) }
            val out = mutex.withLock {
                input[index] = it.copy(media = detail.media)
                input.toList()
            }
            send(out)
        }
    }

    override suspend fun remove(view: MovieView) {
        favorite.toggle(view.getBase())
    }
}