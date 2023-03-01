package movie.metropolis.app.screen.cinema.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.style.layout.EmptyShapeLayout
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@Composable
fun MovieShowingItemEmpty(
    modifier: Modifier = Modifier
) {
    EmptyShapeLayout(
        modifier = modifier,
        contentPadding = PaddingValues(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("🙄", style = Theme.textStyle.title.copy(fontSize = 48.sp))
            Text(stringResource(R.string.whoops), style = Theme.textStyle.title)
            Text(
                stringResource(R.string.error_showings),
                style = Theme.textStyle.emphasis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun MovieShowingItemEmptyPreview() = PreviewLayout {
    MovieShowingItemEmpty()
}
