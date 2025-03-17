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
    ticket: @Composable RowScope.(BookingView) -> Unit,
    onShowAllTicketsClick: () -> Unit,
    onShowAllRecommendedClick: () -> Unit,
    onShowAllComingSoonClick: () -> Unit,
    onShowAllCinemasClick: () -> Unit,
    cinema: @Composable LazyItemScope.(CinemaView) -> Unit,
    movie: @Composable (m: MovieView, shape: Shape, fraction: Float, upcoming: Boolean) -> Unit,
    // --- placeholders
    userPlaceholder: @Composable () -> Unit = { UserTopBar() },
    ticketPlaceholder: @Composable () -> Unit = { TicketBox() },
    moviePlaceholder: @Composable (Shape, Float) -> Unit = { shape, fraction ->
        MovieBox(
            shape = shape,
            fraction = fraction
        )
    },
    cinemaPlaceholder: @Composable () -> Unit = { CinemaBox() },
    // --- errors
    userError: @Composable () -> Unit,
    ticketError: @Composable () -> Unit,
    movieError: @Composable () -> Unit,
    cinemaError: @Composable () -> Unit,
    // ---
    modifier: Modifier = Modifier,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.pc),
            horizontalArrangement = Arrangement.spacedBy(-(3).pc),
            verticalAlignment = Alignment.Top
        ) {
            for (i in 0..<ticketCount) Box(
                modifier = Modifier
                    .weight(1f)
                    .zIndex(1f * ticketCount - i)
                    .scale(1f - i * 0.05f)
                    .blur(2.dp * i, edgeTreatment = BlurredEdgeTreatment.Unbounded),
                propagateMinConstraints = true
            ) {
                StateLayout(
                    state = state.tickets.state,
                    loaded = { _ ->
                        val item = state.tickets.tickets.getOrNull(i)
                        if (item != null) key(item.id) {
                            ticket(this@Row, item)
                        }
                        else ticketPlaceholder()
                    },
                    error = { ticketError() },
                    loading = { ticketPlaceholder() }
                )
            }

        }

        // --- recommended section
        OpenableSection(
            modifier = Modifier
                .animateItemAppearance(scale = 1.25f)
                .padding(horizontal = 2.pc)
                .padding(top = 2.pc),
            onClick = onShowAllRecommendedClick
        ) {
            Text("Recommended")
        }
        val recommendedState = rememberCarouselState { maxOf(state.recommended.size, 5) }
        HorizontalUncontainedCarousel(
            modifier = Modifier
                .fillMaxWidth(),
            state = recommendedState,
            itemWidth = 10.pc,
            itemSpacing = 1.pc,
            flingBehavior = CarouselDefaults.multiBrowseFlingBehavior(recommendedState),
            contentPadding = PaddingValues(horizontal = 2.pc)
        ) {
            val item = state.recommended.getOrNull(it)
            val shape = rememberMaskShape(MaterialTheme.shapes.medium)
            if (item == null) moviePlaceholder(shape, fraction)
            else key(item.id) {
                movie(item, shape, fraction, false)
            }
        }

        // --- coming soon section
        OpenableSection(
            modifier = Modifier
                .animateItemAppearance(scale = 1.25f)
                .padding(horizontal = 2.pc)
                .padding(top = 2.pc),
            onClick = onShowAllComingSoonClick
        ) {
            Text("Coming Soon")
        }
        val comingSoonState = rememberCarouselState { maxOf(state.comingSoon.size, 5) }
        HorizontalUncontainedCarousel(
            modifier = Modifier
                .fillMaxWidth(),
            state = comingSoonState,
            itemWidth = 10.pc,
            itemSpacing = 1.pc,
            flingBehavior = CarouselDefaults.multiBrowseFlingBehavior(comingSoonState),
            contentPadding = PaddingValues(horizontal = 2.pc)
        ) {
            val item = state.comingSoon.getOrNull(it)
            val shape = rememberMaskShape(MaterialTheme.shapes.medium)
            if (item == null) moviePlaceholder(shape, fraction)
            else key(item.id) {
                movie(item, shape, fraction, true)
            }
        }

        // --- cinemas section
        OpenableSection(
            modifier = Modifier
                .animateItemAppearance(scale = 1.25f)
                .padding(horizontal = 2.pc)
                .padding(top = 2.pc),
            onClick = onShowAllCinemasClick
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
        movie = { _, _, _, _ -> },
        userPlaceholder = {},
        ticketPlaceholder = {},
        moviePlaceholder = { _, _ -> },
        cinemaPlaceholder = {},
        userError = {},
        ticketError = {},
        movieError = {},
        cinemaError = {},
    )
}