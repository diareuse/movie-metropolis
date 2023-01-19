package movie.core

import movie.core.model.Media
import movie.core.model.MoviePreview
import movie.image.ImageAnalyzer

interface EventPreviewFeature {

    suspend fun get(result: ResultCallback<List<MoviePreview>>)

    interface Factory {
        fun current(): EventPreviewFeature
        fun upcoming(): EventPreviewFeature
    }

}

suspend fun Iterable<Media>.toSpotColor(analyzer: ImageAnalyzer) = asSequence()
    .filterIsInstance<Media.Image>()
    .minByOrNull { it.width * it.height }
    ?.let { analyzer.getColors(it.url).vibrant.rgb }