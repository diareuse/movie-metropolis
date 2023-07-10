package movie.metropolis.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.runtime.*
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import movie.metropolis.app.feature.play.PlayRating
import movie.metropolis.app.screen.Navigation
import movie.metropolis.app.screen.Route
import movie.style.LocalWindowSizeClass
import movie.style.theme.Theme
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val actions = ActivityActions(
        requestPermissions = ::requestPermissions,
        actionView = ::openExternal,
        actionShare = ::share
    )

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            PlayRating()
            Theme {
                CompositionLocalProvider(
                    LocalActivityActions provides actions,
                    LocalWindowSizeClass provides calculateWindowSizeClass(activity = this)
                ) {
                    val controller = rememberNavController()
                    Navigation(controller = controller)
                    LaunchedEffect(Unit) {
                        navigateIfNecessary(controller)
                    }
                }
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
        val uri = getUriForFile(this, "${packageName}.PROVIDER", file, file.name)
        Intent(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_STREAM, uri)
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension))
            .let { Intent.createChooser(it, file.name) }
            .also(::startActivity)
    }

    private fun navigateIfNecessary(controller: NavHostController) {
        val intent = intent.action ?: return
        if (intent != Intent.ACTION_APPLICATION_PREFERENCES) return
        controller.navigate(Route.Settings.destination())
    }

    companion object {

        fun tickets(context: Context) = Intent(context, MainActivity::class.java)
            .setAction(Intent.ACTION_VIEW)
            .setData(Route.Home.deepLink(Route.Tickets.destination()))

    }

}