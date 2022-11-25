package movie.metropolis.app.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import movie.metropolis.app.R
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.listing.MoviePoster
import movie.metropolis.app.theme.Theme
import movie.metropolis.app.view.EllipsisText
import movie.metropolis.app.view.placeholder

@Composable
fun MovieScreen(
    onBackClick: () -> Unit,
    viewModel: MovieViewModel = hiltViewModel()
) {
    val poster by viewModel.poster.collectAsState(initial = Loadable.loading())
    val trailer by viewModel.trailer.collectAsState(initial = Loadable.loading())
    val detail by viewModel.detail.collectAsState()
    MovieScreen(
        detail = detail,
        poster = poster,
        trailer = trailer,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieScreen(
    detail: Loadable<MovieDetailView>,
    poster: Loadable<ImageView>,
    trailer: Loadable<VideoView>,
    onBackClick: () -> Unit
) {
    MovieScreenLayout(
        appbar = { scrollBehavior ->
            MovieScreenAppBar(
                scrollBehavior = scrollBehavior,
                onBackClick = onBackClick
            )
        },
        background = {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = poster.getOrNull()?.url,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    ) { scrollBehavior, padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(24.dp)
        ) {
            val detail = detail.getOrNull()
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                val poster = poster.getOrNull()
                Box(
                    modifier = Modifier
                        .shadow(24.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surface)
                        .height(225.dp)
                        .aspectRatio(poster?.aspectRatio ?: 0.67f)
                ) {
                    MoviePoster(url = poster?.url)
                    val trailer = trailer.getOrNull()
                    if (trailer != null) Image(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable(role = Role.Button) { },
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
                Column {
                    Text(
                        text = detail?.name ?: "Movie Name",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.placeholder(detail == null)
                    )
                    Text(
                        text = detail?.nameOriginal ?: "Original Movie name",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.placeholder(detail == null)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = detail?.duration ?: "1h 30m",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.placeholder(detail == null)
                    )
                    Text(
                        text = "%s (%s)".format(
                            detail?.countryOfOrigin ?: "USA",
                            detail?.releasedAt ?: "2022"
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.placeholder(detail == null)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = detail?.directors?.joinToString() ?: "Foobar Boobar",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.placeholder(detail == null)
                    )
                    EllipsisText(
                        text = detail?.cast?.joinToString()
                            ?: "Foobar Boobar, Foobar Boobar, Foobar Boobar, Foobar Boobar",
                        maxLines = 3,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.placeholder(detail == null)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            EllipsisText(
                text = detail?.description ?: "Description to be loaded here",
                maxLines = 5,
                modifier = Modifier.placeholder(detail == null)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview(
    @PreviewParameter(MovieDetailViewProvider::class)
    detail: MovieDetailView
) {
    Theme {
        MovieScreen(
            detail = Loadable.success(detail),
            poster = Loadable.success(
                ImageViewPreview(
                    url = "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5376O2R-lg.jpg",
                    aspectRatio = 0.67f
                )
            ),
            trailer = Loadable.loading(),
            onBackClick = {}
        )
    }
}

private data class ImageViewPreview(
    override val aspectRatio: Float,
    override val url: String
) : ImageView

class MovieDetailViewProvider : CollectionPreviewParameterProvider<MovieDetailView>(
    listOf(
        MovieDetailViewPreview()
    )
) {

    private data class MovieDetailViewPreview(
        override val name: String = listOf(
            "Black Adam",
            "Black Panther: Wakanda Forever",
            "Strange World",
            "The Fabelmans"
        ).random(),
        override val nameOriginal: String = listOf(
            "Black Adam",
            "Black Panther: Wakanda Forever",
            "Strange World",
            "The Fabelmans"
        ).random(),
        override val releasedAt: String = "2022",
        override val duration: String = "1h 34m",
        override val countryOfOrigin: String = "USA",
        override val cast: List<String> = listOf("Foo Bar", "Bar Foo-Foo"),
        override val directors: List<String> = listOf("Foofoo Barbar"),
        override val description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam vel finibus augue. Praesent porta, nibh rhoncus ultrices tempus, metus lacus facilisis lorem, id venenatis nisl mi non massa. Vestibulum eu ipsum leo. Mauris et sagittis tortor. Fusce dictum cursus quam in ornare. Curabitur posuere ligula sem, et tincidunt lorem commodo vitae. Fusce mollis elementum dignissim. Fusce suscipit massa maximus metus gravida, vitae posuere sem semper. Nullam auctor venenatis elementum. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Phasellus nibh sem, volutpat nec egestas convallis, ultricies quis massa. Duis quis placerat neque, eu bibendum arcu. ",
        override val availableFrom: String = "23. 4. 2022"
    ) : MovieDetailView

}