package movie.metropolis.app.screen.settings.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
fun SettingsItemRow(
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    value: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier
        .heightIn(min = 56.dp)
        .glow(Theme.container.button)
        .padding(16.dp, 12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp)
) {
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.Start
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

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun SettingsItemPreview() = PreviewLayout {
    SettingsItemRow(
        title = { Text("Settings item") },
        description = { Text("Description") },
        value = { Switch(checked = true, onCheckedChange = {}) }
    )
}