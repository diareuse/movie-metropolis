package movie.metropolis.app.screen.setup.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.textPlaceholder
import movie.style.theme.Theme
import java.util.Locale
import kotlin.random.Random.Default.nextInt

@Composable
fun RegionItem(
    modifier: Modifier = Modifier,
) {
    RegionItemLayout(modifier = modifier) {
        Text("#".repeat(nextInt(5, 20)), modifier = Modifier.textPlaceholder())
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
                    onClick = onClick ?: {},
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