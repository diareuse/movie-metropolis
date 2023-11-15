package movie.metropolis.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import movie.metropolis.app.feature.play.PlayRating
import movie.metropolis.app.screen.Navigation
import movie.metropolis.app.screen.Route
import movie.metropolis.app.screen.home.HomeState
import movie.style.LocalWindowSizeClass
import movie.style.theme.Theme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            PlayRating()
            Theme {
                Surface {
                    CompositionLocalProvider(
                        LocalWindowSizeClass provides calculateWindowSizeClass(activity = this)
                    ) {
                        val controller = rememberNavController()
                        Navigation(controller)
                        LaunchedEffect(Unit) {
                            navigateIfNecessary(controller)
                        }
                    }
                }
            }
        }
    }

    private fun navigateIfNecessary(controller: NavHostController) {
        val intent = intent.action ?: return
        if (intent != Intent.ACTION_APPLICATION_PREFERENCES) return
        controller.navigate(Route.Settings())
    }

    companion object {

        fun tickets(context: Context) = Intent(context, MainActivity::class.java)
            .setAction(Intent.ACTION_VIEW)
            .setData(Route.Home.deepLink(HomeState.Tickets))

    }

}