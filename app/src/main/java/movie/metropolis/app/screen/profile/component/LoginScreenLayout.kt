package movie.metropolis.app.screen.profile.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
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