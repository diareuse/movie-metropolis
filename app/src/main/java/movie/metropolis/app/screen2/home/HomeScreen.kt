package movie.metropolis.app.screen2.home

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.screen2.card.CardScreen
import movie.metropolis.app.screen2.card.CardScreenState
import movie.metropolis.app.screen2.home.component.TransparentBottomNavigation
import movie.metropolis.app.screen2.home.component.TransparentBottomNavigationItem
import movie.metropolis.app.util.getStoreable
import movie.style.layout.PreviewLayout
import movie.style.layout.alignForLargeScreen
import movie.style.modifier.VerticalGravity
import movie.style.modifier.verticalOverlay

@Composable
fun HomeScreen(
    loggedIn: Boolean,
    user: UserView?,
    membership: MembershipView?,
    showProfile: Boolean,
    onShowProfileChange: (Boolean) -> Unit,
    onNavigateToLogin: () -> Unit,
    startWith: HomeState?,
    listing: @Composable (Modifier, PaddingValues) -> Unit,
    tickets: @Composable (Modifier, PaddingValues) -> Unit,
    cinemas: @Composable (Modifier, PaddingValues) -> Unit,
    profile: @Composable (Modifier, PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
) = CardOverlay(
    user = user,
    membership = membership,
    showProfile = showProfile,
    onCloseRequest = { onShowProfileChange(false) },
    modifier = modifier
) { modifier ->
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    val route = entry?.destination?.route
    val currentState = HomeState.by(route)
    if (!loggedIn) LaunchedEffect(entry) {
        val entry = entry ?: return@LaunchedEffect
        val destination = entry.destination
        when (destination.route) {
            HomeState.Profile.name -> {
                onNavigateToLogin()
                navController.navigateBottomNav(HomeState.Listing.name)
            }
        }
    }
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.navigationBars,
        bottomBar = {
            TransparentBottomNavigation(
                modifier = Modifier.alignForLargeScreen()
            ) {
                for (state in HomeState.entries)
                    TransparentBottomNavigationItem(
                        selected = route == state.name,
                        active = { Icon(painterResource(state.active), null) },
                        inactive = { Icon(painterResource(state.inactive), null) },
                        onClick = { navController.navigateBottomNav(state.name) }
                    )
            }
        }
    ) { padding ->
        val overlayModifier = Modifier
            .verticalOverlay(
                height = padding.calculateBottomPadding() + 32.dp,
                gravity = VerticalGravity.Bottom
            )
        val destinationTab = getStoreable("home-tab", HomeState.Listing.name) {
            currentState.name
        }
        LaunchedEffect(Unit) {
            if (navController.currentBackStackEntry?.destination?.route != destinationTab)
                navController.navigateBottomNav(destinationTab)
            val startWith = startWith?.name
            if (startWith != null)
                navController.navigateBottomNav(startWith)
        }
        NavHost(
            modifier = Modifier
                .fillMaxSize(),
            navController = navController,
            startDestination = HomeState.Listing.name,
            enterTransition = {
                val initial = initialState.getIndex()
                val target = targetState.getIndex()
                if (initial < target) {
                    fadeIn() + slideInHorizontally { it / 2 }
                } else {
                    fadeIn() + slideInHorizontally { -it / 2 }
                }
            },
            exitTransition = {
                val initial = initialState.getIndex()
                val target = targetState.getIndex()
                if (initial < target) {
                    fadeOut() + slideOutHorizontally { -it / 2 }
                } else {
                    fadeOut() + slideOutHorizontally { it / 2 }
                }
            }
        ) {
            composable(HomeState.Listing.name) { listing(overlayModifier, padding) }
            composable(HomeState.Tickets.name) { tickets(overlayModifier, padding) }
            composable(HomeState.Cinemas.name) { cinemas(overlayModifier, padding) }
            composable(HomeState.Profile.name) { profile(overlayModifier, padding) }
        }
    }
}

private fun NavHostController.navigateBottomNav(route: String) = navigate(route) {
    popUpTo(graph.findStartDestination().id) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}

private fun NavBackStackEntry.getIndex() = when (destination.route) {
    HomeState.Listing.name -> HomeState.Listing.ordinal
    HomeState.Tickets.name -> HomeState.Tickets.ordinal
    HomeState.Cinemas.name -> HomeState.Cinemas.ordinal
    HomeState.Profile.name -> HomeState.Profile.ordinal
    else -> 0
}

@Composable
fun CardOverlay(
    user: UserView?,
    membership: MembershipView?,
    showProfile: Boolean,
    onCloseRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) = Box(modifier = modifier, propagateMinConstraints = true) {
    val cardState = remember { CardScreenState() }
    LaunchedEffect(showProfile) {
        if (showProfile) cardState.open()
        else cardState.close()
    }
    content(Modifier.blur(cardState.blur))
    if (
        membership != null &&
        user != null &&
        (showProfile || cardState.isOpen)
    ) CardScreen(
        user = user,
        membership = membership,
        state = cardState,
        onCloseRequest = onCloseRequest
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HomeScreenPreview() = PreviewLayout {
    HomeScreen(
        loggedIn = false,
        user = null,
        membership = null,
        showProfile = false,
        onShowProfileChange = {},
        onNavigateToLogin = {},
        startWith = HomeState.Listing,
        listing = { _, _ -> },
        tickets = { _, _ -> },
        cinemas = { _, _ -> },
        profile = { _, _ -> }
    )
}