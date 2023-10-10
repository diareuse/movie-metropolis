package movie.style

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.theme.Theme

@Composable
fun AppSettings(
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
) {
    Surface(
        modifier = modifier,
        color = Theme.color.container.background,
        contentColor = Theme.color.content.background,
        tonalElevation = 2.dp,
        shape = Theme.container.button
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled, onClick = { onCheckedChanged(!checked) })
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CompositionLocalProvider(
                LocalContentColor provides Theme.color.container.primary
            ) {
                if (icon != null) icon()
            }
            Column(modifier = Modifier.weight(1f)) {
                ProvideTextStyle(Theme.textStyle.body.copy(fontWeight = FontWeight.Bold)) {
                    title()
                }
                ProvideTextStyle(Theme.textStyle.caption) {
                    description()
                }
            }
            Switch(checked = checked, onCheckedChange = onCheckedChanged, enabled = enabled)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        AppSettings(
            checked = true,
            onCheckedChanged = {},
            title = { Text("Preference") },
            description = { Text("Preference description") }
        )
    }
}