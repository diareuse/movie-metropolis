package movie.metropolis.app.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.metropolis.app.model.facade.Image
import java.io.File
import java.nio.IntBuffer

val Bitmap.pixels
    get() = IntBuffer.allocate(width * height).apply {
        copyPixelsToBuffer(this)
    }

@SuppressLint("Recycle")
fun Uri.toBitmap(context: Context) = context.contentResolver
    ?.openInputStream(this)
    ?.use(BitmapFactory::decodeStream)

suspend fun Image.writeTo(file: File) {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    for ((x, y, color) in this)
        bitmap.setPixel(x, y, color)
    withContext(Dispatchers.IO) {
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            bitmap.recycle()
        }
    }
}