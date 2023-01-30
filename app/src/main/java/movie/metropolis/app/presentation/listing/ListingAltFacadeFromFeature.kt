package movie.metropolis.app.presentation.listing

import movie.core.EventPreviewFeature
import movie.core.EventPromoFeature
import movie.core.FavoriteFeature
import movie.core.ResultCallback

class ListingAltFacadeFromFeature(
    private val preview: EventPreviewFeature,
    private val favorite: FavoriteFeature,
    private val promo: EventPromoFeature
) : ListingAltFacade {

    override suspend fun get(callback: ResultCallback<ListingAltFacade.Action>) {
        preview.get { result ->
            val output = result.map {
                var out: ListingAltFacade.Action
                out = ListingAltFacadeActionFromData(it)
                out = ListingAltFacadeActionFavorite(out, favorite)
                out = ListingAltFacadeActionWithPoster(out, promo)
                out
            }
            callback(output)
        }
    }

}