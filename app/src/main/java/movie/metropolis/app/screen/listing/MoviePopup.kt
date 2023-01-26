package movie.metropolis.app.screen.listing

import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import movie.style.AppImage
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
        AppImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .clip(Theme.container.card),
            url = url
        )
        Spacer(Modifier.height(24.dp))
        MovieSubText(text = "%s • %s".format(year, director))
        MovieTitleText(text = name)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MoviePopup(
    isVisible: Boolean,
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
        onDismissRequest = {}
    ) {
        MoviePopup(url, year, director, name, aspectRatio = aspectRatio)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        MoviePopup(
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
                    delay(200)
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
        mutex.unlock()
    }

    /**
     * Called when all pointers are up.
     */
    fun release() {
        isReleased = true
        mutex.unlock()
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