package movie.metropolis.app.screen2.profile.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.res.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.style.layout.PreviewLayout
import movie.style.modifier.glow
import movie.style.theme.Theme

@Composable
fun ProfileItem(
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) = Row(
    modifier = modifier
        .clip(Theme.container.button)
        .glow(Theme.container.button, alpha = .1f)
        .clickable(role = Role.Button, onClick = onClick, enabled = enabled)
        .padding(horizontal = 16.dp, vertical = 12.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    icon()
    Box(modifier = Modifier.weight(1f)) {
        ProvideTextStyle(Theme.textStyle.body.copy(fontWeight = FontWeight.Medium)) {
            title()
        }
    }
    Icon(painterResource(R.drawable.ic_forward), null)
}

@Preview
@Composable
private fun ProfileItemPreview() = PreviewLayout {
    ProfileItem(
        icon = { Icon(Icons.Rounded.Settings, null) },
        title = { Text("Settings") },
        onClick = {}
    )
}