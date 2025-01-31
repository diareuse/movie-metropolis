@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.carousel.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import movie.metropolis.app.R
import movie.metropolis.app.screen.cinema.component.CinemaViewProvider
import movie.metropolis.app.screen.movie.component.MovieViewProvider
import movie.metropolis.app.ui.home.component.CinemaBox
import movie.metropolis.app.ui.home.component.LoyaltyCard
import movie.metropolis.app.ui.home.component.MovieBox
import movie.metropolis.app.ui.home.component.RatingBox
import movie.metropolis.app.ui.home.component.ScreenTitle
import movie.metropolis.app.ui.home.component.SectionTitle
import movie.metropolis.app.ui.home.component.TicketBox
import movie.style.Image
import movie.style.layout.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout
import movie.style.layout.plus
import movie.style.modifier.animateItemAppearance
import movie.style.rememberImageState
import movie.style.rememberPaletteImageState
import movie.style.util.pc

val CardSize = 10.pc
val CarouselItemInfo.fraction get() = (size - minSize) / (maxSize - minSize)

@Composable
fun HomeScreen(
    state: HomeScreenState,
    onMovieClick: (id: String, upcoming: Boolean) -> Unit,
    onCinemaClick: (id: String) -> Unit,
    onTicketClick: (id: String) -> Unit,
    onProfileClick: () -> Unit,
    onTicketsClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
    haze: HazeState = remember { HazeState() }
) = HomeScreenScaffold(
    modifier = modifier,
    initialPage = initialPage,
    titleMovies = { Text("Movies") },
    titleCinemas = { Text("Cinemas") },
    profile = {
        IconButton(onProfileClick) {
            Icon(Icons.Default.AccountCircle, null)
        }
    },
    tickets = {
        IconButton(onTicketsClick) {
            Icon(Icons.Default.Email, null)
        }
    },
    movies = { padding ->
        Box(modifier = Modifier.fillMaxSize(), propagateMinConstraints = true) {
            val background = rememberPaletteImageState(
                state.tickets.tickets.firstOrNull()?.movie?.poster?.url
                    ?: state.current.firstOrNull()?.poster?.url
            )
            Image(
                modifier = Modifier
                    .hazeSource(haze)
                    .alpha(.1f)
                    .blur(32.dp)
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            Brush.verticalGradient(
                                listOf(
                                    background.palette.color.copy(alpha = 0f),
                                    background.palette.color
                                )
                            )
                        )
                    },
                state = background
            )
            LazyColumn(
                contentPadding = padding + PaddingValues(vertical = 2.pc),
                verticalArrangement = Arrangement.spacedBy(0.5.pc)
            ) {
                item {
                    ScreenTitle(
                        modifier = Modifier
                            .animateItemAppearance()
                            .animateItem()
                            .padding(horizontal = 2.pc)
                    ) {
                        Text("Hello %s!".format(state.profile.user?.firstName))
                    }
                }
                item(key = "card") {
                    val user = state.profile.user
                    val membership = state.profile.membership
                    if (user != null) LoyaltyCard(
                        modifier = Modifier
                            .animateItemAppearance()
                            .animateItem()
                            .padding(horizontal = 2.pc)
                            .padding(top = 2.pc)
                            .systemGestureExclusion()
                            .fillMaxWidth(),
                        haze = haze,
                        logo = { Image(painterResource(R.drawable.ic_logo_cinemacity), null) },
                        title = {
                            if (membership != null && membership.isExpired.not()) Text("Premium")
                            else Text("Expired")
                        },
                        name = { Text("%s %s".format(user.firstName, user.lastName)) },
                        number = { Text(membership?.cardNumber ?: "") }
                    )
                }
                item(key = "tickets-title") {
                    SectionTitle(
                        modifier = Modifier
                            .animateItemAppearance()
                            .animateItem()
                            .padding(horizontal = 2.pc)
                            .padding(vertical = 2.pc)
                    ) {
                        Text("Tickets")
                    }
                }
                item(key = "tickets") {
                    HorizontalMultiBrowseCarousel(
                        modifier = Modifier
                            .animateItemAppearance()
                            .animateItem()
                            .fillMaxWidth()
                            .systemGestureExclusion(),
                        state = rememberCarouselState { state.tickets.tickets.size },
                        preferredItemWidth = CardSize,
                        contentPadding = PaddingValues(horizontal = 2.pc),
                        itemSpacing = 1.pc,
                        flingBehavior = CarouselDefaults.noSnapFlingBehavior()
                    ) { index ->
                        val it = state.tickets.tickets[index]
                        val image = rememberPaletteImageState(it.movie.poster?.url)
                        val fraction = Modifier.alpha(carouselItemInfo.fraction)
                        TicketBox(
                            modifier = Modifier.maskClip(MaterialTheme.shapes.medium),
                            expired = it.expired,
                            onClick = { onTicketClick(it.id) },
                            date = { Text(it.date, Modifier.then(fraction)) },
                            time = { Text(it.time, Modifier.then(fraction)) },
                            poster = { Image(image) },
                            contentColor = image.palette.textColor,
                            color = image.palette.color
                        )
                    }
                }
                item(key = "upcoming-title") {
                    SectionTitle(
                        modifier = Modifier
                            .animateItemAppearance()
                            .animateItem()
                            .padding(horizontal = 2.pc)
                            .padding(vertical = 2.pc)
                    ) {
                        Text("Upcoming")
                    }
                }
                item(key = "upcoming") {
                    val items = state.upcoming
                    HorizontalMultiBrowseCarousel(
                        modifier = Modifier
                            .animateItemAppearance()
                            .animateItem()
                            .fillMaxWidth()
                            .systemGestureExclusion(),
                        state = rememberCarouselState { items.size },
                        preferredItemWidth = CardSize,
                        contentPadding = PaddingValues(horizontal = 2.pc),
                        itemSpacing = 1.pc,
                        flingBehavior = CarouselDefaults.noSnapFlingBehavior()
                    ) { index ->
                        val it = items[index]
                        val fraction = Modifier.alpha(carouselItemInfo.fraction)
                        val image = rememberPaletteImageState(it.poster?.url)
                        MovieBox(
                            modifier = Modifier
                                .maskClip(MaterialTheme.shapes.medium),
                            onClick = { onMovieClick(it.id, true) },
                            aspectRatio = it.poster?.aspectRatio ?: DefaultPosterAspectRatio,
                            name = { Text(it.name, Modifier.then(fraction)) },
                            poster = { Image(image) },
                            rating = {
                                val r = it.rating
                                if (r != null) RatingBox(
                                    color = image.palette.color
                                ) {
                                    Text(r)
                                }
                            },
                            category = {}
                        )
                    }
                }
                item(key = "showing") {
                    SectionTitle(
                        modifier = Modifier
                            .animateItem()
                            .padding(horizontal = 2.pc)
                            .padding(vertical = 2.pc)
                    ) {
                        Text("Currently Showing")
                    }
                }

                items(state.current.windowed(3, 3)) {
                    Row(
                        modifier = Modifier
                            .animateItem()
                            .padding(horizontal = 2.pc),
                        horizontalArrangement = Arrangement.spacedBy(1.pc)
                    ) {
                        for (it in it) {
                            // todo add pop-up animation on appear
                            val image = rememberPaletteImageState(it.poster?.url)
                            MovieBox(
                                modifier = Modifier
                                    .animateItemAppearance()
                                    .weight(1f),
                                onClick = { onMovieClick(it.id, false) },
                                aspectRatio = it.poster?.aspectRatio
                                    ?: DefaultPosterAspectRatio,
                                name = { Text(it.name) },
                                poster = { Image(image) },
                                rating = {
                                    val r = it.rating
                                    if (r != null) RatingBox(
                                        color = image.palette.color
                                    ) {
                                        Text(r)
                                    }
                                },
                                category = {}
                            )
                        }
                    }
                }
            }
        }
    },
    cinemas = { padding ->
        LazyColumn(
            modifier = Modifier.padding(horizontal = 1.pc),
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(1.pc)
        ) {
            items(state.cinemas) { view ->
                CinemaBox(
                    onClick = { onCinemaClick(view.id) },
                    name = { Text(view.name) },
                    city = { Text(view.city) },
                    distance = {
                        val d = view.distance
                        if (d != null) Text(d)
                    },
                    image = { Image(rememberImageState(view.image)) }
                )
            }
        }
    }
)

@PreviewLightDark
@Composable
private fun HomeScreenMoviesPreview() = PreviewLayout {
    val state = remember {
        HomeScreenState().apply {
            current.addAll(MovieViewProvider().values)
            upcoming.addAll(MovieViewProvider().values)
        }
    }
    HomeScreen(
        state = state,
        onMovieClick = { _, _ -> },
        onCinemaClick = {},
        onProfileClick = {},
        onTicketClick = {},
        onTicketsClick = {}
    )
}

@PreviewLightDark
@Composable
private fun HomeScreenCinemasPreview() = PreviewLayout {
    val state = remember {
        HomeScreenState().apply {
            cinemas.addAll(CinemaViewProvider().values)
        }
    }
    HomeScreen(
        state = state,
        onMovieClick = { _, _ -> },
        onProfileClick = {},
        onCinemaClick = {},
        initialPage = 1,
        onTicketClick = {},
        onTicketsClick = {}
    )
}