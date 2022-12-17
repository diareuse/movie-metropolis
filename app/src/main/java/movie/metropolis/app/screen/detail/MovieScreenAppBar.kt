package movie.metropolis.app.screen.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.metropolis.app.R
import movie.metropolis.app.theme.Theme

@Composable
fun MovieScreenAppBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable (RowScope.() -> Unit) = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(24.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            shadowElevation = 16.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(painterResource(id = R.drawable.ic_back), null)
            }
        }
        Spacer(Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
    }
}

@Composable
fun FavoriteButton(
    isChecked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        val painter = when (isChecked) {
            true -> painterResource(id = R.drawable.ic_favorite_checked)
            else -> painterResource(id = R.drawable.ic_favorite_unchecked)
        }
        val tint = when (isChecked) {
            true -> MaterialTheme.colorScheme.tertiary
            else -> MaterialTheme.colorScheme.onSurface
        }
        Icon(painter = painter, contentDescription = null, tint = tint)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        MovieScreenAppBar(
            onBackClick = {}
        )
    }
}