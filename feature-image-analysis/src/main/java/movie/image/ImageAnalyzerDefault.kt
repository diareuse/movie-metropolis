package movie.image

internal class ImageAnalyzerDefault : ImageAnalyzer {

    override suspend fun getColors(url: String) = Swatch(
        vibrant = SwatchColor.Black,
        light = SwatchColor.White,
        dark = SwatchColor.Black
    )

}