package movie.metropolis.app.screen.setup.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.modifier.LightSource
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.rememberPaletteImageState
import movie.style.theme.Theme

@Composable
fun RegionRow(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    color: Color = Theme.color.container.background
) {
    val clickable = when {
        onClick != null -> Modifier.clickable(onClick = onClick, role = Role.Button)
        else -> Modifier
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(clickable)
            .surface(
                shape = Theme.container.button,
                color = Theme.color.container.background,
                elevation = 8.dp,
                shadowColor = color
            )
            .glow(Theme.container.button)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .height(32.dp)
                .aspectRatio(1.5f)
                .surface(
                    shape = Theme.container.buttonSmall,
                    color = Theme.color.container.background,
                    elevation = 8.dp,
                    shadowColor = color
                )
                .glow(Theme.container.buttonSmall, lightSource = LightSource.BottomRight),
            propagateMinConstraints = true
        ) {
            icon()
        }
        ProvideTextStyle(Theme.textStyle.emphasis) {
            label()
        }
    }
}

@Preview
@Composable
private fun RegionRowPreview() = PreviewLayout(modifier = Modifier.padding(16.dp)) {
    val state = rememberPaletteImageState(R.drawable.ic_czechia)
    RegionRow(
        icon = { Image(state) },
        label = { Text("Česko") }
    )
}