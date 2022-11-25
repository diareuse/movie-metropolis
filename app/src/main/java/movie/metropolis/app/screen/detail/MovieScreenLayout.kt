package movie.metropolis.app.screen.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreenLayout(
    appbar: @Composable (TopAppBarScrollBehavior) -> Unit,
    background: @Composable () -> Unit,
    content: @Composable (TopAppBarScrollBehavior, PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = { appbar(scrollBehavior) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            val surfaceColor = MaterialTheme.colorScheme.surface
            val gradientColors = listOf(Color.Transparent, surfaceColor)
            val brush = Brush.verticalGradient(gradientColors)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .blur(16.dp)
                    .alpha(.3f)
                    .drawWithContent {
                        drawContent()
                        drawRect(brush, size = size)
                    }
            ) {
                background()
            }
            content(scrollBehavior, padding)
        }
    }
}