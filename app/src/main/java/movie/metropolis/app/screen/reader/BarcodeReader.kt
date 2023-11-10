package movie.metropolis.app.screen.reader

import android.Manifest
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.viewinterop.*
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.journeyapps.barcodescanner.camera.CenterCropStrategy
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import android.graphics.Color as AndroidColor

@RequiresPermission(Manifest.permission.CAMERA)
@Composable
fun BarcodeReader(
    format: BarcodeFormat,
    onBarcodeRead: (String) -> Unit,
    modifier: Modifier = Modifier
) = BarcodeReader(
    formats = persistentListOf(format),
    onBarcodeRead = onBarcodeRead,
    modifier = modifier
)

@RequiresPermission(Manifest.permission.CAMERA)
@Composable
fun BarcodeReader(
    formats: ImmutableList<BarcodeFormat>,
    onBarcodeRead: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val disposer = remember { Disposer() }
    val callback = remember(onBarcodeRead) {
        var last: String? = null
        BarcodeCallback {
            val text = it?.text ?: return@BarcodeCallback
            if (text == last) return@BarcodeCallback
            last = text
            onBarcodeRead(text)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            disposer.dispose()
        }
    }
    AndroidView(
        modifier = modifier,
        factory = { context ->
            DecoratedBarcodeView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                viewFinder.apply {
                    setLaserVisibility(false)
                    setMaskColor(AndroidColor.TRANSPARENT)
                }
                barcodeView.apply {
                    previewScalingStrategy = CenterCropStrategy()
                    isUseTextureView = true
                }
                cameraSettings.apply {
                    isAutoFocusEnabled = true
                }
                decoderFactory = DefaultDecoderFactory(formats)
                setStatusText("")
                decodeContinuous(callback)
                resume()
                disposer.addListener {
                    barcodeView.stopDecoding()
                    pause()
                }
            }
        },
        update = {
            if (!disposer.isDisposed)
                it.decodeContinuous(callback)
        }
    )
}

class Disposer {

    private val listeners = mutableSetOf<Listener>()
    var isDisposed = false
        private set

    fun dispose() {
        for (listener in listeners)
            listener.invoke()
        listeners.clear()
        isDisposed = true
    }

    fun addListener(listener: Listener) {
        listeners += listener
    }

    fun interface Listener {
        fun invoke()
    }

}