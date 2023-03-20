package movie.image

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class ImageAnalyzerSerial(
    private val analyzer: ImageAnalyzer
) : ImageAnalyzer {

    override suspend fun getColors(url: String): Swatch = lock.withLock {
        analyzer.getColors(url)
    }

    companion object {
        private val lock = Mutex()
    }

}