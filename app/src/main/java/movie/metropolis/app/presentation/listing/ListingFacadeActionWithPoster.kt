package movie.metropolis.app.presentation.listing

import androidx.compose.ui.graphics.*
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.core.EventPromoFeature
import movie.image.ImageAnalyzer
import movie.metropolis.app.model.Genre
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.ImageViewFromPoster
import movie.metropolis.app.model.adapter.ImageViewWithColor
import movie.metropolis.app.model.adapter.MovieViewWithPoster
import movie.metropolis.app.util.flatMapResult

data class ListingFacadeActionWithPoster(
    private val origin: ListingFacade.Action,
    private val promo: EventPromoFeature,
    private val analyzer: ImageAnalyzer
) : ListingFacade.Action by origin {

    private val cachePoster = mutableMapOf<String, ImageView?>()
    private val cacheColor = mutableMapOf<String, ImageView>()

    override val promotions = origin.promotions.flatMapResult { withPoster(it) }
    override val groups = origin.groups.flatMapResult { withSpotColors(it) }

    private fun withPoster(items: List<MovieView>) = channelFlow {
        val output = items.map { MovieViewWithPoster(it) }.toMutableList()
        send(output)
        for ((index, item) in items.withIndex()) launch {
            val promo = cachePoster.getOrPut(item.id) { getPoster(item) }
            val poster = MovieViewWithPoster(item, promo)
            output[index] = poster
            send(output.toList())
            if (promo != null) {
                val color = cacheColor.getOrPut(item.id) { getSpotColor(promo) }
                output[index] = poster.copy(poster = color)
                send(output.toList())
            }
        }
    }.map(Result.Companion::success)

    private fun withSpotColors(items: Map<Genre, List<MovieView>>) = channelFlow {
        val output = items.mapValues { (_, it) -> it.toMutableList() }
        val locks = mutableMapOf<String, Mutex>()
        send(output)
        for ((genre, movies) in items) {
            for ((index, movie) in movies.withIndex()) launch {
                val poster = movie.poster ?: return@launch
                val updated = locks.getOrPut(movie.id) { Mutex() }.withLock {
                    val rating = cacheColor.getOrPut(movie.id) { getSpotColor(poster) }
                    val updated = MovieViewWithPoster(movie, rating)
                    output.getValue(genre)[index] = updated
                    output.mapValues { (_, it) -> it.toList() }
                }
                send(updated)
            }
        }
    }.map(Result.Companion::success)

    private suspend fun getPoster(movie: MovieView): ImageView? = promo.get(movie.getBase())
        .map { ImageViewFromPoster(it, Color.Black) }
        .getOrNull()

    private suspend fun getSpotColor(image: ImageView): ImageView {
        val color = analyzer.getColors(image.url).vibrant.rgb.let(::Color)
        return ImageViewWithColor(image, color)
    }

}