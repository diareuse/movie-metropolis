package movie.style

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.style.haptic.hapticClick
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
                .clickable(enabled = enabled, onClick = hapticClick { onCheckedChanged(!checked) })
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