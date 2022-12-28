package movie.image

internal class ImageAnalyzerFallback(
    private vararg val origins: ImageAnalyzer
) : ImageAnalyzer {

    override suspend fun getColors(url: String): Swatch {
        for (origin in origins) try {
            return origin.getColors(url)
        } catch (e: Throwable) {
            continue
        }
        throw IllegalStateException("No valid analyzer could complete")
    }

}