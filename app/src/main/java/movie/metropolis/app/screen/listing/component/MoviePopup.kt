package movie.metropolis.app.screen.listing.component

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import movie.style.Image
import movie.style.rememberImageState
import movie.style.theme.Theme

@Composable
private fun MoviePopup(
    url: String,
    year: String,
    director: String,
    name: String,
    modifier: Modifier = Modifier,
    aspectRatio: Float = DefaultPosterAspectRatio,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .navigationBarsPadding()
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(rememberImageState(url = url))
        Spacer(Modifier.height(24.dp))
        CompositionLocalProvider(
            LocalContentColor provides Color.White
        ) {
            MovieSubText(text = "%s • %s".format(year, director))
            MovieTitleText(text = name)
        }
    }
}

@Composable
fun MoviePopup(
    isVisible: Boolean,
    onVisibilityChanged: (Boolean) -> Unit,
    url: String,
    year: String,
    director: String,
    name: String,
    aspectRatio: Float = DefaultPosterAspectRatio,
) {
    if (isVisible) Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        ),
        onDismissRequest = { onVisibilityChanged(false) }
    ) {
        MoviePopup(url, year, director, name, aspectRatio = aspectRatio)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        MoviePopup(
            modifier = Modifier.background(Color.Black.copy(alpha = .5f)),
            url = "https://images.unsplash.com/photo-1674707488760-4ec87e969368?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=3864&q=80",
            year = "2022",
            director = "Movie Director",
            name = "Foo bar"
        )
    }
}

fun Modifier.detectLongPress(onStateChanged: (Boolean) -> Unit) = composed {
    val density = LocalDensity.current
    pointerInput(density) {
        coroutineScope {
            val pressScope = PressGestureScopeImpl(density)
            awaitEachGesture {
                var isHooked = false
                awaitFirstDown()
                launch {
                    pressScope.reset()
                }
                launch {
                    delay(500)
                    onStateChanged(true)
                    isHooked = true
                    pressScope.tryAwaitRelease()
                    onStateChanged(false)
                }

                val change = waitForUpOrCancellation()
                when (change) {
                    null -> pressScope.cancel()
                    else -> pressScope.release()
                }
                if (isHooked) change?.consume()
            }
        }
    }
}

class PressGestureScopeImpl(
    density: Density
) : PressGestureScope, Density by density {
    private var isReleased = false
    private var isCanceled = false
    private val mutex = Mutex(locked = false)

    /**
     * Called when a gesture has been canceled.
     */
    fun cancel() {
        isCanceled = true
        try {
            mutex.unlock()
        } catch (ignore: IllegalStateException) {
        }
    }

    /**
     * Called when all pointers are up.
     */
    fun release() {
        isReleased = true
        try {
            mutex.unlock()
        } catch (ignore: IllegalStateException) {
        }
    }

    /**
     * Called when a new gesture has started.
     */
    suspend fun reset() {
        mutex.lock()
        isReleased = false
        isCanceled = false
    }

    override suspend fun awaitRelease() {
        if (!tryAwaitRelease()) {
            throw GestureCancellationException("The press gesture was canceled.")
        }
    }

    override suspend fun tryAwaitRelease(): Boolean {
        if (!isReleased && !isCanceled) {
            mutex.lock()
            mutex.unlock()
        }
        return isReleased
    }
}