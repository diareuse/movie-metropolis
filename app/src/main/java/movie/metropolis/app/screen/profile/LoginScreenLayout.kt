package movie.metropolis.app.screen.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import movie.metropolis.app.R
import movie.style.haptic.withHaptics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenLayout(
    title: @Composable () -> Unit,
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = title,
                navigationIcon = {
                    IconButton(onBackClick.withHaptics()) {
                        Icon(painterResource(id = R.drawable.ic_back), null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        content = content
    )
}