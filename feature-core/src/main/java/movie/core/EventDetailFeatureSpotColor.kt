package movie.core

import movie.core.adapter.MovieDetailWithSpotColor
import movie.core.image.toSpotColor
import movie.core.model.Movie
import movie.core.model.MovieDetail
import movie.image.ImageAnalyzer

class EventDetailFeatureSpotColor(
    private val origin: EventDetailFeature,
    private val analyzer: ImageAnalyzer
) : EventDetailFeature {

    override suspend fun get(movie: Movie, result: ResultCallback<MovieDetail>) {
        origin.get(movie, result.thenMap outer@{ detail ->
            val color = analyzer.toSpotColor(detail.media) ?: return@outer detail
            MovieDetailWithSpotColor(detail, color)
        })
    }

}