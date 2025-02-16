package movie.style

import android.graphics.Bitmap
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.util.*
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import androidx.core.util.lruCache
import coil.compose.AsyncImage
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.style.util.encodeBase64
import java.io.File

@Composable
fun Barcode(
    code: String,
    modifier: Modifier = Modifier,
    format: BarcodeFormat = BarcodeFormat.QR_CODE
) = BoxWithConstraints(
    modifier = modifier,
    propagateMinConstraints = true
) {
    val context = LocalContext.current
    val file = remember(this, format, code) {
        val name = "%dx%d-%s-%s".format(
            maxWidth.value.toInt(),
            maxHeight.value.toInt(),
            format.name,
            code.encodeBase64()
        ).take(255) + ".png"
        val dir = File(context.cacheDir, "barcodes")
        val file = File(dir, name)
        CachedCodeWriter(file)
    }
    val density = LocalDensity.current
    if (!file.exists) LaunchedEffect(maxWidth, maxHeight, code, format) {
        file.write(maxWidth, maxHeight, density, format, code)
    }
    if (file.exists) AsyncImage(
        model = file.file,
        contentDescription = null
    ) else Box(
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(strokeCap = StrokeCap.Round)
    }
}

private val barcodeCache =
    lruCache<BarcodeDefinition, Bitmap>(1_000_000, { _, v -> v.allocationByteCount })

private data class BarcodeDefinition(
    val width: Int,
    val height: Int,
    val format: BarcodeFormat,
    val data: String
)

@Composable
fun Barcode2(
    code: String,
    modifier: Modifier = Modifier,
    format: BarcodeFormat = BarcodeFormat.QR_CODE
) = Box(modifier = modifier.drawWithCache {
    val definition = BarcodeDefinition(
        width = size.width.fastRoundToInt(),
        height = size.height.fastRoundToInt(),
        format = format,
        data = code
    )
    val cached = barcodeCache.get(definition)
    val image = cached?.asImageBitmap() ?: if (code.isNotEmpty()) {
        val (w, h) = definition
        val vertical = h > w
        val writer = MultiFormatWriter()
        val matrix = writer.encode(code, format, if (vertical) h else w, if (vertical) w else h)
        val bitmap = createBitmap(w, h)
        val black = Color.Black.toArgb()
        val white = Color.White.toArgb()
        if (vertical) matrix.rotate(90)
        for (x in 0 until matrix.width) for (y in 0 until matrix.height) {
            val pixel = when {
                matrix.get(x, y) -> black
                else -> white
            }
            bitmap[x, y] = pixel
        }
        barcodeCache.put(definition, bitmap)
        bitmap.asImageBitmap()
    } else null
    onDrawWithContent {
        if (image != null) drawImage(image)
    }
})

@Stable
private class CachedCodeWriter(val file: File) {

    private val writer = MultiFormatWriter()

    var exists by mutableStateOf(file.exists())
        private set

    suspend fun write(
        maxWidth: Dp,
        maxHeight: Dp,
        density: Density,
        type: BarcodeFormat,
        data: String
    ) = withContext(Dispatchers.Default) {
        with(density) {
            val w = maxWidth.roundToPx()
            val h = maxHeight.roundToPx()
            if (w * h <= 0) return@withContext
            val matrix = writer.encode(data, type, w, h)
            val bitmap = createBitmap(w, h)
            for (x in 0 until matrix.width) for (y in 0 until matrix.height) {
                val pixel = when {
                    matrix.get(x, y) -> Color.Black.toArgb()
                    else -> Color.Transparent.toArgb()
                }
                bitmap[x, y] = pixel
            }
            if (file.parentFile?.exists()?.not() == true) file.parentFile?.mkdirs()
            if (!file.exists()) file.createNewFile()
            file.outputStream().use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
            bitmap.recycle()
        }
    }.also {
        exists = file.exists()
    }

}