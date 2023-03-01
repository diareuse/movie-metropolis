package movie.metropolis.app.screen.cinema.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import movie.style.imagePlaceholder
import movie.style.layout.PreviewLayout
import movie.style.textPlaceholder

@Composable
fun CinemaItemLoading(
    modifier: Modifier = Modifier,
) {
    CinemaLayout(
        modifier = modifier,
        image = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imagePlaceholder(true)
            )
        },
        name = { Text("#".repeat(17), modifier = Modifier.textPlaceholder(true)) },
        address = { Text("#".repeat(22), modifier = Modifier.textPlaceholder(true)) },
        city = { Text("#".repeat(15), modifier = Modifier.textPlaceholder(true)) },
        distance = { Text("#".repeat(5), modifier = Modifier.textPlaceholder(true)) }
    )
}

@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun CinemaItemLoadingPreview() = PreviewLayout {
    CinemaItemLoading()
}
