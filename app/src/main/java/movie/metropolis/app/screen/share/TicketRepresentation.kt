package movie.metropolis.app.screen.share

import java.nio.IntBuffer

sealed class TicketRepresentation {

    val bytes
        get() = when (this) {
            is Image -> throw IllegalArgumentException("Cannot convert image to bytes")
            is Text -> value.encodeToByteArray()
        }

    data class Text(val value: String) : TicketRepresentation()
    data class Image(
        val width: Int,
        val height: Int,
        val image: IntBuffer
    ) : TicketRepresentation()

}