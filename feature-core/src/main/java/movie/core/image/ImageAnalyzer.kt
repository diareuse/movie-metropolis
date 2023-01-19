package movie.core.image

import movie.core.model.Media
import movie.image.ImageAnalyzer

suspend fun ImageAnalyzer.toSpotColor(media: Iterable<Media>) = media.asSequence()
    .filterIsInstance<Media.Image>()
    .minByOrNull { it.width * it.height }
    ?.let { getColors(it.url).vibrant.rgb }