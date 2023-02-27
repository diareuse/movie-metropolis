package movie.metropolis.app.screen.listing.component

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import movie.style.textPlaceholder
import movie.style.theme.Theme

@Composable
fun MovieSubText(
    text: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Text(
        modifier = modifier.textPlaceholder(isLoading),
        text = text,
        style = Theme.textStyle.caption
    )
}

@Composable
fun MovieTitleText(
    text: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    maxLines: Int = 2
) {
    Text(
        modifier = modifier.textPlaceholder(visible = isLoading),
        text = text,
        style = Theme.textStyle.body,
        fontWeight = FontWeight.Bold,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}