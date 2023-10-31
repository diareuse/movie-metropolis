package movie.metropolis.app.presentation.listing

import movie.core.EventDetailFeature
import movie.core.EventPreviewFeature
import movie.core.EventPromoFeature
import movie.core.FavoriteFeature
import movie.image.ImageAnalyzer
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.Listenable
import movie.metropolis.app.presentation.OnChangedListener
import movie.rating.MetadataProvider

class ListingFacadeCurrent(
    private val preview: EventPreviewFeature,
    private val promo: EventPromoFeature,
    private val analyzer: ImageAnalyzer,
    private val rating: MetadataProvider,
    private val favorite: FavoriteFeature,
    private val detail: EventDetailFeature
) : ListingFacade {

    private val listenable = Listenable<OnChangedListener>()

    override suspend fun get(): Result<ListingFacade.Action> = preview.get().map {
        var out: ListingFacade.Action
        out = ListingFacadeActionFromData(it.asIterable())
        out = ListingFacadeActionFavorite(out, favorite)
        out = ListingFacadeActionWithPoster(out, promo, analyzer)
        out = ListingFacadeActionWithRating(out, rating, detail)
        out
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