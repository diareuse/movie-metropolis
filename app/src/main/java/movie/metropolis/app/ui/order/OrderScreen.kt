package movie.metropolis.app.ui.order

import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.tooling.preview.*
import movie.metropolis.app.presentation.order.RequestView
import movie.metropolis.app.ui.order.component.WebView
import movie.style.layout.PreviewLayout

@OptIn(ExperimentalMaterial3Api::class, ExperimentalStdlibApi::class)
@Composable
fun OrderScreen(
    state: RequestView,
    onBackClick: () -> Unit,
    onUrlChanged: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var title by remember { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    OrderScreenScaffold(
        modifier = modifier,
        title = { Text(title) },
        website = {
            val contentColor = LocalContentColor.current
            WebView(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                url = state.url,
                headers = state.headers,
                onProgressChanged = {},
                onEndReached = onBackClick,
                onUrlChanged = onUrlChanged,
                onTitleChanged = { title = it.orEmpty() },
                contentPadding = it,
                onPageVisible = {
                    val js = """
                    var ss = document.createElement("style");
                    ss.textContent = `table.ticketTable td { color: #${
                        (contentColor.toArgb().shl(8).or(0xFF)).toHexString()
                    } !important;} .orderForm .payuoptions .payuOpt { background-color: transparent !important; } .total-block { color: unset !important }`;
                    document.head.appendChild(ss);
                            """.trimIndent().replace("\n", "")
                    evaluateJavascript(js, null)
                },
                connection = scrollBehavior.nestedScrollConnection
            )
        },
        navigationIcon = {
            IconButton(onBackClick) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, null)
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun OrderScreenPreview() = PreviewLayout {
    OrderScreen(
        state = RequestView(),
        onBackClick = {},
        onUrlChanged = {}
    )
}