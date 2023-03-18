package movie.core.image

import movie.core.model.Media
import movie.image.ImageAnalyzer

suspend fun ImageAnalyzer.toSpotColor(media: Iterable<Media>): Int {
    for (item in media) {
        if (item !is Media.Image) continue
        return getColors(item.url).vibrant.rgb
    }
    return 0xff000000.toInt()
}