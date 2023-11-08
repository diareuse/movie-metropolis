package movie.metropolis.app.screen.order

import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.*
import androidx.hilt.navigation.compose.hiltViewModel
import movie.metropolis.app.R
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onFailure
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.presentation.order.RequestView
import movie.style.AppIconButton
import movie.style.AppToolbar
import movie.style.theme.Theme

@Composable
fun OrderScreen(
    viewModel: OrderViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onCompleted: () -> Unit
) {
    val request by viewModel.request.collectAsState()
    val isCompleted by viewModel.isCompleted.collectAsState()
    OrderScreen(
        request = request,
        onBackClick = onBackClick,
        onUrlChanged = viewModel::updateUrl
    )
    LaunchedEffect(isCompleted) {
        if (!isCompleted) return@LaunchedEffect
        onCompleted()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    request: Loadable<RequestView>,
    onBackClick: () -> Unit,
    onUrlChanged: (String?) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppToolbar(
                title = { Text(stringResource(R.string.booking)) },
                navigationIcon = {
                    AppIconButton(
                        onClick = onBackClick,
                        painter = painterResource(id = R.drawable.ic_back)
                    )
                }
            )
        }
    ) { padding ->
        Box {
            var progress by remember { mutableStateOf(0) }
            AnimatedVisibility(
                modifier = Modifier.padding(padding),
                visible = progress in 1 until 100,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                LinearProgressIndicator(
                    progress = {
                        progress / 100f
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                )
            }
            request.onSuccess { request ->
                WebView(
                    request = request,
                    modifier = Modifier.padding(padding),
                    onProgressChanged = { progress = it },
                    onUrlChanged = onUrlChanged
                )
            }.onLoading {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }.onFailure {
                LaunchedEffect(Unit) {
                    onBackClick()
                }
            }
        }
    }
}

private val state = Bundle()

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WebView(
    request: RequestView,
    onProgressChanged: (Int) -> Unit,
    onUrlChanged: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val backPress = LocalOnBackPressedDispatcherOwner.current
    val backgroundColor = Theme.color.container.background.toArgb()
    var callback by remember { mutableStateOf(null as OnBackPressedCallback?) }
    val state by remember {
        derivedStateOf {
            if (state.getString("url") != request.url) state.clear()
            state
        }
    }
    DisposableEffect(callback) {
        onDispose {
            callback?.isEnabled = false
            callback?.remove()
        }
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
                callback = backPress?.onBackPressedDispatcher?.addCallback(enabled = true) {
                    isEnabled = canGoBack()
                    if (isEnabled) goBack()
                    else {
                        remove()
                        backPress.onBackPressedDispatcher.onBackPressed()
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