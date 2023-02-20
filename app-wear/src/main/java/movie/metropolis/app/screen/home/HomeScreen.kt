package movie.metropolis.app.screen.home

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.*
import movie.style.theme.Theme

@Composable
fun HomeScreen() {

}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true, showBackground = true)
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true, showBackground = true)
@Composable
private fun Preview() = Theme {
    HomeScreen()
}