package movie.metropolis.app.ui.setup.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.util.pc

@Composable
fun CountryRow(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) = Row(
    modifier = modifier
        .run { if (selected) border(2.dp, LocalContentColor.current) else this }
        .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
        .padding(1.pc),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(1.pc)
) {
    Box(
        modifier = Modifier
            .width(48.dp)
            .aspectRatio(4 / 3f),
        propagateMinConstraints = true
    ) {
        icon()
    }
    label()
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun CountryRowPreview() = PreviewLayout {
    CountryRow(true, {}, { Box(Modifier.background(Color.Blue)) }, { Text("Czechia") })
}