package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import movie.core.EventPromoFeature
import movie.image.ImageAnalyzer
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.ImageViewFromPoster
import movie.metropolis.app.model.adapter.MovieViewWithPoster
import movie.metropolis.app.util.flatMapResult

data class ListingFacadeActionWithPoster(
    private val origin: ListingFacade.Action,
    private val promo: EventPromoFeature,
    private val analyzer: ImageAnalyzer
) : ListingFacade.Action by origin {

    private val cachePoster = mutableMapOf<String, ImageView?>()

    override val promotions = origin.promotions.flatMapResult { withPoster(it) }
    override val groups = origin.groups

    private fun withPoster(items: List<MovieView>) = channelFlow {
        val output = items.map { MovieViewWithPoster(it) }.toMutableList()
        send(output)
        for ((index, item) in output.withIndex()) launch {
            val promo = cachePoster.getOrPut(item.id) { getPoster(item) }
            val poster = item.copy(poster = promo)
            output[index] = poster
            send(output.toList())
        }
    }.map(Result.Companion::success)

    private suspend fun getPoster(movie: MovieView) = cachePoster.getOrPut(movie.id) {
        promo.get(movie.getBase())
            .map { ImageViewFromPoster(it) }
            .getOrNull()
    }

}