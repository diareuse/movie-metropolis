@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen2.purchase

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.metropolis.app.presentation.order.RequestView
import movie.metropolis.app.screen2.purchase.component.WebView
import movie.style.layout.PreviewLayout

@Composable
fun PurchaseScreen(
    request: RequestView?,
    onUrlChanged: (String?) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Scaffold(
    modifier = modifier,
    topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.booking)) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, null)
                }
            }
        )
    }
) { padding ->
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        propagateMinConstraints = true
    ) {
        var progress by remember { mutableIntStateOf(0) }
        AnimatedVisibility(
            visible = progress in 1 until 100 || request == null,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            val modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
            if (progress == 0) LinearProgressIndicator(modifier = modifier)
            else LinearProgressIndicator(
                modifier = modifier,
                progress = progress / 100f
            )
        }
        if (request != null) WebView(
            request = request,
            onProgressChanged = { progress = it },
            onUrlChanged = onUrlChanged,
            onEndReached = onBackClick
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun PurchaseScreenPreview() = PreviewLayout {
    PurchaseScreen(
        request = null,
        onUrlChanged = {},
        onBackClick = {}
    )
}