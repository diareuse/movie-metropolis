package movie.image

import movie.image.database.AnalysisDao

internal class ImageAnalyzerDatabase(
    private val dao: AnalysisDao
) : ImageAnalyzer {

    override suspend fun getColors(url: String): Swatch {
        val colors = dao.getColors(url).map { SwatchColor(it) }
        require(colors.isNotEmpty())
        return Swatch(colors)
    }
}