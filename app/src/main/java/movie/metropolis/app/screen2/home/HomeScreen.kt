package movie.metropolis.app.screen2.home

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import movie.metropolis.app.screen2.home.component.TransparentBottomNavigation
import movie.metropolis.app.screen2.home.component.TransparentBottomNavigationItem
import movie.style.layout.PreviewLayout

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    val route = entry?.destination?.route
    Scaffold(
        modifier = modifier,
        topBar = {},
        bottomBar = {
            TransparentBottomNavigation {
                for (state in HomeState.entries)
                    TransparentBottomNavigationItem(
                        selected = route == state.name,
                        active = { Icon(painterResource(state.active), null) },
                        inactive = { Icon(painterResource(state.inactive), null) },
                        onClick = {
                            while (navController.navigateUp()) {
                                /* no-op */
                            }
                            navController.navigate(state.name)
                        }
                    )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = HomeState.Listing.name
        ) {
            composable(HomeState.Listing.name) {}
            composable(HomeState.Tickets.name) {}
            composable(HomeState.Cinemas.name) {}
            composable(HomeState.Profile.name) {}
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HomeScreenPreview() = PreviewLayout {
    HomeScreen()
}