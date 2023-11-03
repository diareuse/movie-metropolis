package movie.metropolis.app.screen2.setup

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import movie.metropolis.app.model.RegionView
import movie.style.layout.PreviewLayout

@Composable
fun SetupScreen(
    posters: ImmutableList<String>,
    regions: ImmutableList<RegionView>,
    regionSelected: Boolean,
    onSetupComplete: () -> Unit,
    onRegionClick: (RegionView) -> Unit,
    modifier: Modifier = Modifier,
    startWith: SetupState = SetupState.Initial
) = Scaffold(
    modifier = modifier,
    topBar = {},
    bottomBar = {},
    contentWindowInsets = WindowInsets(0)
) { padding ->
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        navController = navController,
        startDestination = startWith.name
    ) {
        composable(SetupState.Initial.name) {
            SetupInitialContent(
                posters = posters,
                onContinueClick = {
                    val builder: NavOptionsBuilder.() -> Unit = {
                        popUpTo(SetupState.Initial.name) {
                            inclusive = true
                        }
                    }
                    when (regionSelected) {
                        true -> navController.navigate(SetupState.Login.name, builder)
                        else -> navController.navigate(SetupState.RegionSelection.name, builder)
                    }
                }
            )
        }
        composable(SetupState.RegionSelection.name) {
            LaunchedEffect(regionSelected) {
                if (regionSelected) navController.navigate(SetupState.Login.name)
            }
            SetupRegionSelectionContent(
                regions = regions,
                posters = posters,
                onRegionClick = onRegionClick
            )
        }
        composable(SetupState.Login.name) {
            SetupLoginContent()
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun SetupScreenPreview() = PreviewLayout {
    SetupScreen(
        regions = persistentListOf<RegionView>().toImmutableList(),
        posters = List(20) { "" }.toImmutableList(),
        regionSelected = false,
        onRegionClick = {},
        onSetupComplete = {}
    )
}