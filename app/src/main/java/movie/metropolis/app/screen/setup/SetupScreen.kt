package movie.metropolis.app.screen.setup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.metropolis.app.R
import movie.metropolis.app.model.RegionView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.screen.onLoading
import movie.metropolis.app.screen.onSuccess
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