package movie.metropolis.app.screen.listing.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@Composable
fun PosterActionColumn(
    color: Color,
    contentColor: Color,
    onHideClick: () -> Unit,
    onOpenClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Surface(
    modifier = modifier,
    color = color.copy(.8f),
    shape = Theme.container.button,
    contentColor = contentColor
) {
    Column(modifier = Modifier.width(IntrinsicSize.Max)) {
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onHideClick
        ) {
            Icon(painterResource(id = R.drawable.ic_eye_off), null)
            Text(stringResource(R.string.hide))
        }
        HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = contentColor.copy(.5f))
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onOpenClick
        ) {
            Icon(painterResource(id = R.drawable.ic_link), null)
            Text(stringResource(R.string.open_in_browser))
        }
    }
}

@Composable
fun TextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProvideTextStyle(Theme.textStyle.body.copy(fontWeight = FontWeight.Medium)) {
            content()
        }
    }
}

@Preview
@Composable
private fun PosterActionColumnPreview() = PreviewLayout {
    PosterActionColumn(Color.Green, Color.White, {}, {})
}