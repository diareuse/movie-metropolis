@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen.listing

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import movie.metropolis.app.R
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.screen.listing.component.PosterActionColumn
import movie.metropolis.app.screen.listing.component.PosterColumn
import movie.metropolis.app.screen.listing.component.RatingBox
import movie.metropolis.app.screen.movie.component.MovieViewProvider
import movie.metropolis.app.util.rememberStoreable
import movie.style.Image
import movie.style.OverlayState
import movie.style.PopOutBox
import movie.style.action.actionView
import movie.style.layout.PreviewLayout
import movie.style.layout.alignForLargeScreen
import movie.style.layout.plus
import movie.style.rememberPaletteImageState
import movie.style.rememberPopOutState

@Composable
fun ListingScreen(
    promotions: ImmutableList<MovieView>,
    movies: ImmutableList<MovieView>,
    state: LazyStaggeredGridState,
    overlay: OverlayState,
    onClick: (MovieView) -> Unit,
    onHideClick: (MovieView) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onMoreClick: (() -> Unit)? = null,
    connection: NestedScrollConnection? = null
) = Box(
    modifier = modifier,
    propagateMinConstraints = true
) {
    val scope = rememberCoroutineScope()
    var zoom by rememberStoreable(key = "listing-zoom", default = 75f)
    val gridModifier = if (connection != null) Modifier.nestedScroll(connection) else Modifier
    LazyVerticalStaggeredGrid(
        modifier = gridModifier
            .alignForLargeScreen()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoomFactor, _ ->
                    zoom = (zoom * zoomFactor).coerceIn(75f, 200f)
                }
            },
        state = state,
        contentPadding = contentPadding + PaddingValues(12.dp),
        columns = StaggeredGridCells.Adaptive(zoom.dp),
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies, key = { it.id }) {
            val state = rememberPaletteImageState(url = it.poster?.url ?: it.posterLarge?.url)
            val dialogState = overlay.rememberPopOutState()
            PopOutBox(
                state = dialogState,
                modifier = Modifier.animateItem(),
                expansion = {
                    PosterActionColumn(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = state.palette.color,
                        contentColor = state.palette.textColor,
                        onHideClick = {
                            scope.launch {
                                dialogState.close()
                                onHideClick(it)
                            }
                        },
                        onOpenClick = actionView {
                            scope.launch { dialogState.close() }
                            it.url
                        }
                    )
                }
            ) {
                PosterColumn(
                    color = state.palette.color,
                    poster = { Image(state) },
                    name = { Text(it.name) },
                    rating = {
                        val rating = it.rating
                        if (rating != null) RatingBox(
                            color = state.palette.color,
                            rating = { Text(rating) }
                        )
                    },
                    onClick = { onClick(it) },
                    onLongClick = { scope.launch { dialogState.open() } }
                )
            }
        }
        if (onMoreClick != null && movies.isNotEmpty()) item(
            span = StaggeredGridItemSpan.FullLine
        ) {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onMoreClick
            ) {
                Text(stringResource(R.string.view_upcoming))
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun ListingScreenPreview() = PreviewLayout {
    val content = MovieViewProvider().values
    val movies = content.toImmutableList()
    val promotions = content.shuffled().take(3).toImmutableList()
    ListingScreen(
        promotions = promotions,
        movies = movies,
        state = rememberLazyStaggeredGridState(),
        onClick = {},
        onHideClick = {},
        overlay = remember { OverlayState() }
    )
}