package movie.metropolis.app.screen.share

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import movie.metropolis.app.MainActivity
import movie.metropolis.app.util.toBitmap
import javax.inject.Inject

abstract class AbstractShareActivity : AppCompatActivity() {

    @Inject
    lateinit var facade: ShareFacade

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        val bitmap = getUri(intent)?.toBitmap(this) ?: return finish()
        val ticket = TicketRepresentation.Image(bitmap)
        val context = this
        lifecycleScope.launch {
            facade.putTicket(ticket)
            startActivity(MainActivity.tickets(context))
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

}