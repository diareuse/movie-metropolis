package movie.metropolis.app.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.pager.*
import androidx.compose.material3.*
import androidx.compose.material3.carousel.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.ui.home.component.CinemaBox
import movie.metropolis.app.ui.home.component.MovieBox
import movie.metropolis.app.ui.home.component.TicketBox
import movie.metropolis.app.ui.home.component.UserTopBar
import movie.metropolis.app.ui.home.component.windowBackground
import movie.style.layout.PreviewLayout
import movie.style.layout.StateLayout
import movie.style.modifier.animateItemAppearance
import movie.style.util.pc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenScaffold(
    state: HomeScreenState,
    // --- main content
    userAccount: @Composable (UserView, MembershipView?) -> Unit,
    ticket: @Composable (BookingView) -> Unit,
    onShowAllTicketsClick: () -> Unit,
    cinema: @Composable LazyItemScope.(CinemaView) -> Unit,
    movie: @Composable (m: MovieView, upcoming: Boolean) -> Unit,
    // --- errors
    userError: @Composable () -> Unit,
    ticketError: @Composable () -> Unit,
    movieError: @Composable () -> Unit,
    cinemaError: @Composable () -> Unit,
    // ---
    modifier: Modifier = Modifier,
    // --- placeholders
    userPlaceholder: @Composable () -> Unit = { UserTopBar() },
    ticketPlaceholder: @Composable () -> Unit = { TicketBox() },
    moviePlaceholder: @Composable () -> Unit = { MovieBox() },
    cinemaPlaceholder: @Composable () -> Unit = { CinemaBox() },
    ticketCount: Int = 4,
) = Scaffold(
    modifier = modifier,
    contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom)
) { padding ->
    Column(
        modifier = Modifier
            .windowBackground()
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
                .animateItemAppearance(scale = 1.25f)
                .padding(horizontal = 2.pc)
                .padding(top = 2.pc),
            onClick = onShowAllTicketsClick
        ) {
            Text("Tickets")
        }
        val pagerState = rememberPagerState { maxOf(state.tickets.tickets.size, 5) }
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(10.pc),
            contentPadding = PaddingValues(horizontal = 2.pc),
            pageSpacing = 1.pc
        ) {
            Box(
                modifier = Modifier.zIndex(1f * ticketCount - it)
            ) {
                StateLayout(
                    state = state.tickets.state,
                    loaded = { _ ->
                        val item = state.tickets.tickets.getOrNull(it)
                        if (item != null) key(item.id) {
                            ticket(item)
                        }
                        else ticketPlaceholder()
                    },
                    error = { ticketError() },
                    loading = { ticketPlaceholder() }
                )
            }
        }

        // --- recommended section
        Section(
            modifier = Modifier
                .animateItemAppearance(scale = 1.25f)
                .padding(horizontal = 2.pc)
                .padding(top = 2.pc)
        ) {
            Text("Recommended")
        }
        val recommendedState = rememberLazyListState()
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(1.pc),
            contentPadding = PaddingValues(horizontal = 2.pc),
            state = recommendedState
        ) {
            items(state.recommended, key = { it.id }) { item ->
                Box(
                    modifier = Modifier
                        .animateItem()
                        .width(10.pc),
                    propagateMinConstraints = true
                ) {
                    movie(item, false)
                }
            }
            items(5 - state.recommended.size.coerceAtMost(5)) {
                Box(
                    modifier = Modifier.width(10.pc),
                    propagateMinConstraints = true
                ) {
                    moviePlaceholder()
                }
            }
        }

        // --- coming soon section
        Section(
            modifier = Modifier
                .animateItemAppearance(scale = 1.25f)
                .padding(horizontal = 2.pc)
                .padding(top = 2.pc)
        ) {
            Text("Coming Soon")
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(1.pc),
            contentPadding = PaddingValues(horizontal = 2.pc)
        ) {
            items(state.comingSoon, key = { it.id }) { item ->
                Box(
                    modifier = Modifier
                        .animateItem()
                        .width(10.pc),
                    propagateMinConstraints = true
                ) {
                    movie(item, false)
                }
            }
            items(5 - state.comingSoon.size.coerceAtMost(5)) {
                Box(
                    modifier = Modifier.width(10.pc),
                    propagateMinConstraints = true
                ) {
                    moviePlaceholder()
                }
            }
        }

        // --- cinemas section
        Section(
            modifier = Modifier
                .animateItemAppearance(scale = 1.25f)
                .padding(horizontal = 2.pc)
                .padding(top = 2.pc)
        ) {
            Text("Cinemas")
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
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

@OptIn(ExperimentalMaterial3Api::class)
val CarouselItemScope.fraction get() = with(carouselItemInfo) { (size - minSize) / (maxSize - minSize) }

@Composable
fun OpenableSection(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
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

@Composable
fun Section(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = Row(
    modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 1.pc),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {
    ProvideTextStyle(MaterialTheme.typography.titleSmall) {
        content()
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
        cinema = {},
        movie = { _, _ -> },
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