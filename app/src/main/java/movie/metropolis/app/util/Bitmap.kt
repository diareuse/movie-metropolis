package movie.metropolis.app.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.nio.IntBuffer

val Bitmap.pixels
    get() = IntBuffer.allocate(width * height).apply {
        copyPixelsToBuffer(this)
    }

@SuppressLint("Recycle")
fun Uri.toBitmap(context: Context) = context.contentResolver
    ?.openInputStream(this)
    ?.use(BitmapFactory::decodeStream)