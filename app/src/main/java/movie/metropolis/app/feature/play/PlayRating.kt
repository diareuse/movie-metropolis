package movie.metropolis.app.feature.play

import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.delay
import movie.style.util.findActivity
import kotlin.time.Duration.Companion.minutes

@Composable
fun PlayRating() {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val manager = remember(context) { ReviewManagerFactory.create(context) }
    LaunchedEffect(manager) {
        delay(3.minutes)
        manager.runCatching { requestReview() }
            .onSuccess { review ->
                manager.launchReview(activity, review)
            }
    }
}