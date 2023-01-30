package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.coroutineScope
import movie.core.EventPromoFeature
import movie.core.EventPromoFeature.Companion.get
import movie.core.ResultCallback
import movie.core.adapter.MovieFromId
import movie.core.thenMap
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewWithPoster

data class ListingAltFacadeActionWithPoster(
    private val origin: ListingAltFacade.Action,
    private val promo: EventPromoFeature
) : ListingAltFacade.Action by origin {

    override suspend fun promotions(callback: ResultCallback<List<MovieView>>) = coroutineScope {
        origin.promotions(callback.thenMap { movies ->
            movies.map {
                when (val poster = promo.get(MovieFromId(it.id)).getOrNull()) {
                    null -> it
                    else -> MovieViewWithPoster(it, poster)
                }
            }
        })
    }

}