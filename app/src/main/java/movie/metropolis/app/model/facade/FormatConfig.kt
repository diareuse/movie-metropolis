package movie.metropolis.app.model.facade

import androidx.compose.runtime.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.IntBuffer

@Stable
data class FormatConfig(
    private val writer: MultiFormatWriter,
    private val width: Int,
    private val height: Int,
    private val format: BarcodeFormat
) {

    suspend fun getImage(value: String) = withContext(Dispatchers.Default) {
        check(width * height >= 0)
        val matrix = writer.encode(value, format, width, height)
        val width = matrix.width
        val height = matrix.height
        val pixels = IntBuffer.allocate(width * height)
        for (y in 0 until height)
            for (x in 0 until width)
                pixels.put(if (matrix.get(x, y)) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
        Image(width, height, pixels)
    }

}