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
        colorDao.insert(ColorStored(url, it.vibrant.rgb, ColorStored.ClassVibrant))
        colorDao.insert(ColorStored(url, it.dark.rgb, ColorStored.ClassDark))
        colorDao.insert(ColorStored(url, it.light.rgb, ColorStored.ClassLight))
    }

}