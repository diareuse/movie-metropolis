package movie.metropolis.app.presentation.listing

import movie.core.FavoriteFeature
import movie.core.ResultCallback
import movie.core.adapter.MovieFromId
import movie.core.thenMap
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewWithFavorite

class ListingAltFacadeActionFavorite(
    private val origin: ListingAltFacadeActionFromData,
    private val favorite: FavoriteFeature
) : ListingAltFacade.Action {

    override suspend fun promotions(callback: ResultCallback<List<MovieView>>) {
        origin.promotions(callback.thenMap { items ->
            items.markFavorite()
        })
    }

    override suspend fun groupUp(callback: ResultCallback<Map<String, List<MovieView>>>) {
        origin.groupUp(callback.thenMap { groups ->
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