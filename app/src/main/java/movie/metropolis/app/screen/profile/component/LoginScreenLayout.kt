package movie.metropolis.app.screen.profile.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.*
import movie.metropolis.app.R
import movie.style.AppIconButton
import movie.style.AppToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenLayout(
    title: @Composable () -> Unit,
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = title,
                navigationIcon = {
                    AppIconButton(
                        onClick = onBackClick,
                        painter = painterResource(id = R.drawable.ic_back)
                    )
                }
            )
        },
        content = content
    )
}