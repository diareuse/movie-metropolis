package movie.metropolis.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
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
import movie.metropolis.app.ui.home.component.TicketBox
import movie.style.Image
import movie.style.layout.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout
import movie.style.rememberImageState
import movie.style.rememberPaletteImageState
import movie.style.util.pc

@OptIn(ExperimentalMaterial3Api::class)
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
            Image(
                modifier = Modifier
                    .hazeSource(haze)
                    .alpha(.5f)
                    .blur(8.dp),
                state = rememberImageState(state.current.firstOrNull()?.poster?.url)
            )
            LazyVerticalStaggeredGrid(
                modifier = Modifier.padding(horizontal = 1.pc),
                columns = StaggeredGridCells.Adaptive(150.dp),
                contentPadding = padding,
                horizontalArrangement = Arrangement.spacedBy(1.pc),
                verticalItemSpacing = 1.pc
            ) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    val user = state.profile.user
                    val membership = state.profile.membership
                    if (user != null) LoyaltyCard(
                        modifier = Modifier.fillMaxWidth(),
                        haze = haze,
                        logo = {
                            androidx.compose.foundation.Image(
                                painterResource(R.drawable.ic_logo_cinemacity),
                                null
                            )
                        },
                        title = {
                            if (membership != null && membership.isExpired.not()) Text("Premium")
                            else Text("Expired")
                        },
                        name = { Text("%s %s".format(user.firstName, user.lastName)) },
                        number = { Text(membership?.cardNumber ?: "") }
                    )
                }
                item(span = StaggeredGridItemSpan.FullLine) {
                    Text("Tickets", style = MaterialTheme.typography.titleMedium)
                }
                item(span = StaggeredGridItemSpan.FullLine) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy((-2).pc)
                    ) {
                        itemsIndexed(state.tickets.tickets) { index, it ->
                            val image = rememberPaletteImageState(it.movie.poster?.url)
                            TicketBox(
                                modifier = Modifier.zIndex(state.tickets.tickets.size - index * 1f),
                                expired = it.expired,
                                onClick = { onTicketClick(it.id) },
                                date = { Text(it.date) },
                                time = { Text(it.time) },
                                poster = { Image(image) },
                                contentColor = image.palette.textColor,
                                color = image.palette.color
                            )
                        }
                    }
                }
                item(span = StaggeredGridItemSpan.FullLine) {
                    Text("Currently showing", style = MaterialTheme.typography.titleMedium)
                }
                items(state.current) {
                    MovieBox(
                        onClick = { onMovieClick(it.id, false) },
                        aspectRatio = it.poster?.aspectRatio ?: DefaultPosterAspectRatio,
                        name = { Text(it.name) },
                        poster = { Image(rememberImageState(it.poster?.url)) },
                        rating = {
                            val r = it.rating
                            if (r != null) Text(r)
                        },
                        category = {}
                    )
                }
                item(span = StaggeredGridItemSpan.FullLine) {
                    Text("Upcoming", style = MaterialTheme.typography.titleMedium)
                }
                items(state.upcoming) {
                    MovieBox(
                        onClick = { onMovieClick(it.id, true) },
                        aspectRatio = it.poster?.aspectRatio ?: DefaultPosterAspectRatio,
                        name = { Text(it.name) },
                        poster = { Image(rememberImageState(it.poster?.url)) },
                        rating = {
                            val r = it.rating
                            if (r != null) Text(r)
                        },
                        category = {}
                    )
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