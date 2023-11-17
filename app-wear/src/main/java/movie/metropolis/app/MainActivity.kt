package movie.metropolis.app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.os.postDelayed
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import movie.metropolis.app.screen.Navigation
import movie.style.theme.Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        var isReady = false
        Handler(Looper.getMainLooper()).postDelayed(500) { isReady = true }
        val content = findViewById<View>(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw() = when {
                    isReady -> {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    }

                    else -> false
                }
            }
        )
        setContent {
            Theme(darkTheme = true) {
                Navigation()
            }
        }
    }
}
