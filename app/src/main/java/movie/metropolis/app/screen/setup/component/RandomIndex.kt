package movie.metropolis.app.screen.setup.component

import androidx.compose.runtime.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun <T> ImmutableList<T>.rememberRandomItemAsState(delay: Duration = 5.seconds): State<T> {
    val state = remember { mutableStateOf(random()) }
    LaunchedEffect(this) {
        while (true) {
            delay(delay)
            state.value = random()
        }
    }
    return state
}