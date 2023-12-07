package movie.metropolis.app.presentation.listing

import movie.core.FavoriteFeature
import movie.core.adapter.MovieFromId
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewWithFavorite
import movie.metropolis.app.util.mapResult

class ListingFacadeActionFavorite(
    origin: ListingFacadeActionFromData,
    private val favorite: FavoriteFeature
) : ListingFacade.Action {

    override val promotions = origin.promotions.mapResult { it.markFavorite() }

    override val groups =
        origin.groups.mapResult { it.mapValues { (_, items) -> items.markFavorite() } }

    // ---

    private suspend fun List<MovieView>.markFavorite(): List<MovieView> = map {
        val movie = MovieFromId(it.id)
        val isFavorite = favorite.isFavorite(movie)
        MovieViewWithFavorite(it, isFavorite)
    }

}