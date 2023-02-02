package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.coroutineScope
import movie.core.FavoriteFeature
import movie.core.ResultCallback
import movie.core.adapter.MovieFromId
import movie.core.thenMap
import movie.metropolis.app.model.Genre
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewWithFavorite

class ListingFacadeActionFavorite(
    private val origin: ListingFacadeActionFromData,
    private val favorite: FavoriteFeature
) : ListingFacade.Action {

    override suspend fun promotions(callback: ResultCallback<List<MovieView>>) = coroutineScope {
        origin.promotions(callback.thenMap(this) { items ->
            items.markFavorite()
        })
    }

    override suspend fun groupUp(
        callback: ResultCallback<Map<Genre, List<MovieView>>>
    ) = coroutineScope {
        origin.groupUp(callback.thenMap(this) { groups ->
            groups.mapValues { (_, items) ->
                items.markFavorite()
            }
        })
    }

    // ---

    private suspend fun List<MovieView>.markFavorite(): List<MovieView> = map {
        val movie = MovieFromId(it.id)
        val isFavorite = favorite.isFavorite(movie).getOrDefault(false)
        MovieViewWithFavorite(it, isFavorite)
    }

}