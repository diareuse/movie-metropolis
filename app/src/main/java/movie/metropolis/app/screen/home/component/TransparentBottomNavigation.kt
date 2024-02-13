package movie.metropolis.app.screen.home.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout

@Composable
fun TransparentBottomNavigation(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) = Row(
    modifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 12.dp),
    horizontalArrangement = Arrangement.SpaceAround,
    verticalAlignment = Alignment.CenterVertically
) {
    content()
}

@Preview(showBackground = true)
@Composable
private fun TransparentBottomNavigationPreview() = PreviewLayout {
    var selected by remember { mutableIntStateOf(0) }
    TransparentBottomNavigation {
        TransparentBottomNavigationItem(
            selected = selected == 0,
            active = { Icon(Icons.Filled.Home, null) },
            inactive = { Icon(Icons.Outlined.Home, null) },
            onClick = { selected = 0 }
        )
        TransparentBottomNavigationItem(
            selected = selected == 1,
            active = { Icon(Icons.Filled.Person, null) },
            inactive = { Icon(Icons.Outlined.Person, null) },
            onClick = { selected = 1 }
        )
        TransparentBottomNavigationItem(
            selected = selected == 2,
            active = { Icon(Icons.Filled.Build, null) },
            inactive = { Icon(Icons.Outlined.Build, null) },
            onClick = { selected = 2 }
        )
        TransparentBottomNavigationItem(
            selected = selected == 3,
            active = { Icon(Icons.Filled.Settings, null) },
            inactive = { Icon(Icons.Outlined.Settings, null) },
            onClick = { selected = 3 }
        )
    }
}