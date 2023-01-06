package movie.metropolis.app.screen.order

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import movie.metropolis.app.R
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.onLoading
import movie.metropolis.app.screen.onSuccess
import movie.style.AppIconButton
import movie.style.AppToolbar
import movie.style.theme.Theme

@Composable
fun OrderScreen(
    viewModel: OrderViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val request by viewModel.request.collectAsState()
    OrderScreen(
        request = request,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    request: Loadable<RequestView>,
    onBackClick: () -> Unit
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
        request.onSuccess { request ->
            WebView(request = request, modifier = Modifier.padding(padding))
        }.onLoading {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun WebView(
    request: RequestView,
    modifier: Modifier = Modifier,
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
                webViewClient = WebViewClient()
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