package movie.metropolis.app.screen.detail.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.metropolis.app.R
import movie.style.AppIconButton
import movie.style.theme.Theme

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
            color = Theme.color.container.background,
            contentColor = Theme.color.content.background,
            tonalElevation = 1.dp,
            shadowElevation = 16.dp,
            shape = Theme.container.button
        ) {
            AppIconButton(
                onClick = onBackClick,
                painter = painterResource(id = R.drawable.ic_back)
            )
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
    tintChecked: Color = Theme.color.content.tertiary,
    tint: Color = LocalContentColor.current
) {
    val painter = when (isChecked) {
        true -> painterResource(id = R.drawable.ic_favorite_checked)
        else -> painterResource(id = R.drawable.ic_favorite_unchecked)
    }
    val color = when (isChecked) {
        true -> tintChecked
        else -> tint
    }
    AppIconButton(
        modifier = modifier,
        onClick = onClick,
        painter = painter,
        color = color
    )
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