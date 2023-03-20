package movie.image

import movie.image.database.AnalysisDao

internal class ImageAnalyzerDatabase(
    private val dao: AnalysisDao
) : ImageAnalyzer {

    override suspend fun getColors(url: String) = Swatch(
        vibrant = SwatchColor(dao.getVibrant(url).let(::requireNotNull)),
        light = SwatchColor(dao.getLight(url).let(::requireNotNull)),
        dark = SwatchColor(dao.getDark(url).let(::requireNotNull))
    )

}

