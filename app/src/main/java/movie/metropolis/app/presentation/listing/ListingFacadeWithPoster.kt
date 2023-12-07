package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import movie.core.EventPromoFeature
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.ImageViewFromPoster
import movie.metropolis.app.model.adapter.MovieViewWithPoster

data class ListingFacadeWithPoster(
    private val origin: ListingFacade,
    private val promo: EventPromoFeature
) : ListingFacade by origin {

    private val cachePoster = mutableMapOf<String, ImageView?>()

    override fun get() = origin.get().flatMapLatest {
        flow {
            withPoster(it.promotions)
                .catch { _ -> emit(it.promotions) }
                .collect { promos ->
                    emit(it.copy(promotions = promos))
                }
        }
    }

    private fun withPoster(items: List<MovieView>): Flow<List<MovieView>> = channelFlow {
        val output = items.map { MovieViewWithPoster(it) }.toMutableList()
        if (cachePoster.isEmpty()) send(output)
        for ((index, item) in output.withIndex()) launch {
            val promo = cachePoster.getOrPut(item.id) {
                getPoster(item) ?: item.posterLarge ?: item.poster
            }
            val poster = item.copy(poster = promo)
            output[index] = poster
            send(output.toList())
        }
    }

    private suspend fun getPoster(movie: MovieView) = cachePoster.getOrPut(movie.id) {
        promo.get(movie.getBase())
            .map { ImageViewFromPoster(it) }
            .getOrNull()
    }

}