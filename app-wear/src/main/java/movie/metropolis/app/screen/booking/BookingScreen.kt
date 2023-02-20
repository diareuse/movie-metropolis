package movie.metropolis.app.screen.booking

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import movie.metropolis.app.presentation.onSuccess
import movie.style.AppImage
import movie.style.theme.Theme

@Composable
fun BookingScreen(
    viewModel: BookingViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()
    ScalingLazyColumn(modifier = Modifier.fillMaxSize()) {
        items.onSuccess {
            items(it, key = { it.id }) {
                AppImage(url = it.image)
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true, showBackground = true)
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true, showBackground = true)
@Composable
private fun Preview() = Theme {
    BookingScreen()
}