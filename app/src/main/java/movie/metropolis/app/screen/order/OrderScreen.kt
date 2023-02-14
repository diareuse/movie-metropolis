package movie.metropolis.app.screen.order

import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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
    onBackClick: () -> Unit
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
        // todo add navigation to support screen
        // todo add invalidation
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    progress = progress / 100f
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

@Composable
fun WebView(
    request: RequestView,
    modifier: Modifier = Modifier,
    onProgressChanged: (Int) -> Unit,
    onUrlChanged: (String?) -> Unit
) {
    val backPress = LocalOnBackPressedDispatcherOwner.current
    val backgroundColor = Theme.color.container.background.toArgb()
    var callback by remember { mutableStateOf(null as OnBackPressedCallback?) }
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
                loadUrl(request.url, request.headers)
            }
        },
        update = {
            it.loadUrl(request.url, request.headers)
        }
    )
}