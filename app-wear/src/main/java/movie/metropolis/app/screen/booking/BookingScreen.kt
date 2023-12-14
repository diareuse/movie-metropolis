package movie.metropolis.app.screen.booking

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.zxing.BarcodeFormat
import movie.style.Barcode
import movie.style.modifier.screenBrightness
import movie.style.util.findActivity

@Composable
fun BookingScreen(
    viewModel: BookingViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {
    DisposableEffect(Unit) {
        val window = context.findActivity().window
        val initialBrightness = window.attributes.screenBrightness
        window.attributes = window.attributes.apply {
            screenBrightness = 1f
        }
        onDispose {
            window.attributes = window.attributes.apply {
                screenBrightness = initialBrightness
            }
        }
    }
    Barcode(
        code = viewModel.id,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .screenBrightness(true),
        format = BarcodeFormat.QR_CODE
    )
}