package movie.metropolis.app.screen.home.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import movie.metropolis.app.screen.Route
import movie.metropolis.app.screen.home.component.HomeScreenContentParameter.Data
import movie.style.layout.PreviewLayout

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreenContent(
    startWith: Route,
    listing: @Composable () -> Unit,
    cinemas: @Composable () -> Unit,
    booking: @Composable () -> Unit,
    settings: @Composable () -> Unit,
    controller: NavHostController = rememberAnimatedNavController()
) {
    AnimatedNavHost(
        navController = controller,
        startDestination = startWith()
    ) {
        composable(
            route = Route.Movies(),
            deepLinks = Route.Movies.deepLinks
        ) {
            listing()
        }
        composable(
            route = Route.Cinemas(),
            deepLinks = Route.Cinemas.deepLinks
        ) {
            cinemas()
        }
        composable(
            route = Route.Tickets(),
            deepLinks = Route.Tickets.deepLinks
        ) {
            booking()
        }
        composable(
            route = Route.Settings(),
            deepLinks = Route.Settings.deepLinks
        ) {
            settings()
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HomeScreenContentPreview(
    @PreviewParameter(HomeScreenContentParameter::class)
    parameter: Data
) = PreviewLayout {
    HomeScreenContent(
        startWith = parameter.route,
        listing = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Red)
            )
        },
        cinemas = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Green)
            )
        },
        booking = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Blue)
            )
        },
        settings = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Yellow)
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