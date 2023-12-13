package movie.metropolis.app.presentation.listing

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.map
import movie.core.FavoriteFeature
import movie.core.adapter.MovieFromId
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewWithFavorite

class ListingFacadeFavorite(
    private val origin: ListingFacade,
    private val favorite: FavoriteFeature
) : ListingFacade by origin {

    private val cache = mutableMapOf<String, Boolean>()

    override fun get() = origin.get().map {
        try {
            it.copy(
                items = it.items.markFavorite(),
                promotions = it.promotions.markFavorite()
            )
        } catch (ignore: Throwable) {
            it
        }
    }

    override suspend fun toggle(item: MovieView) {
        cache.remove(item.id)
        origin.toggle(item)
    }

    // ---

    private suspend fun List<MovieView>.markFavorite(): ImmutableList<MovieView> = map {
        val movie = MovieFromId(it.id)
        val isFavorite = cache.getOrPut(movie.id) { favorite.isFavorite(movie) }
        MovieViewWithFavorite(it, isFavorite)
    }.toPersistentList()

}