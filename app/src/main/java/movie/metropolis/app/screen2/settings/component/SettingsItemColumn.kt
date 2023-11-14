package movie.metropolis.app.screen2.settings.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.modifier.glow
import movie.style.theme.Theme

@Composable
fun SettingsItemColumn(
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    value: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier.glow(Theme.container.button)
) {
    Column(
        modifier = Modifier.padding(16.dp, 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        ProvideTextStyle(Theme.textStyle.body.copy(fontWeight = FontWeight.Medium)) {
            title()
        }
        ProvideTextStyle(Theme.textStyle.caption) {
            CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(.5f)) {
                description()
            }
        }
    }
    value()
}

@Preview(showBackground = true)
@Composable
private fun SettingsItemColumnPreview() = PreviewLayout {
    SettingsItemColumn(
        title = { Text("Settings item") },
        description = { Text("Description") },
        value = { TextField(value = "", onValueChange = {}) }
    )
}