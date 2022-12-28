package movie.image

internal class ImageAnalyzerDefault : ImageAnalyzer {

    override suspend fun getColors(url: String): Swatch {
        return Swatch(emptyList())
    }

}