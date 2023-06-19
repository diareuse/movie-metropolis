package movie.metropolis.app.screen

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import movie.metropolis.app.R
import movie.metropolis.app.screen.cinema.CinemaScreen
import movie.metropolis.app.screen.detail.MovieScreen
import movie.metropolis.app.screen.home.HomeScreen
import movie.metropolis.app.screen.order.OrderScreen
import movie.metropolis.app.screen.order.complete.OrderCompleteScreen
import movie.metropolis.app.screen.profile.LoginScreen
import movie.metropolis.app.screen.profile.UserScreen
import movie.metropolis.app.screen.setup.SetupScreen
import movie.metropolis.app.screen.setup.SetupViewModel
import movie.style.LocalWindowSizeClass
import movie.style.modifier.surface
import movie.style.theme.Theme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(
    controller: NavHostController = rememberAnimatedNavController()
) {
    val setupViewModel = hiltViewModel<SetupViewModel>()
    val requiresSetup by setupViewModel.requiresSetup.collectAsState()
    AnimatedNavHost(
        navController = controller,
        startDestination = if (requiresSetup) Route.Setup() else Route.Home(),
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { fadeOut() + slideOutHorizontally() },
        popEnterTransition = { fadeIn() + slideInHorizontally() },
        popExitTransition = { slideOutHorizontally { it } }
    ) {
        composable(
            route = Route.Setup(),
            deepLinks = Route.Setup.deepLinks
        ) {
            SetupScreen(
                viewModel = setupViewModel,
                onNavigateHome = {
                    controller.popBackStack(Route.Setup(), true)
                    controller.navigate(Route.Home.destination())
                }
            )
        }
        composable(
            route = Route.Home(),
            arguments = Route.Home.arguments,
            deepLinks = Route.Home.deepLinks
        ) {
            val args = remember(it) { Route.Home.Arguments(it) }

            @Composable
            fun draw(
                controller: NavHostController,
                originController: NavHostController = rememberAnimatedNavController()
            ) {
                HomeScreen(
                    startWith = args.screen,
                    controller = originController,
                    onClickMovie = { id, upcoming ->
                        controller.navigate(Route.Movie.destination(id, upcoming))
                    },
                    onClickCinema = { id -> controller.navigate(Route.Cinema.destination(id)) },
                    onClickUser = { controller.navigate(Route.User.destination()) },
                    onClickLogin = { controller.navigate(Route.Login.destination()) }
                )
            }
            when (LocalWindowSizeClass.current.widthSizeClass) {
                WindowWidthSizeClass.Expanded -> Row {
                    val innerController = rememberAnimatedNavController()
                    val homeController = rememberAnimatedNavController()
                    val destination by homeController.currentDestinationAsState()
                    LaunchedEffect(destination) {
                        do {
                            if (innerController.currentBackStackEntry?.destination?.route == "none")
                                break
                        } while (innerController.popBackStack())
                    }
                    Box(modifier = Modifier.weight(1f), propagateMinConstraints = true) {
                        draw(innerController, homeController)
                    }
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .alpha(.18f)
                            .statusBarsPadding()
                            .navigationBarsPadding()
                            .background(LocalContentColor.current, CircleShape)
                    )
                    AnimatedNavHost(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .surface(1.dp),
                        navController = innerController,
                        startDestination = "none"
                    ) {
                        composable("none") {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    modifier = Modifier.padding(64.dp),
                                    text = stringResource(R.string.select_content_left),
                                    style = Theme.textStyle.title,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        login(controller)
                        user(innerController)
                        cinema(innerController)
                        movie(innerController)
                        order(
                            controller = innerController,
                            onNavigateToOrderComplete = { controller.navigate(Route.OrderComplete.destination()) }
                        )
                    }
                }

                else -> draw(controller)
            }

        }
        login(controller)
        user(controller)
        cinema(controller)
        movie(controller)
        order(
            controller = controller,
            onNavigateToOrderComplete = { controller.navigate(Route.OrderComplete.destination()) }
        )
        orderComplete(controller)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.login(
    controller: NavHostController
) = composable(
    route = Route.Login(),
    deepLinks = Route.Login.deepLinks
) {
    LoginScreen(
        onNavigateHome = {
            controller.popBackStack(Route.Home(), true)
            controller.navigate(Route.Home.destination())
        },
        onBackClick = controller::navigateUp
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.user(
    controller: NavHostController
) = composable(
    route = Route.User(),
    deepLinks = Route.User.deepLinks
) {
    UserScreen(
        onNavigateBack = controller::navigateUp
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.cinema(
    controller: NavHostController
) = composable(
    route = Route.Cinema(),
    arguments = Route.Cinema.arguments,
    deepLinks = Route.Cinema.deepLinks
) {
    CinemaScreen(
        onBackClick = controller::navigateUp,
        onBookingClick = { url -> controller.navigate(Route.Order.destination(url)) }
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.movie(
    controller: NavHostController
) = composable(
    route = Route.Movie(),
    arguments = Route.Movie.arguments,
    deepLinks = Route.Movie.deepLinks
) {
    MovieScreen(
        onBackClick = controller::navigateUp,
        onBookingClick = { url -> controller.navigate(Route.Order.destination(url)) }
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.order(
    controller: NavHostController,
    onNavigateToOrderComplete: () -> Unit
) = composable(
    route = Route.Order(),
    arguments = Route.Order.arguments,
    deepLinks = Route.Order.deepLinks
) {
    OrderScreen(
        onBackClick = controller::navigateUp,
        onCompleted = {
            controller.popBackStack(Route.Order(), true)
            onNavigateToOrderComplete()
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.orderComplete(
    controller: NavHostController
) = composable(
    route = Route.OrderComplete(),
    deepLinks = Route.OrderComplete.deepLinks
) {
    OrderCompleteScreen(
        onBackClick = {
            controller.popBackStack(Route.Home(), true)
            controller.navigate(Route.Home.destination(Route.Tickets.destination()))
        }
    )
}

val NavHostController.currentDestinationFlow
    get() = callbackFlow {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            trySend(destination)
        }
        addOnDestinationChangedListener(listener)
        awaitClose {
            removeOnDestinationChangedListener(listener)
        }
    }

@Composable
fun NavHostController.currentDestinationAsState(): State<NavDestination?> {
    return currentDestinationFlow.collectAsState(currentDestination)
}