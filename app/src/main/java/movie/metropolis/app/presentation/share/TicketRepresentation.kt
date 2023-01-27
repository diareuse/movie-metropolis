package movie.metropolis.app.presentation.share

import android.graphics.Bitmap
import movie.metropolis.app.util.pixels
import java.nio.IntBuffer

sealed class TicketRepresentation {

    val bytes
        get() = when (this) {
            is Image -> throw IllegalArgumentException("Cannot convert image to bytes")
            is Text -> value.encodeToByteArray()
        }

    data class Text(
        val value: String
    ) : TicketRepresentation()

    data class Image(
        val width: Int,
        val height: Int,
        val image: IntBuffer
    ) : TicketRepresentation() {

        constructor(
            bitmap: Bitmap
        ) : this(
            width = bitmap.width,
            height = bitmap.height,
            image = bitmap.pixels
        ) {
            bitmap.recycle()
        }

    }

}