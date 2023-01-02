package movie.metropolis.app

import android.content.Intent
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import movie.metropolis.app.screen.Navigation
import movie.metropolis.app.theme.Theme
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            Theme {
                Navigation(
                    onPermissionsRequested = { requestPermissions(it) },
                    onLinkClicked = ::openExternal,
                    onShareFile = ::share
                )
            }
        }
    }

    private suspend fun requestPermissions(permissions: Array<String>): Boolean {
        val contract = ActivityResultContracts.RequestMultiplePermissions()
        val result = activityResultRegistry.register("permissions", contract, permissions)
        return result.all { it.value }
    }

    private suspend fun <I, O> ActivityResultRegistry.register(
        key: String,
        contract: ActivityResultContract<I, O>,
        input: I
    ): O = suspendCoroutine { cont ->
        register(key, contract) {
            cont.resume(it)
        }.launch(input)
    }

    private fun openExternal(link: String) {
        Intent(Intent.ACTION_VIEW)
            .setData(link.toUri())
            .let { Intent.createChooser(it, "") }
            .also(::startActivity)
    }

    private fun share(file: File) {
        val uri = getUriForFile(this, "movie.metropolis.PROVIDER", file, file.name)
        Intent(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_STREAM, uri)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension))
            .let { Intent.createChooser(it, file.name) }
            .also(::startActivity)
    }

}