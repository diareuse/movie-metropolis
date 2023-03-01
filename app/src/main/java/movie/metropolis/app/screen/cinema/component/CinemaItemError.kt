package movie.metropolis.app.screen.cinema.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import movie.metropolis.app.R
import movie.style.AppErrorItem
import movie.style.layout.PreviewLayout

@Composable
fun CinemaItemError(
    modifier: Modifier = Modifier,
) {
    AppErrorItem(
        modifier = modifier
    ) {
        Text(stringResource(R.string.error_cinemas))
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun CinemaItemErrorPreview() = PreviewLayout {

}
