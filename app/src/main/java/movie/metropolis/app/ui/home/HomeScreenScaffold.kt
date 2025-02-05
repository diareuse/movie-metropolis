package movie.metropolis.app.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.material3.carousel.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.ui.home.component.windowBackground
import movie.style.layout.PreviewLayout
import movie.style.layout.StateLayout
import movie.style.util.pc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenScaffold(
    state: HomeScreenState,
    // --- main content
    userAccount: @Composable (UserView, MembershipView?) -> Unit,
    ticket: @Composable RowScope.(BookingView) -> Unit,
    onShowAllTicketsClick: () -> Unit,
    onShowAllRecommendedClick: () -> Unit,
    onShowAllComingSoonClick: () -> Unit,
    onShowAllCinemasClick: () -> Unit,
    cinema: @Composable LazyItemScope.(CinemaView) -> Unit,
    movie: @Composable (MovieView) -> Unit,
    // --- placeholders
    userPlaceholder: @Composable () -> Unit,
    ticketPlaceholder: @Composable () -> Unit,
    moviePlaceholder: @Composable () -> Unit,
    cinemaPlaceholder: @Composable () -> Unit,
    // --- errors
    userError: @Composable () -> Unit,
    ticketError: @Composable () -> Unit,
    movieError: @Composable () -> Unit,
    cinemaError: @Composable () -> Unit,
    // ---
    modifier: Modifier = Modifier,
    ticketCount: Int = 4,
) = Scaffold(
    modifier = modifier.windowBackground(),
    contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom),
    containerColor = Color.Transparent,
    contentColor = LocalContentColor.current
) { padding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(padding)
            .systemBarsPadding()
    ) {
        StateLayout(
            state = state.profile.user,
            loaded = { userAccount(it, state.profile.membership.getOrNull()) },
            error = userError,
            loading = userPlaceholder
        )

        // --- tickets section
        OpenableSection(
            modifier = Modifier
                .padding(horizontal = 2.pc)
                .padding(top = 2.pc),
            onClick = onShowAllTicketsClick
        ) {
            Text("Tickets")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 2.pc),
            horizontalArrangement = Arrangement.spacedBy(-(3).pc),
            verticalAlignment = Alignment.Top
        ) {
            StateLayout(
                state = state.tickets.state,
                loaded = { _ ->
                    MaterialTheme.colorScheme.surface
                    for (i in 0..<ticketCount) Box(
                        modifier = Modifier
                            .weight(1f)
                            .zIndex(1f * ticketCount - i)
                            .scale(1f - i * 0.05f)
                            .blur(2.dp * i, edgeTreatment = BlurredEdgeTreatment.Unbounded),
                        propagateMinConstraints = true
                    ) {
                        val item = state.tickets.tickets.getOrNull(i)
                        if (item == null) ticketPlaceholder()
                        else key(item.id) {
                            ticket(this@Row, item)
                        }
                    }
                },
                error = { repeat(3) { ticketError() } },
                loading = { repeat(3) { ticketPlaceholder() } })
        }

        // --- recommended section
        OpenableSection(
            modifier = Modifier
                .padding(horizontal = 2.pc)
                .padding(top = 2.pc),
            onClick = onShowAllRecommendedClick
        ) {
            Text("Recommended")
        }
        val recommendedState = rememberCarouselState { maxOf(state.recommended.size, 5) }
        HorizontalUncontainedCarousel(
            state = recommendedState,
            itemWidth = 10.pc,
            itemSpacing = 1.pc,
            flingBehavior = CarouselDefaults.multiBrowseFlingBehavior(recommendedState),
            contentPadding = PaddingValues(horizontal = 2.pc)
        ) {
            Box(modifier = Modifier.maskClip(MaterialTheme.shapes.medium)) {
                val item = state.recommended.getOrNull(it)
                if (item == null) moviePlaceholder()
                else key(item.id) {
                    movie(item)
                }
            }
        }

        // --- coming soon section
        OpenableSection(
            modifier = Modifier
                .padding(horizontal = 2.pc)
                .padding(top = 2.pc),
            onClick = onShowAllComingSoonClick
        ) {
            Text("Coming Soon")
        }
        val comingSoonState = rememberCarouselState { maxOf(state.comingSoon.size, 5) }
        HorizontalUncontainedCarousel(
            state = comingSoonState,
            itemWidth = 10.pc,
            itemSpacing = 1.pc,
            flingBehavior = CarouselDefaults.multiBrowseFlingBehavior(comingSoonState),
            contentPadding = PaddingValues(horizontal = 2.pc)
        ) {
            Box(modifier = Modifier.maskClip(MaterialTheme.shapes.medium)) {
                val item = state.comingSoon.getOrNull(it)
                if (item == null) moviePlaceholder()
                else key(item.id) {
                    movie(item)
                }
            }
        }

        // --- cinemas section
        OpenableSection(
            modifier = Modifier
                .padding(horizontal = 2.pc)
                .padding(top = 2.pc),
            onClick = onShowAllCinemasClick
        ) {
            Text("Cinemas")
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 2.pc),
            horizontalArrangement = Arrangement.spacedBy(1.pc)
        ) {
            state.cinemas.ifEmpty {
                items(3) { cinemaPlaceholder() }
            }
            items(state.cinemas, key = { it.id }) {
                cinema(it)
            }
        }
    }
}

@Composable
fun OpenableSection(
    onClick: () -> Unit, modifier: Modifier = Modifier, content: @Composable () -> Unit
) = Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {
    ProvideTextStyle(MaterialTheme.typography.titleSmall) {
        content()
    }
    TextButton(onClick) {
        Text("Show all")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@PreviewFontScale
@Composable
private fun HomeScreenScaffoldPreview() = PreviewLayout {
    HomeScreenScaffold(
        state = remember { HomeScreenState() },
        userAccount = { _, _ -> },
        ticket = {},
        onShowAllTicketsClick = {},
        onShowAllRecommendedClick = {},
        onShowAllComingSoonClick = {},
        onShowAllCinemasClick = {},
        cinema = {},
        movie = {},
        userPlaceholder = {},
        ticketPlaceholder = {},
        moviePlaceholder = {},
        cinemaPlaceholder = {},
        userError = {},
        ticketError = {},
        movieError = {},
        cinemaError = {},
    )
}