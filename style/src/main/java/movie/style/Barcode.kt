package movie.style

import android.graphics.Bitmap
import androidx.compose.foundation.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.style.theme.Theme
import movie.style.util.findActivity

@Composable
fun Barcode(
    code: String,
    format: BarcodeFormat,
    modifier: Modifier = Modifier,
    color: Color = Theme.color.container.background
) {
    val (size, onSizeChanged) = remember { mutableStateOf(IntSize.Zero) }
    var image by remember { mutableStateOf(null as ImageBitmap?) }
    LaunchedEffect(size, code, format) {
        val writer = MultiFormatWriter()
        image = withContext(Dispatchers.Default) {
            val w = size.width
            val h = size.height
            if (w * h <= 0) return@withContext null
            val matrix = writer.encode(code, format, w, h)
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            for (x in 0 until matrix.width) for (y in 0 until matrix.height) {
                val pixel = if (matrix.get(x, y)) color.toArgb() else Color.Transparent.toArgb()
                bitmap.setPixel(x, y, pixel)
            }
            bitmap.asImageBitmap()
        }
    }
    val context = LocalContext.current
    DisposableEffect(context) {
        val window = context.findActivity().window
        val initialBrightness = window.attributes.screenBrightness
        window.attributes = window.attributes.apply {
            screenBrightness = 1f
        }
        println(">in")
        onDispose {
            println(">gone")
            window.attributes = window.attributes.apply {
                screenBrightness = initialBrightness
            }
        }
    }
    Canvas(modifier.onSizeChanged(onSizeChanged)) {
        drawImage(image ?: return@Canvas)
    }
}