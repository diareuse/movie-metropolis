package movie.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Target
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

internal class ImageAnalyzerNetwork : ImageAnalyzer {

    override suspend fun getColors(url: String): Swatch {
        val bitmap = withContext(Dispatchers.IO) {
            URL(url).openStream().use { stream ->
                BitmapFactory.decodeStream(stream, null, getOptions())
            }
        }
        requireNotNull(bitmap)
        val palette = withContext(Dispatchers.Default) {
            Palette.from(bitmap)
                .resizeBitmapArea(200)
                .addTarget(Target.VIBRANT)
                .addTarget(Target.LIGHT_MUTED)
                .addTarget(Target.DARK_MUTED)
                .generate()
        }
        bitmap.recycle()
        return Swatch(
            vibrant = palette.vibrantSwatch?.asSwatch() ?: SwatchColor.Black,
            light = palette.lightMutedSwatch?.asSwatch() ?: SwatchColor.White,
            dark = palette.darkMutedSwatch?.asSwatch() ?: SwatchColor.Black
        )
    }

    private fun Palette.Swatch.asSwatch(): SwatchColor = SwatchColor(rgb or 0xff000000.toInt())

    private fun getOptions() = BitmapFactory.Options().apply {
        inPreferredConfig = Bitmap.Config.RGB_565
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            outConfig = Bitmap.Config.RGB_565
        }
    }

}