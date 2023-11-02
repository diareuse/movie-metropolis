package movie.metropolis.app.screen2.home

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import movie.metropolis.app.R
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.screen.home.component.ProfileIcon
import movie.metropolis.app.screen2.card.CardScreen
import movie.metropolis.app.screen2.card.CardScreenState
import movie.metropolis.app.screen2.home.component.HomeToolbar
import movie.metropolis.app.screen2.home.component.TransparentBottomNavigation
import movie.metropolis.app.screen2.home.component.TransparentBottomNavigationItem
import movie.metropolis.app.util.getStoreable
import movie.style.layout.PreviewLayout
import movie.style.modifier.VerticalGravity
import movie.style.modifier.verticalOverlay

@Composable
fun HomeScreen(
    user: UserView?,
    membership: MembershipView?,
    listing: @Composable (Modifier, PaddingValues) -> Unit,
    tickets: @Composable (Modifier, PaddingValues) -> Unit,
    cinemas: @Composable (Modifier, PaddingValues) -> Unit,
    profile: @Composable (Modifier, PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
) = Box(
    modifier = Modifier.fillMaxSize(),
    propagateMinConstraints = true
) {
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    val route = entry?.destination?.route
    val state = HomeState.by(route)
    val cardState = remember { CardScreenState() }
    var showProfile by remember { mutableStateOf(false) }
    LaunchedEffect(showProfile) {
        if (showProfile) cardState.open()
        else cardState.close()
    }
    Scaffold(
        modifier = modifier.blur(cardState.blur),
        topBar = {
            val title = @Composable { Text(stringResource(state.title)) }
            val settings = @Composable {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Rounded.Settings, null)
                }
            }
            when (user) {
                null -> HomeToolbar(
                    title = title,
                    settings = settings
                )

                else -> HomeToolbar(
                    icon = {
                        ProfileIcon(
                            email = user.email,
                            onClick = { showProfile = true }
                        )
                    },
                    name = { Text(stringResource(R.string.home_greeting, user.firstName)) },
                    title = title,
                    settings = settings
                )
            }
        },
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
        val overlayModifier = Modifier
            .verticalOverlay(
                height = padding.calculateTopPadding() + 32.dp,
                gravity = VerticalGravity.Top
            )
            .verticalOverlay(
                height = padding.calculateBottomPadding() + 32.dp,
                gravity = VerticalGravity.Bottom
            )
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = navController,
            startDestination = getStoreable("home-tab", HomeState.Listing.name) {
                state.name
            }
        ) {
            composable(HomeState.Listing.name) { listing(overlayModifier, padding) }
            composable(HomeState.Tickets.name) { tickets(overlayModifier, padding) }
            composable(HomeState.Cinemas.name) { cinemas(overlayModifier, padding) }
            composable(HomeState.Profile.name) { profile(overlayModifier, padding) }
        }
    }
    if (
        membership != null &&
        user != null &&
        (showProfile || cardState.isOpen)
    ) CardScreen(
        user = user,
        membership = membership,
        state = cardState,
        onCloseRequest = { showProfile = false }
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HomeScreenPreview() = PreviewLayout {
    HomeScreen(
        user = null,
        membership = null,
        listing = { _, _ -> },
        tickets = { _, _ -> },
        cinemas = { _, _ -> },
        profile = { _, _ -> }
    )
}