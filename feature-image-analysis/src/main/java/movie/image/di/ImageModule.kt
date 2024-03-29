package movie.image.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movie.image.ImageAnalyzer
import movie.image.ImageAnalyzerDatabase
import movie.image.ImageAnalyzerDefault
import movie.image.ImageAnalyzerFallback
import movie.image.ImageAnalyzerNetwork
import movie.image.ImageAnalyzerSaving
import movie.image.ImageAnalyzerSerial
import movie.image.database.AnalysisDao
import movie.image.database.ColorDao
import movie.image.database.ImageDao

@Module
@InstallIn(SingletonComponent::class)
internal class ImageModule {

    @Provides
    @Reusable
    fun analyzer(
        imageDao: ImageDao,
        colorDao: ColorDao,
        analysisDao: AnalysisDao
    ): ImageAnalyzer {
        var out: ImageAnalyzer
        out = ImageAnalyzerNetwork()
        out = ImageAnalyzerSaving(out, imageDao, colorDao)
        out = ImageAnalyzerSerial(out)

        return ImageAnalyzerFallback(
            ImageAnalyzerDatabase(analysisDao),
            out,
            ImageAnalyzerDefault()
        )
    }

}

