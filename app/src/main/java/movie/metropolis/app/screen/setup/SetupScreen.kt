package movie.metropolis.app.screen.setup

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.metropolis.app.model.RegionView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.screen.setup.component.Background
import movie.metropolis.app.screen.setup.component.RegionItem
import movie.style.AppToolbar
import movie.style.theme.Theme

@Composable
fun SetupScreen(
    onNavigateHome: () -> Unit,
    viewModel: SetupViewModel
) {
    val requiresSetup by viewModel.requiresSetup.collectAsState()
    val regions by viewModel.regions.collectAsState()
    LaunchedEffect(requiresSetup) {
        if (!requiresSetup) onNavigateHome()
    }
    SetupScreen(
        regions = regions,
        onClickRegion = viewModel::select
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun SetupScreen(
    regions: Loadable<List<RegionView>>,
    onClickRegion: (RegionView) -> Unit
) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = { Text(stringResource(R.string.select_country)) }
            )
        }
    ) { padding ->
        Background(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(.3f),
                painter = painterResource(id = R.drawable.ic_language),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Theme.color.container.primary)
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding + PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            regions.onLoading {
                items(5) {
                    RegionItem(modifier = Modifier.animateItemPlacement())
                }
            }.onSuccess { items ->
                items(items, key = RegionView::name) {
                    RegionItem(
                        modifier = Modifier.animateItemPlacement(),
                        text = it.name,
                        onClick = { onClickRegion(it) }
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.select_country_description),
                    style = Theme.textStyle.caption,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        SetupScreen(
            regions = Loadable.loading(),
            onClickRegion = {}
        )
    }
}