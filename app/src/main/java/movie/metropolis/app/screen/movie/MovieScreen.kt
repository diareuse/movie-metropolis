@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package movie.metropolis.app.screen.movie

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.snapping.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.screen.movie.component.ActorRow
import movie.metropolis.app.screen.movie.component.LargeMoviePoster
import movie.metropolis.app.screen.movie.component.LargeRatingBox
import movie.metropolis.app.screen.movie.component.MovieDetailViewProvider
import movie.metropolis.app.screen.movie.component.MovieMetadataRow
import movie.style.BackgroundImage
import movie.style.CollapsingTopAppBar
import movie.style.Image
import movie.style.TwoPaneSurface
import movie.style.action.actionView
import movie.style.layout.PreviewLayout
import movie.style.layout.alignForLargeScreen
import movie.style.rememberPaletteImageState
import movie.style.theme.Theme

@Composable
fun MovieScreen(
    movie: MovieDetailView?,
    onBackClick: () -> Unit,
    onBookClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) = Scaffold(
    modifier = modifier,
    topBar = {
        CollapsingTopAppBar(
            modifier = Modifier.alignForLargeScreen(),
            title = {
                if (movie != null) Text(
                    movie.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(painterResource(R.drawable.ic_back), null)
                }
            },
            scrollBehavior = scrollBehavior
        )
    }
) { padding ->
    val backdrop = rememberPaletteImageState(url = movie?.backdrop?.url)
    BackgroundImage(
        modifier = Modifier.fillMaxSize(),
        state = backdrop
    )
    TwoPaneSurface(
        modifier = Modifier.fillMaxSize(),
        contentPadding = padding,
        connection = scrollBehavior.nestedScrollConnection,
        primaryWeight = .5f,
        primary = {
            val poster = rememberPaletteImageState(url = movie?.poster?.url)
            LargeMoviePoster(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                color = poster.palette.color,
                contentColor = poster.palette.textColor,
                order = { Icon(painterResource(R.drawable.ic_cart), null) },
                rating = {
                    if (movie?.rating != null) LargeRatingBox(
                        color = poster.palette.color
                    ) {
                        Text(movie.rating.orEmpty())
                    }
                },
                onOrderClick = onBookClick
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    state = poster
                )
            }
            Spacer(Modifier.height(24.dp))
        },
        secondary = {
            if (movie != null) Column(
                modifier = Modifier
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MovieMetadataRow(
                    modifier = Modifier
                        .height(90.dp)
                        .padding(horizontal = 24.dp),
                    time = { Text(movie.duration.replace(" ", "\n")) },
                    country = { Text(movie.countryOfOrigin.replace("/", "\n")) },
                    year = { Text(movie.releasedAt) },
                    color = backdrop.palette.color,
                    contentColor = backdrop.palette.textColor
                )
                if (movie.directors.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.directors),
                        style = Theme.textStyle.headline,
                        textAlign = TextAlign.Center
                    )
                    val state = rememberLazyListState()
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        state = state,
                        horizontalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterHorizontally
                        ),
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        flingBehavior = rememberSnapFlingBehavior(state)
                    ) {
                        items(movie.directors) {
                            val state = rememberPaletteImageState(
                                url = it.image,
                                color = backdrop.palette.color,
                                contentColor = backdrop.palette.textColor
                            )
                            ActorRow(
                                image = {
                                    Image(
                                        state = state,
                                        placeholderError = painterResource(R.drawable.ic_person)
                                    )
                                },
                                name = { Text(it.name) },
                                popularity = { Text(if (it.popularity > 0) it.popularity.toString() else "n/a") },
                                movieCount = { Text(if (it.movies.size > 0) it.movies.joinToString { it.name } else "n/a") },
                                color = state.palette.color,
                                onClick = actionView { it.url }
                            )
                        }
                    }
                }
                if (movie.cast.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.cast),
                        style = Theme.textStyle.headline,
                        textAlign = TextAlign.Center
                    )
                    val state = rememberLazyListState()
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        state = state,
                        horizontalArrangement = Arrangement.spacedBy(
                            16.dp,
                            Alignment.CenterHorizontally
                        ),
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        flingBehavior = rememberSnapFlingBehavior(state)
                    ) {
                        items(movie.cast) {
                            val state = rememberPaletteImageState(
                                url = it.image,
                                color = backdrop.palette.color,
                                contentColor = backdrop.palette.textColor
                            )
                            ActorRow(
                                image = {
                                    Image(
                                        state = state,
                                        placeholderError = painterResource(R.drawable.ic_person)
                                    )
                                },
                                name = { Text(it.name) },
                                popularity = { Text(if (it.popularity > 0) it.popularity.toString() else "n/a") },
                                movieCount = { Text(if (it.movies.size > 0) it.movies.joinToString { it.name } else "n/a") },
                                color = state.palette.color,
                                onClick = actionView { it.url }
                            )
                        }
                    }
                }
                if (movie.description.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = stringResource(R.string.description),
                        style = Theme.textStyle.headline,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = movie.description,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, heightDp = 1500)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO, heightDp = 1500)
@Composable
private fun MovieScreenPreview() = PreviewLayout {
    val movie = MovieDetailViewProvider().values.first()
    MovieScreen(movie, {}, {})
}