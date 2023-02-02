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

    override suspend fun get(result: ResultCallback<List<MoviePreview>>) = coroutineScope {
        origin.get(result.thenMap(this) { movies ->
            movies.map inner@{ movie ->
                MoviePreviewWithSpotColor(
                    origin = movie,
                    spotColor = analyzer.toSpotColor(movie.media) ?: return@inner movie
                )
            }
        })
    }

}