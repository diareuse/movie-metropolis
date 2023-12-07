package movie.metropolis.app.presentation.listing

import movie.core.EventPreviewFeature
import movie.core.EventPromoFeature
import movie.core.FavoriteFeature
import movie.image.ImageAnalyzer
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.Listenable
import movie.metropolis.app.presentation.OnChangedListener

class ListingFacadeUpcoming(
    private val preview: EventPreviewFeature,
    private val favorite: FavoriteFeature,
    private val promo: EventPromoFeature,
    private val analyzer: ImageAnalyzer
) : ListingFacade {

    private val listenable = Listenable<OnChangedListener>()

    override suspend fun get(): Result<ListingFacade.Action> = preview.runCatching { get() }.map {
        var out: ListingFacade.Action
        out = ListingFacadeActionFromData(it.asIterable())
        out = ListingFacadeActionFavorite(out, favorite)
        out = ListingFacadeActionWithPoster(out, promo, analyzer)
        out
    }

    override suspend fun toggle(item: MovieView) {
        favorite.toggle(item.getBase())
        listenable.notify { onChanged() }
    }

    override fun addListener(listener: OnChangedListener): OnChangedListener {
        listenable += listener
        return listener
    }

    override fun removeListener(listener: OnChangedListener) {
        listenable -= listener
    }

}