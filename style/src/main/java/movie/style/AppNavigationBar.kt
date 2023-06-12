package movie.style

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import movie.style.theme.Theme

@Composable
fun AppNavigationBar(
    modifier: Modifier = Modifier,
    size: WindowSizeClass = LocalWindowSizeClass.current,
    content: @Composable () -> Unit,
) {
    when (size.widthSizeClass) {
        WindowWidthSizeClass.Compact -> AppNavigationBarCompact(
            modifier = modifier,
            content = content
        )

        else -> AppNavigationBarMedium(
            modifier = modifier,
            content = content
        )
    }
}

@Composable
private fun AppNavigationBarCompact(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = Theme.container.card.copy(
            bottomEnd = CornerSize(0),
            bottomStart = CornerSize(0)
        ),
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            content()
        }
    }
}

@Composable
private fun AppNavigationBarMedium(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = Theme.container.card.copy(
                topStart = CornerSize(0),
                bottomStart = CornerSize(0)
            ),
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    }
}