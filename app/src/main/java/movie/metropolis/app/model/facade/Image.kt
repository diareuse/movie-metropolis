package movie.metropolis.app.model.facade

import java.nio.IntBuffer

class Image(
    val width: Int,
    val height: Int,
    private val pixels: IntBuffer
) {

    operator fun get(x: Int, y: Int): Int {
        return pixels[x + (y * width)]
    }

    operator fun iterator() = iterator {
        for (y in 0 until height)
            for (x in 0 until width)
                yield(Pixel(x, y, get(x, y)))
    }

    data class Pixel(
        val x: Int,
        val y: Int,
        val color: Int
    )

}