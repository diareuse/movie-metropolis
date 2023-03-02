package movie.metropolis.app.screen.listing.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.style.layout.PreviewLayout
import movie.style.modifier.surface
import movie.style.theme.Theme

@Composable
fun MovieItemError(modifier: Modifier = Modifier) {
    MovieItemLayout(
        modifier = modifier,
        text = {
            MovieSubText(text = stringResource(R.string.error_movie_sub))
            MovieTitleText(text = stringResource(R.string.error_movie))
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .surface(0.dp, Theme.container.poster),
            contentAlignment = Alignment.Center
        ) {
            Text("😦", style = Theme.textStyle.title.copy(fontSize = 48.sp))
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun MovieItemErrorPreview() = PreviewLayout {
    MovieItemError()
}