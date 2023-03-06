package movie.metropolis.app.screen.cinema.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.screen.detail.component.ShowingItemTime
import movie.style.layout.PreviewLayout
import movie.style.textPlaceholder

@Composable
fun MovieShowingItemLoading(
    modifier: Modifier = Modifier
) {
    ShowingsLayout(
        title = { Text("#".repeat(23), Modifier.textPlaceholder()) },
        modifier = modifier
    ) {
        ShowingTypeLayout(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .fillMaxWidth(),
            type = {
                Box(
                    Modifier
                        .textPlaceholder()
                        .size(16.dp)
                )
                Text("#".repeat(4), Modifier.textPlaceholder())
            },
            language = {
                Box(
                    Modifier
                        .textPlaceholder()
                        .size(16.dp)
                )
                Text("#".repeat(7), Modifier.textPlaceholder())
            }
        ) {
            items(3) {
                ShowingItemTime {
                    Text("#".repeat(5), Modifier.textPlaceholder())
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun MovieShowingItemLoadingPreview() = PreviewLayout {
    MovieShowingItemLoading()
}
