package movie.metropolis.app.screen2.home.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@Composable
fun HomeToolbar(
    icon: @Composable () -> Unit,
    name: @Composable () -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier
        .statusBarsPadding()
        .padding(horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    Box(
        modifier = Modifier.minimumInteractiveComponentSize(),
        propagateMinConstraints = true
    ) {
        icon()
    }
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(-(4).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProvideTextStyle(Theme.textStyle.title) {
            name()
        }
        ProvideTextStyle(Theme.textStyle.caption) {
            title()
        }
    }
    Spacer(modifier = Modifier.minimumInteractiveComponentSize())
}

@Composable
fun HomeToolbar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier
        .statusBarsPadding()
        .padding(horizontal = 24.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    Spacer(modifier = Modifier.minimumInteractiveComponentSize())
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProvideTextStyle(Theme.textStyle.title) {
            title()
        }
    }
    Spacer(modifier = Modifier.minimumInteractiveComponentSize())
}

@Preview(showBackground = true)
@Composable
private fun HomeToolbarPreview() = PreviewLayout {
    HomeToolbar(
        icon = { Icon(Icons.Rounded.Person, null) },
        name = { Text("Hi John!") },
        title = { Text("Your tickets") }
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeToolbarEmptyPreview() = PreviewLayout {
    HomeToolbar(
        title = { Text("Your tickets") }
    )
}