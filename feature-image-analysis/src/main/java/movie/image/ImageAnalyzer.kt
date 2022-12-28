package movie.image

interface ImageAnalyzer {

    suspend fun getColors(url: String): Swatch

}