package movie.metropolis.app

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build.*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.GlobalHistogramBinarizer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import movie.core.TicketShareRegistry
import movie.core.UserFeature
import movie.core.di.Saving
import java.nio.IntBuffer
import javax.inject.Inject

@AndroidEntryPoint
class ShareActivity : ComponentActivity() {

    @Inject
    lateinit var share: TicketShareRegistry

    @Saving
    @Inject
    lateinit var user: UserFeature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = when (intent.action) {
            Intent.ACTION_OPEN_DOCUMENT,
            Intent.ACTION_VIEW -> intent.data

            Intent.ACTION_SEND -> if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(Intent.EXTRA_STREAM)
            }

            else -> return finish()
        } ?: return finish()
        val bitmap = applicationContext.contentResolver?.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        } ?: return finish()
        val pixels = IntBuffer.allocate(bitmap.width * bitmap.height)
        bitmap.copyPixelsToBuffer(pixels)
        bitmap.recycle()
        val source = RGBLuminanceSource(bitmap.width, bitmap.height, pixels.array())
        val binaryBitmap = BinaryBitmap(GlobalHistogramBinarizer(source))
        MultiFormatReader().runCatching { decode(binaryBitmap).text }
            .onFailure { finish() }
            .onSuccess { result ->
                lifecycleScope.launch {
                    share.add(result.encodeToByteArray())
                    user.getBookings()
                    val intent = Intent(this@ShareActivity, MainActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData("app://movie.metropolis/home?screen=tickets".toUri())
                    startActivity(intent)
                    finish()
                }
            }
    }

}