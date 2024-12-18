package movie.metropolis.app.screen2.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import movie.metropolis.app.screen2.component.FloatingTab
import movie.metropolis.app.screen2.component.FloatingTabRow
import movie.metropolis.app.screen2.component.Icons
import movie.metropolis.app.screen2.component.TextLogo
import movie.style.layout.PreviewLayout

@Composable
fun HomeScreen(
    loggedIn: Boolean,
    modifier: Modifier = Modifier,
    haze: HazeState = remember { HazeState() },
    navController: NavHostController = rememberNavController()
) = Scaffold(
    modifier = modifier,
    topBar = {
        val entry by navController.currentBackStackEntryAsState()
        val route = entry?.destination?.route
        val index = when (route) {
            HomeRoute.Movies -> 0
            HomeRoute.Upcoming -> 1
            HomeRoute.Tickets -> 2
            else -> 0
        }
        val style = HazeStyle(MaterialTheme.colorScheme.surfaceTint.copy(.1f))
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row {
                IconButton({}, Modifier.hazeChild(haze, MaterialTheme.shapes.medium, style)) {
                    Icon(Icons.Search, null)
                }
                TextLogo(Modifier.weight(1f))
                IconButton({}, Modifier.hazeChild(haze, MaterialTheme.shapes.medium, style)) {
                    Icon(Icons.Profile, null)
                }
            }
            FloatingTabRow(
                modifier = Modifier.hazeChild(haze, MaterialTheme.shapes.medium, style),
                selectedTabIndex = index
            ) {
                FloatingTab(route == null || route == HomeRoute.Movies, {}) { Text("Movies") }
                FloatingTab(route == HomeRoute.Upcoming, {}) { Text("Upcoming") }
                FloatingTab(route == HomeRoute.Tickets, {}, enabled = loggedIn) { Text("Tickets") }
            }
        }
    },
    bottomBar = {

    }
) { padding ->
    NavHost(
        modifier = Modifier.haze(haze),
        navController = navController,
        startDestination = HomeRoute.Movies
    ) {
        composable(HomeRoute.Movies) {}
        composable(HomeRoute.Upcoming) {}
        composable(HomeRoute.Tickets) {}
    }
}

object HomeRoute {
    const val Movies = "movies"
    const val Upcoming = "upcoming"
    const val Tickets = "tickets"
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun HomeScreenPreview() = PreviewLayout {
    HomeScreen(false)
}