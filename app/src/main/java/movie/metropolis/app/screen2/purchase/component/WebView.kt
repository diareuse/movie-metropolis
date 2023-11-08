package movie.metropolis.app.screen2.purchase.component

import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.viewinterop.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import movie.metropolis.app.presentation.order.RequestView
import movie.style.theme.Theme

private val state = Bundle()

@Composable
fun WebView(
    request: RequestView,
    modifier: Modifier = Modifier,
    onProgressChanged: (Int) -> Unit,
    onEndReached: () -> Unit,
    onUrlChanged: (String?) -> Unit
) {
    val backgroundColor = Theme.color.container.background.toArgb()
    val state by remember {
        derivedStateOf {
            if (state.getString("url") != request.url) state.clear()
            state
        }
    }
    var enabled by remember { mutableStateOf(true) }
    val backTasks = remember { Channel<Unit>() }
    val scope = rememberCoroutineScope()
    BackHandler(enabled = enabled) {
        backTasks.trySend(Unit)
    }
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        onUrlChanged(url)
                    }
                }
                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        onProgressChanged(newProgress)
                    }
                }
                settings.apply {
                    javaScriptEnabled = true
                    allowFileAccess = false
                    allowContentAccess = false
                    domStorageEnabled = true
                    databaseEnabled = true
                    offscreenPreRaster = true
                }
                setBackgroundColor(backgroundColor)
                scope.launch {
                    backTasks.consumeEach {
                        enabled = canGoBack()
                        if (enabled) goBack()
                        else onEndReached()
                    }
                }
                if (state.isEmpty) {
                    loadUrl(request.url, request.headers)
                } else {
                    restoreState(state)
                }
            }
        },
        update = {
            if (state.isEmpty) {
                it.loadUrl(request.url, request.headers)
            } else {
                it.restoreState(state)
            }
        },
        onRelease = {
            it.saveState(state)
            state.putString("url", request.url)
        },
        onReset = {
            it.restoreState(state)
        }
    )
}