package movie.metropolis.app.screen2.settings.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@Composable
fun SettingsSection(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally),
        propagateMinConstraints = true
    ) {
        ProvideTextStyle(
            Theme.textStyle.emphasis.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        ) {
            title()
        }
    }
    content()
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun SettingsSectionPreview() = PreviewLayout {
    SettingsSection(
        title = { Text("Fields") }
    ) {
        SettingsItemRow(
            title = { Text("Item") },
            description = { Text("Item description") },
            value = { Checkbox(checked = true, onCheckedChange = {}) }
        )
    }
}