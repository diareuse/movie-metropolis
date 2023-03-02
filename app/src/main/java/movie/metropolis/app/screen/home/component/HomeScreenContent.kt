package movie.metropolis.app.screen.home.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import movie.metropolis.app.R
import movie.metropolis.app.screen.Route
import movie.metropolis.app.screen.home.component.HomeScreenContentParameter.Data
import movie.style.layout.PreviewLayout

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreenContent(
    startWith: Route,
    icon: @Composable () -> Unit,
    listing: @Composable (PaddingValues, TopAppBarScrollBehavior) -> Unit,
    cinemas: @Composable (PaddingValues, TopAppBarScrollBehavior) -> Unit,
    booking: @Composable (PaddingValues, TopAppBarScrollBehavior) -> Unit,
    controller: NavHostController = rememberAnimatedNavController()
) {
    val state = rememberHomeScreenState()
    HomeScreenLayout(
        state = state,
        profileIcon = icon
    ) { padding, behavior ->
        AnimatedNavHost(
            navController = controller,
            startDestination = startWith()
        ) {
            composable(
                route = Route.Movies(),
                deepLinks = Route.Movies.deepLinks
            ) {
                SideEffect {
                    state.title = R.string.now_available
                }
                listing(padding, behavior)
            }
            composable(
                route = Route.Cinemas(),
                deepLinks = Route.Cinemas.deepLinks
            ) {
                SideEffect {
                    state.title = R.string.cinemas
                }
                cinemas(padding, behavior)
            }
            composable(
                route = Route.Tickets(),
                deepLinks = Route.Tickets.deepLinks
            ) {
                SideEffect {
                    state.title = R.string.tickets
                }
                booking(padding, behavior)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HomeScreenContentPreview(
    @PreviewParameter(HomeScreenContentParameter::class)
    parameter: Data
) = PreviewLayout {
    HomeScreenContent(
        startWith = parameter.route,
        icon = {
            Box(
                Modifier
                    .size(24.dp)
                    .background(Color.Magenta)
            )
        },
        listing = { padding, _ ->
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color.Red)
            )
        },
        cinemas = { padding, _ ->
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color.Green)
            )
        },
        booking = { padding, _ ->
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color.Blue)
            )
        }
    )
}

private class HomeScreenContentParameter : CollectionPreviewParameterProvider<Data>(
    listOf(
        Data(Route.Movies), Data(Route.Cinemas), Data(Route.Tickets)
    )
) {
    data class Data(val route: Route)
}