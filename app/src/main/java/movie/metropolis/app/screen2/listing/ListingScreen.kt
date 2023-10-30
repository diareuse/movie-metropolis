package movie.metropolis.app.screen2.listing

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.screen.listing.MovieViewProvider
import movie.metropolis.app.screen2.listing.component.PosterColumn
import movie.metropolis.app.screen2.listing.component.RatingBox
import movie.metropolis.app.screen2.setup.component.completelyVisibleItemsInfo
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.layout.plus
import movie.style.rememberImageState
import movie.style.rememberPaletteImageState

@Composable
fun ListingScreen(
    movies: ImmutableList<MovieView>,
    state: LazyStaggeredGridState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) = Box(
    modifier = modifier,
    propagateMinConstraints = true
) {
    var selectedItem by remember {
        mutableIntStateOf(state.layoutInfo.visibleItemsInfo.randomOrNull()?.index ?: 0)
    }
    LaunchedEffect(state.layoutInfo.visibleItemsInfo) {
        val info = state.layoutInfo.completelyVisibleItemsInfo
        if (selectedItem !in info.map { it.index })
            selectedItem = info.randomOrNull()?.index ?: 0
    }
    val background = rememberImageState(movies.getOrNull(selectedItem)?.posterLarge?.url)
    AnimatedContent(
        targetState = background,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) {
        Image(
            modifier = Modifier
                .blur(16.dp)
                .alpha(.35f),
            state = it
        )
    }
    LazyVerticalStaggeredGrid(
        state = state,
        contentPadding = contentPadding + PaddingValues(24.dp),
        columns = StaggeredGridCells.Adaptive(100.dp),
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies) {
            val state = rememberPaletteImageState(url = it.poster?.url ?: it.posterLarge?.url)
            PosterColumn(
                color = state.palette.color,
                contentColor = state.palette.textColor,
                poster = { Image(state) },
                favorite = { Icon(Icons.Rounded.FavoriteBorder, null) },
                name = { Text(it.name) },
                rating = {
                    val rating = it.rating
                    if (rating != null) RatingBox(
                        color = state.palette.color,
                        contentColor = state.palette.textColor,
                        rating = { Text(rating) }
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun ListingScreenPreview() = PreviewLayout {
    val movies = MovieViewProvider().values.toImmutableList()
    ListingScreen(movies, rememberLazyStaggeredGridState())
}

private class ListingScreenParameter : PreviewParameterProvider<ListingScreenParameter.Data> {
    override val values = sequence { yield(Data()) }

    class Data
}