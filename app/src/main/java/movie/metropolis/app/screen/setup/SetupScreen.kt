package movie.metropolis.app.screen.setup

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import movie.style.layout.PreviewLayout

@Composable
fun SetupScreen(
    regionSelected: Boolean,
    modifier: Modifier = Modifier,
    startWith: SetupState = SetupState.Initial,
    initial: @Composable () -> Unit,
    region: @Composable () -> Unit,
    login: @Composable () -> Unit,
    navController: NavHostController = rememberNavController()
) = Scaffold(
    modifier = modifier,
    topBar = {},
    bottomBar = {},
    contentWindowInsets = WindowInsets(0)
) { padding ->
    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        navController = navController,
        startDestination = startWith.name
    ) {
        composable(SetupState.Initial.name) {
            initial()
        }
        composable(SetupState.RegionSelection.name) {
            LaunchedEffect(regionSelected) {
                if (regionSelected) navController.navigate(SetupState.Login.name)
            }
            region()
        }
        composable(SetupState.Login.name) {
            login()
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun SetupScreenPreview() = PreviewLayout {
    SetupScreen(
        regionSelected = false,
        initial = {},
        region = {},
        login = {}
    )
}