package movie.core

import movie.core.adapter.MovieDetailWithSpotColor
import movie.core.adapter.MoviePreviewWithSpotColor
import movie.core.model.Media
import movie.core.model.Movie
import movie.image.ImageAnalyzer

class EventFeatureSpotColor(
    private val origin: EventFeature,
    private val analyzer: ImageAnalyzer
) : EventFeature by origin {

    override suspend fun getDetail(movie: Movie) = origin.getDetail(movie).map {
        MovieDetailWithSpotColor(it, it.media.toSpotColor() ?: return@map it)
    }

    override suspend fun getCurrent() = origin.getCurrent().map { values ->
        values.map inner@{
            MoviePreviewWithSpotColor(it, it.media.toSpotColor() ?: return@inner it)
        }
    }

    override suspend fun getUpcoming() = origin.getUpcoming().map { values ->
        values.map inner@{
            MoviePreviewWithSpotColor(it, it.media.toSpotColor() ?: return@inner it)
        }
    }

    // ---

    private suspend fun Iterable<Media>.toSpotColor() = asSequence()
        .filterIsInstance<Media.Image>()
        .minByOrNull { it.width * it.height }
        ?.let { analyzer.getColors(it.url).vibrant.rgb }

}