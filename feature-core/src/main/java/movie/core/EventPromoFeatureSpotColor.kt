package movie.core

import kotlinx.coroutines.coroutineScope
import movie.core.adapter.MoviePromoPosterWithSpotColor
import movie.core.model.Movie
import movie.core.model.MoviePromoPoster
import movie.image.ImageAnalyzer

class EventPromoFeatureSpotColor(
    private val origin: EventPromoFeature,
    private val analyzer: ImageAnalyzer
) : EventPromoFeature {

    override suspend fun get(
        movie: Movie,
        callback: ResultCallback<MoviePromoPoster>
    ) = coroutineScope {
        origin.get(movie, callback.thenMap(this) {
            val color = analyzer.getColors(it.url).vibrant.rgb
            MoviePromoPosterWithSpotColor(it, color)
        })
    }

}