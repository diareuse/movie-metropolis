package movie.metropolis.app.presentation.listing

import movie.core.EventPreviewFeature
import movie.core.EventPromoFeature
import movie.core.FavoriteFeature
import movie.core.ResultCallback
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.Listenable
import movie.metropolis.app.presentation.OnChangedListener

class ListingAltFacadeFromFeature(
    private val preview: EventPreviewFeature,
    private val favorite: FavoriteFeature,
    private val promo: EventPromoFeature
) : ListingAltFacade {

    private val listenable = Listenable<OnChangedListener>()

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

    override suspend fun toggle(item: MovieView) {
        favorite.toggle(item.getBase()).onSuccess {
            listenable.notify { onChanged() }
        }
    }

    override fun addListener(listener: OnChangedListener): OnChangedListener {
        listenable += listener
        return listener
    }

    override fun removeListener(listener: OnChangedListener) {
        listenable -= listener
    }

}