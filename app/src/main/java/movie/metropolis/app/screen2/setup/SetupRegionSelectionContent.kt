@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package movie.metropolis.app.screen2.setup

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import movie.core.model.Region
import movie.metropolis.app.R
import movie.metropolis.app.model.RegionView
import movie.metropolis.app.model.adapter.RegionViewFromFeature
import movie.metropolis.app.screen2.setup.component.RegionRow
import movie.metropolis.app.screen2.setup.component.SetupPreviewLayout
import movie.metropolis.app.util.rememberVisibleItemAsState
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.rememberPaletteImageState
import movie.style.theme.Theme
import java.util.Locale

@Composable
fun SetupRegionSelectionContent(
    posters: ImmutableList<String>,
    regions: ImmutableList<RegionView>,
    onRegionClick: (RegionView) -> Unit,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier,
    propagateMinConstraints = true
) {
    val state = rememberLazyStaggeredGridState()
    val selectedItem by state.rememberVisibleItemAsState()
    SetupPreviewLayout(
        modifier = Modifier.alpha(.3f),
        count = posters.size,
        contentPadding = PaddingValues(24.dp),
        rowCount = 5,
        selectedItem = selectedItem,
        state = state
    ) {
        val selected = it == selectedItem
        val state = rememberPaletteImageState(url = posters[it])
        val elevation by animateDpAsState(if (selected) 16.dp else 0.dp, tween(700))
        val color = state.palette.color
        Image(
            modifier = Modifier
                .surface(
                    shape = Theme.container.poster,
                    color = Theme.color.container.background,
                    elevation = elevation,
                    shadowColor = color
                )
                .glow(Theme.container.poster),
            state = state
        )
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(24.dp)
    ) {
        items(regions, { it.name }) {
            val state = rememberPaletteImageState(url = it.icon)
            RegionRow(
                modifier = Modifier.animateItemPlacement(),
                icon = { Image(state = state) },
                label = { Text(it.name) },
                onClick = { onRegionClick(it) },
                color = state.palette.color
            )
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            Text(
                text = stringResource(R.string.select_country_description),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun SetupRegionSelectionContentPreview() = PreviewLayout {
    val regions = buildList {
        this += RegionViewFromFeature(Region.Czechia, Locale("cs", "CZ"))
        this += RegionViewFromFeature(Region.Slovakia, Locale("sk", "SK"))
        this += RegionViewFromFeature(Region.Poland, Locale("pl", "PL"))
        this += RegionViewFromFeature(Region.Hungary, Locale("hu", "HU"))
        this += RegionViewFromFeature(Region.Romania, Locale("ro", "RO"))
    }.toImmutableList()
    SetupRegionSelectionContent(
        modifier = Modifier.fillMaxSize(),
        posters = List(10) { "" }.toImmutableList(),
        regions = regions,
        onRegionClick = {})
}