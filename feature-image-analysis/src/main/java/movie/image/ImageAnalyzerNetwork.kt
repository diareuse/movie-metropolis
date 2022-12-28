package movie.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.graphics.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.math.roundToInt

internal class ImageAnalyzerNetwork : ImageAnalyzer {

    override suspend fun getColors(url: String): Swatch {
        val bitmap = withContext(Dispatchers.IO) {
            URL(url).openStream().use { stream ->
                BitmapFactory.decodeStream(stream, null, getOptions())
            }
        }
        requireNotNull(bitmap)
        val colorMap = mutableMapOf<Int, Int>()
        for (x in 0 until bitmap.width)
            for (y in 0 until bitmap.height) {
                val color = bitmap[x, y]
                colorMap[color] = (colorMap[color] ?: 0) + 1
            }
        val threshold = ((bitmap.width * bitmap.height) * 0.1f).roundToInt()
        val colors = colorMap.asSequence()
            .takeWhile { (_, occurrence) -> occurrence >= threshold }
            .map { (color) -> SwatchColor(color) }
            .toList()
        return Swatch(colors)
    }

    private fun getOptions() = BitmapFactory.Options().apply {
        inPreferredConfig = Bitmap.Config.RGB_565
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            outConfig = Bitmap.Config.RGB_565
        }
    }

}