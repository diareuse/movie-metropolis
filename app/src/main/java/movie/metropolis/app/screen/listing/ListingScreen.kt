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
import movie.metropolis.app.screen.booking.component.rememberMultiChildPagerState
import movie.metropolis.app.screen.listing.component.PosterActionColumn
import movie.metropolis.app.screen.listing.component.PosterColumn
import movie.metropolis.app.screen.listing.component.PromotionColumn
import movie.metropolis.app.screen.listing.component.PromotionHorizontalPager
import movie.metropolis.app.screen.listing.component.RatingBox
import movie.metropolis.app.screen.movie.component.MovieViewProvider
import movie.metropolis.app.util.rememberStoreable
import movie.metropolis.app.util.rememberVisibleItemAsState
import movie.style.BackgroundImage
import movie.style.DialogClone
import movie.style.Image
import movie.style.OverlayContainer
import movie.style.OverlayScope
import movie.style.action.actionView
import movie.style.layout.PreviewLayout
import movie.style.layout.alignForLargeScreen
import movie.style.layout.plus
import movie.style.rememberDialogCloneState
import movie.style.rememberImageState
import movie.style.rememberPaletteImageState

@Composable
fun ListingScreen(
    promotions: ImmutableList<MovieView>,
    movies: ImmutableList<MovieView>,
    state: LazyStaggeredGridState,
    overlay: OverlayScope,
    onClick: (MovieView) -> Unit,
    onFavoriteClick: (MovieView) -> Unit,
    onHideClick: (MovieView) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onMoreClick: (() -> Unit)? = null,
    connection: NestedScrollConnection? = null
) = Box(
    modifier = modifier,
    propagateMinConstraints = true
) {
    val selectedItem by state.rememberVisibleItemAsState()
    val scope = rememberCoroutineScope()
    BackgroundImage(
        modifier = Modifier.fillMaxSize(),
        state = rememberImageState(movies.getOrNull(selectedItem)?.posterLarge?.url)
    )
    var zoom by rememberStoreable(key = "listing-zoom", default = 100f)
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
        contentPadding = contentPadding + PaddingValues(24.dp),
        columns = StaggeredGridCells.Adaptive(zoom.dp),
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            val (state, indicator) = rememberMultiChildPagerState(childCount = 1) { promotions.size }
            PromotionHorizontalPager(
                modifier = Modifier.animateItemPlacement(),
                state = state,
                indicatorState = indicator
            ) {
                val it = promotions[it]
                val state = rememberPaletteImageState(it.poster?.url)
                val dialogState = overlay.rememberDialogCloneState()
                DialogClone(
                    state = dialogState,
                    expansion = {
                        PosterActionColumn(
                            modifier = Modifier.padding(vertical = 16.dp),
                            favorite = it.favorite,
                            color = state.palette.color,
                            contentColor = state.palette.textColor,
                            onFavoriteClick = {
                                scope.launch {
                                    dialogState.close()
                                    onFavoriteClick(it)
                                }
                            },
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
                    PromotionColumn(
                        color = state.palette.color,
                        contentColor = state.palette.textColor,
                        name = { Text(it.name) },
                        rating = {
                            val rating = it.rating
                            if (rating != null) RatingBox(
                                color = state.palette.color,
                                contentColor = state.palette.textColor,
                                rating = { Text(rating) },
                                offset = PaddingValues(start = 4.dp, bottom = 4.dp)
                            )
                        },
                        poster = { Image(state, alignment = Alignment.TopCenter) },
                        action = {
                            val icon = when (it.favorite) {
                                true -> painterResource(R.drawable.ic_favorite_fill)
                                else -> painterResource(R.drawable.ic_favorite_outline)
                            }
                            Icon(icon, null)
                        },
                        onClick = { onClick(it) },
                        onActionClick = { onFavoriteClick(it) },
                        onLongClick = { scope.launch { dialogState.open() } }
                    )
                }
            }
        }
        items(movies, key = { it.id }) {
            val state = rememberPaletteImageState(url = it.poster?.url ?: it.posterLarge?.url)
            val dialogState = overlay.rememberDialogCloneState()
            DialogClone(
                state = dialogState,
                modifier = Modifier.animateItemPlacement(),
                expansion = {
                    PosterActionColumn(
                        modifier = Modifier.padding(vertical = 16.dp),
                        favorite = it.favorite,
                        color = state.palette.color,
                        contentColor = state.palette.textColor,
                        onFavoriteClick = {
                            scope.launch {
                                dialogState.close()
                                onFavoriteClick(it)
                            }
                        },
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
                    contentColor = state.palette.textColor,
                    poster = { Image(state) },
                    favorite = {
                        val icon = when (it.favorite) {
                            true -> painterResource(R.drawable.ic_favorite_fill)
                            else -> painterResource(R.drawable.ic_favorite_outline)
                        }
                        Icon(icon, null)
                    },
                    name = { Text(it.name) },
                    rating = {
                        val rating = it.rating
                        if (rating != null) RatingBox(
                            color = state.palette.color,
                            contentColor = state.palette.textColor,
                            rating = { Text(rating) }
                        )
                    },
                    onClick = { onClick(it) },
                    onActionClick = { onFavoriteClick(it) },
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
    OverlayContainer {
        ListingScreen(
            promotions = promotions,
            movies = movies,
            state = rememberLazyStaggeredGridState(),
            onClick = {},
            onFavoriteClick = {},
            onHideClick = {},
            overlay = this
        )
    }
}