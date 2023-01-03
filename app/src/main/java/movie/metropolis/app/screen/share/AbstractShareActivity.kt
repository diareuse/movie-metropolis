package movie.metropolis.app.screen.share

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import movie.metropolis.app.MainActivity
import java.nio.IntBuffer
import javax.inject.Inject

abstract class AbstractShareActivity : ComponentActivity() {

    @Inject
    lateinit var facade: ShareFacade

    private val Bitmap.pixels
        get() = IntBuffer.allocate(width * height).apply {
            copyPixelsToBuffer(this)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = getUri(intent) ?: return finish()
        val bitmap = getBitmap(uri) ?: return finish()
        val ticket = TicketRepresentation.Image(bitmap.width, bitmap.height, bitmap.pixels)
        bitmap.recycle()
        lifecycleScope.launch {
            facade.putTicket(ticket)
            startActivity(getHomeIntent())
            finish()
        }
    }

    private fun getUri(intent: Intent): Uri? = when (intent.action) {
        Intent.ACTION_OPEN_DOCUMENT,
        Intent.ACTION_VIEW -> intent.data

        Intent.ACTION_SEND -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
        }

        else -> null
    }

    private fun getBitmap(uri: Uri) = applicationContext.contentResolver
        ?.openInputStream(uri)
        ?.use(BitmapFactory::decodeStream)

    private fun getHomeIntent() = Intent(this, MainActivity::class.java)
        .setAction(Intent.ACTION_VIEW)
        .setData("app://movie.metropolis/home?screen=tickets".toUri())

}