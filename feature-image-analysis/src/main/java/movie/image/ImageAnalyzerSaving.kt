package movie.image

import movie.image.database.ColorDao
import movie.image.database.ColorStored
import movie.image.database.ImageDao
import movie.image.database.ImageStored

internal class ImageAnalyzerSaving(
    private val origin: ImageAnalyzer,
    private val imageDao: ImageDao,
    private val colorDao: ColorDao
) : ImageAnalyzer {

    override suspend fun getColors(url: String) = origin.getColors(url).also {
        imageDao.insert(ImageStored(url))
        for (color in it.colors) {
            colorDao.insert(ColorStored(url, color.rgb))
        }
    }

}