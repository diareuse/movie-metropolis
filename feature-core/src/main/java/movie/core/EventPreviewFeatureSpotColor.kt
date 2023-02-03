package movie.core

import kotlinx.coroutines.coroutineScope
import movie.core.adapter.MoviePreviewWithSpotColor
import movie.core.image.toSpotColor
import movie.core.model.MoviePreview
import movie.image.ImageAnalyzer

class EventPreviewFeatureSpotColor(
    private val origin: EventPreviewFeature,
    private val analyzer: ImageAnalyzer
) : EventPreviewFeature {

    private val colors = mutableMapOf<String, Int>()

    override suspend fun get(result: ResultCallback<List<MoviePreview>>) = coroutineScope {
        val movies = MutableResult.getOrNull {
            origin.get(result.collectInto(it))
        }
        result.parallelize(this, movies ?: return@coroutineScope) { movie ->
            MoviePreviewWithSpotColor(movie, getColor(movie))
        }
    }

    private suspend fun getColor(movie: MoviePreview): Int {
        return colors.getOrPut(movie.id) {
            analyzer.toSpotColor(movie.media) ?: 0xff000000.toInt()
        }
    }

}