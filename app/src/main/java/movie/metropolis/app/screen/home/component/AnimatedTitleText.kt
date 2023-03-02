package movie.metropolis.app.screen.home.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import movie.style.layout.PreviewLayout

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> AnimatedTitleText(
    target: T,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    AnimatedContent(
        modifier = modifier,
        targetState = target,
        transitionSpec = {
            fadeIn() + slideInHorizontally { it } with fadeOut() + slideOutHorizontally { -it }
        }
    ) {
        content(it)
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun AnimatedTitleTextPreview() = PreviewLayout {
    AnimatedTitleText("Foo") {
        Text(it)
    }
}