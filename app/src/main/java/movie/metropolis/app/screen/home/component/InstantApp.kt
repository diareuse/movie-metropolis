package movie.metropolis.app.screen.home.component

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import com.google.android.gms.instantapps.InstantApps
import movie.style.util.findActivity

@Composable
fun rememberInstantApp(
    context: Context = LocalContext.current
): InstantApp {
    return remember(context) {
        val instant = InstantApps.getPackageManagerCompat(context).isInstantApp
        InstantApp(instant, context)
    }
}

data class InstantApp(
    val isInstant: Boolean,
    private val context: Context
) {

    fun install() {
        val activity = context.findActivity()
        val postInstall = Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_DEFAULT)
            .setPackage("movie.metropolis.app")
        InstantApps.showInstallPrompt(activity, postInstall, 100, null)
    }

}