package movie.metropolis.app.screen.listing.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import movie.style.imagePlaceholder
import movie.style.layout.PreviewLayout

@Composable
fun MovieItemLoading(
    modifier: Modifier = Modifier,
) {
    MovieItemLayout(
        modifier = modifier,
        shadowColor = Color.Black
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .imagePlaceholder()
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun MovieItemLoadingPreview() = PreviewLayout {
    MovieItemLoading()
}