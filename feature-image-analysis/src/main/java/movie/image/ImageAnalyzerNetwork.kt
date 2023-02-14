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
        val bitmap = getImage(URL(url))
        requireNotNull(bitmap)
        val palette = withContext(Dispatchers.Default) {
            Palette.from(bitmap)
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

    private fun Palette.Swatch.asSwatch(): SwatchColor =
        SwatchColor(rgb or 0xff000000.toInt())

    private suspend fun getImage(url: URL): Bitmap? {
        val options = getOptions()
        decodeBitmap(url, options)
        options.calculateInSampleSize(100, 100)
        return decodeBitmap(url, options)
    }

    private suspend fun decodeBitmap(
        url: URL,
        options: BitmapFactory.Options
    ) = withContext(Dispatchers.IO) {
        url.openStream().use {
            BitmapFactory.decodeStream(it, null, options)
        }
    }

    private fun BitmapFactory.Options.calculateInSampleSize(reqWidth: Int, reqHeight: Int) {
        val height = outHeight
        val width = outWidth
        inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            while (
                halfHeight / inSampleSize >= reqHeight &&
                halfWidth / inSampleSize >= reqWidth
            ) {
                inSampleSize *= 2
            }
        }

        inJustDecodeBounds = false
    }


    private fun getOptions() = BitmapFactory.Options().apply {
        inPreferredConfig = Bitmap.Config.RGB_565
        inJustDecodeBounds = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            outConfig = Bitmap.Config.RGB_565
        }
    }

}