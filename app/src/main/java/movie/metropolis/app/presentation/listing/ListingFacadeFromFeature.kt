package movie.metropolis.app.presentation.listing

import movie.core.EventPreviewFeature
import movie.core.EventPromoFeature
import movie.core.FavoriteFeature
import movie.core.ResultCallback
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.Listenable
import movie.metropolis.app.presentation.OnChangedListener

class ListingFacadeFromFeature(
    private val preview: EventPreviewFeature,
    private val favorite: FavoriteFeature,
    private val promo: EventPromoFeature
) : ListingFacade {

    private val listenable = Listenable<OnChangedListener>()

    override suspend fun get(callback: ResultCallback<ListingFacade.Action>) {
        preview.get { result ->
            val output = result.map {
                var out: ListingFacade.Action
                out = ListingFacadeActionFromData(it)
                out = ListingFacadeActionFavorite(out, favorite)
                out = ListingFacadeActionWithPoster(out, promo)
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