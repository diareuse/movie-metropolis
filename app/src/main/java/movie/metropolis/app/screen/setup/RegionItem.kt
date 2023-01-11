package movie.metropolis.app.screen.setup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.style.haptic.withHaptics
import movie.style.textPlaceholder
import movie.style.theme.Theme
import java.util.Locale
import kotlin.random.Random.Default.nextInt

@Composable
fun RegionItem(
    modifier: Modifier = Modifier,
) {
    RegionItemLayout(modifier = modifier) {
        Text("#".repeat(nextInt(5, 20)), modifier = Modifier.textPlaceholder(true))
    }
}

@Composable
fun RegionItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    RegionItemLayout(modifier = modifier, onClick = onClick) {
        Text(text)
    }
}

@Composable
private fun RegionItemLayout(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    name: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = Theme.color.container.background,
        contentColor = Theme.color.content.background,
        shape = Theme.container.button,
        tonalElevation = 4.dp
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onClick?.withHaptics() ?: {},
                    enabled = onClick != null,
                    role = Role.Button
                )
                .padding(24.dp, 16.dp)
        ) {
            ProvideTextStyle(Theme.textStyle.emphasis) {
                name()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        RegionItem(Locale("hu", "HU").displayCountry, {})
    }
}