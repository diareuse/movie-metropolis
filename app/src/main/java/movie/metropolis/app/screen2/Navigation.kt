@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)

package movie.metropolis.app.screen2

import android.Manifest
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.pager.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.collections.immutable.toImmutableList
import movie.metropolis.app.feature.location.rememberLocation
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.screen.Route
import movie.metropolis.app.screen2.booking.BookingScreen
import movie.metropolis.app.screen2.booking.BookingViewModel
import movie.metropolis.app.screen2.cinema.CinemasScreen
import movie.metropolis.app.screen2.cinema.CinemasViewModel
import movie.metropolis.app.screen2.home.HomeScreen
import movie.metropolis.app.screen2.home.HomeViewModel
import movie.metropolis.app.screen2.listing.ListingScreen
import movie.metropolis.app.screen2.listing.ListingViewModel
import movie.metropolis.app.screen2.setup.SetupScreen
import movie.metropolis.app.screen2.setup.SetupViewModel

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
) {
    val setupViewModel = hiltViewModel<SetupViewModel>()
    LaunchedEffect(setupViewModel) {
        setupViewModel.requiresSetup.collect {
            if (it) navController.navigate(Route.Setup()) {
                popUpTo(Route.Home()) {
                    inclusive = true
                }
            }
        }
    }
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Route.Home(),
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { fadeOut() + slideOutHorizontally() },
        popEnterTransition = { fadeIn() + slideInHorizontally() },
        popExitTransition = { slideOutHorizontally { it } }
    ) {
        setup(navController)
        home(navController)
        login(navController)
        user(navController)
        cinema(navController)
        movie(navController)
        order(navController)
        orderComplete(navController)
    }
}

private fun NavGraphBuilder.setup(
    navController: NavHostController
) = composable(
    route = Route.Setup.route,
    deepLinks = Route.Setup.deepLinks
) {
    val viewModel = hiltViewModel<SetupViewModel>()
    val regions by viewModel.regions.collectAsState()
    val posters = viewModel.posters.toImmutableList()
    LaunchedEffect(viewModel) {
        viewModel.requiresSetup.collect {
            if (!it) navController.navigate(Route.Home())
        }
    }
    SetupScreen(
        regions = regions.getOrNull().orEmpty().toImmutableList(),
        posters = posters,
        onRegionClick = viewModel::select
    )
}

private fun NavGraphBuilder.home(
    navController: NavHostController
) = composable(
    route = Route.Home.route,
    arguments = Route.Home.arguments,
    deepLinks = Route.Home.deepLinks
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val listingVM = hiltViewModel<ListingViewModel>()
    val cinemasVM = hiltViewModel<CinemasViewModel>()
    val bookingVM = hiltViewModel<BookingViewModel>()
    val listingState = rememberLazyStaggeredGridState()
    val cinemasState = rememberLazyListState()
    val tickets by bookingVM.tickets.collectAsState()
    val bookingState = rememberPagerState { tickets.size }
    val cinemas by cinemasVM.cinemas.collectAsState()
    val movies by listingVM.movies.collectAsState()
    val promotions by listingVM.promotions.collectAsState()
    val user by viewModel.user.collectAsState(Loadable.loading())
    HomeScreen(
        user = user.getOrNull(),
        listing = { modifier, padding ->
            ListingScreen(
                modifier = modifier,
                contentPadding = padding,
                state = listingState,
                movies = movies.toImmutableList(),
                promotions = promotions.toImmutableList(),
                onClick = { navController.navigate(Route.Movie(it.id)) },
                onFavoriteClick = { listingVM.favorite(it) }
            )
        },
        tickets = { modifier, padding ->
            LaunchedEffect(Unit) {
                bookingVM.refresh()
            }
            BookingScreen(
                bookings = tickets.toImmutableList(),
                state = bookingState,
                modifier = modifier,
                contentPadding = padding
            )
        },
        cinemas = { modifier, padding ->
            val state = rememberMultiplePermissionsState(
                listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            val location by rememberLocation(state)
            LaunchedEffect(location) {
                cinemasVM.location.value = location
            }
            CinemasScreen(
                modifier = modifier,
                contentPadding = padding,
                cinemas = cinemas.toImmutableList(),
                permission = state,
                state = cinemasState,
                onClick = { navController.navigate(Route.Cinema(it.id)) },
                onMapClick = { /* todo open maps */ }
            )
        },
        profile = { modifier, padding -> }
    )
}

private fun NavGraphBuilder.login(
    navController: NavHostController
) = composable(
    route = Route.Login.route,
    deepLinks = Route.Login.deepLinks
) {
}

private fun NavGraphBuilder.user(
    navController: NavHostController
) = composable(
    route = Route.User.route,
    deepLinks = Route.User.deepLinks
) {
}

private fun NavGraphBuilder.cinema(
    navController: NavHostController
) = composable(
    route = Route.Cinema.route,
    arguments = Route.Cinema.arguments,
    deepLinks = Route.Cinema.deepLinks
) {
}

private fun NavGraphBuilder.movie(
    navController: NavHostController
) = composable(
    route = Route.Movie.route,
    arguments = Route.Movie.arguments,
    deepLinks = Route.Movie.deepLinks
) {
}

private fun NavGraphBuilder.order(
    navController: NavHostController
) = composable(
    route = Route.Order.route,
    arguments = Route.Order.arguments,
    deepLinks = Route.Order.deepLinks
) {
}

private fun NavGraphBuilder.orderComplete(
    navController: NavHostController
) = composable(
    route = Route.OrderComplete.route,
    deepLinks = Route.OrderComplete.deepLinks
) {
}