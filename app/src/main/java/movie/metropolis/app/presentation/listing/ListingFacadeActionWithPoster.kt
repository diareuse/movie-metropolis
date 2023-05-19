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
        for ((index, item) in output.withIndex()) launch {
            val promo = cachePoster.getOrPut(item.id) { getPoster(item) }
            val poster = item.copy(poster = promo)
            output[index] = poster
            send(output.toList())
            if (promo != null) {
                output[index] = poster.copy(poster = getSpotColor(promo))
                send(output.toList())
            }
        }
    }.map(Result.Companion::success)

    private fun withSpotColors(items: Map<Genre, List<MovieView>>) = channelFlow {
        send(items)
        val output = items.mapValues { (_, it) -> it.toMutableList() }
        val writeLock = Mutex()
        val movies = items.asSequence()
            .flatMap { it.value }
            .distinctBy { it.id }
        for (movie in movies) launch {
            val poster = movie.poster?.run { getSpotColor(this) } ?: return@launch
            val updated = MovieViewWithPoster(movie, poster)
            writeLock.withLock {
                output.mapValues { (_, it) ->
                    it.replaceAll { m ->
                        if (m.id == movie.id) updated else m
                    }
                }
            }
            send(output)
        }
    }.map(Result.Companion::success)

    private suspend fun getPoster(movie: MovieView) = cachePoster.getOrPut(movie.id) {
        promo.get(movie.getBase())
            .map { ImageViewFromPoster(it, Color.Black) }
            .getOrNull()
    }

    private suspend fun getSpotColor(image: ImageView) = cacheColor.getOrPut(image.url) {
        val color = analyzer.getColors(image.url).vibrant.rgb.let(::Color)
        ImageViewWithColor(image, color)
    }

}