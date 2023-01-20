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
        origin.get(movie) outer@{
            result(it)
            val output = it.map { detail ->
                val color = analyzer.toSpotColor(detail.media) ?: return@outer
                MovieDetailWithSpotColor(detail, color)
            }
            result(output)
        }
    }

}