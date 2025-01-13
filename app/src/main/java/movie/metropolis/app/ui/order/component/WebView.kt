package movie.metropolis.app.ui.order.component

import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme
import movie.style.theme.contentColorFor

private val state = Bundle()

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun WebView(
    url: String,
    headers: Map<String, String>,
    onProgressChanged: (Int) -> Unit,
    onEndReached: () -> Unit,
    onUrlChanged: (String?) -> Unit,
    onTitleChanged: (String?) -> Unit,
    modifier: Modifier = Modifier,
    onPageVisible: WebView.() -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(),
    connection: NestedScrollConnection? = null
) {
    val backgroundColor = Theme.color.container.background
    val contentColor = Theme.color.contentColorFor(backgroundColor)
    val state by remember {
        derivedStateOf {
            if (state.getString("url") != url) state.clear()
            state
        }
    }
    var enabled by remember { mutableStateOf(true) }
    val backTasks = remember { Channel<Unit>() }
    val scope = rememberCoroutineScope()
    val ld = LocalLayoutDirection.current
    val density = LocalDensity.current
    BackHandler(enabled = enabled) {
        backTasks.trySend(Unit)
    }
    val color = MaterialTheme.colorScheme.background
    AndroidView(
        modifier = modifier
            .fillMaxSize(),
        factory = {
            object : WebView(it) {
                override fun onScrollChanged(
                    scrollX: Int,
                    scrollY: Int,
                    oldScrollX: Int,
                    oldScrollY: Int
                ) {
                    super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY)
                    val o =
                        Offset(
                            (oldScrollX - scrollX).toFloat(),
                            (oldScrollY - scrollY).toFloat()
                        ) * 0.5f
                    val c = connection?.onPreScroll(o, NestedScrollSource.UserInput) ?: return
                    connection.onPostScroll(
                        c,
                        o - c,
                        NestedScrollSource.UserInput
                    )
                }
            }.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(color.toArgb())
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        onUrlChanged(url)
                    }

                    fun Dp.toPx(): String = with(density) { "${value}px" }
                    override fun onPageCommitVisible(view: WebView?, url: String?) {
                        val padding = listOf(
                            contentPadding.calculateTopPadding(),
                            contentPadding.calculateRightPadding(ld),
                            contentPadding.calculateBottomPadding(),
                            contentPadding.calculateLeftPadding(ld)
                        ).joinToString(separator = " ") { it.toPx() }
                        val javascript = listOf(
                            "document.body.style.padding = '$padding'",
                            "document.body.style.background = '#${
                                (backgroundColor.toArgb().shl(8).or(0xFF)).toHexString()
                            }'",
                            "document.body.style.color = '#${
                                (contentColor.toArgb().shl(8).or(0xFF)).toHexString()
                            }'"
                        )
                        for (js in javascript) {
                            evaluateJavascript(js, null)
                        }
                        onPageVisible()
                    }
                }
                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        onProgressChanged(newProgress)
                    }

                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        onTitleChanged(title)
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
                setBackgroundColor(backgroundColor.toArgb())
                scope.launch {
                    backTasks.consumeEach {
                        enabled = canGoBack()
                        if (enabled) goBack()
                        else onEndReached()
                    }
                }
                if (state.isEmpty) {
                    loadUrl(url, headers)
                } else {
                    restoreState(state)
                }
            }
        },
        update = {
            if (connection != null) it.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                connection.onPostScroll(
                    Offset.Zero,
                    Offset((oldScrollX - scrollX).toFloat(), (oldScrollY - scrollY).toFloat()),
                    NestedScrollSource.UserInput
                )
            }
            if (state.isEmpty) {
                it.loadUrl(url, headers)
            } else {
                it.restoreState(state)
            }
        },
        onRelease = {
            it.saveState(state)
            state.putString("url", url)
        },
        onReset = {
            it.restoreState(state)
        }
    )
}

@Preview
@Composable
private fun WebViewPreview() = PreviewLayout {
    WebView(
        "https://google.com",
        emptyMap(),
        {},
        {},
        {},
        {},
        contentPadding = PaddingValues(16.dp, 32.dp)
    )
}