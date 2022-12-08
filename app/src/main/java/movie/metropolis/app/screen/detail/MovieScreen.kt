package movie.metropolis.app.screen.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import movie.metropolis.app.feature.location.rememberLocation
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.listing.DefaultPosterAspectRatio
import movie.metropolis.app.theme.Theme
import movie.metropolis.app.view.EllipsisText
import movie.metropolis.app.view.imagePlaceholder
import movie.metropolis.app.view.textPlaceholder
import java.util.Date
import kotlin.random.Random.Default.nextBytes
import kotlin.random.Random.Default.nextInt

@Composable
fun MovieScreen(
    onBackClick: () -> Unit,
    onBookingClick: (String) -> Unit,
    onPermissionsRequested: suspend (Array<String>) -> Boolean,
    viewModel: MovieViewModel = hiltViewModel()
) {
    val poster by viewModel.poster.collectAsState(initial = Loadable.loading())
    val trailer by viewModel.trailer.collectAsState(initial = Loadable.loading())
    val detail by viewModel.detail.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val showings by viewModel.showings.collectAsState()
    val location by rememberLocation(onPermissionsRequested)
    SideEffect {
        viewModel.location.value = location
    }
    MovieScreen(
        detail = detail,
        poster = poster,
        trailer = trailer,
        showings = showings,
        selectionAvailableStart = startDate.getOrNull(),
        selectedDate = selectedDate,
        onBackClick = onBackClick,
        onSelectedDateUpdated = { viewModel.selectedDate.value = it },
        onBookingClick = onBookingClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun MovieScreen(
    detail: Loadable<MovieDetailView>,
    poster: Loadable<ImageView>,
    trailer: Loadable<VideoView>,
    showings: Loadable<List<CinemaBookingView>>,
    selectionAvailableStart: Date?,
    selectedDate: Date?,
    onSelectedDateUpdated: (Date) -> Unit,
    onBackClick: () -> Unit,
    onBookingClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            MovieScreenAppBar(
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        SideEffect { padding } // just to suppress lint
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                AsyncImage(
                    modifier = Modifier
                        .shadow(32.dp)
                        .fillMaxWidth()
                        .aspectRatio(poster.getOrNull()?.aspectRatio ?: DefaultPosterAspectRatio)
                        .clip(
                            MaterialTheme.shapes.large.copy(
                                topStart = CornerSize(0.dp),
                                topEnd = CornerSize(0.dp)
                            )
                        )
                        .imagePlaceholder(poster.getOrNull() == null)
                        .background(MaterialTheme.colorScheme.surface)
                        .animateItemPlacement(),
                    model = poster.getOrNull()?.url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            item {
                DetailPosterRow(modifier = Modifier.animateItemPlacement(), detail = detail)
            }
            item {
                Divider(
                    Modifier
                        .padding(horizontal = 32.dp)
                        .animateItemPlacement()
                )
            }
            if (selectionAvailableStart != null && selectedDate != null) item {
                DatePickerRow(
                    modifier = Modifier.animateItemPlacement(),
                    start = selectionAvailableStart,
                    selected = selectedDate,
                    onClickDate = onSelectedDateUpdated
                )
            }
            items(showings.getOrNull().orEmpty(), key = { it.cinema.id }) {
                ShowingItem(
                    modifier = Modifier
                        .animateItemPlacement()
                        .padding(horizontal = 24.dp),
                    title = it.cinema.name,
                    showings = it.availability,
                    onClick = onBookingClick
                )
            }
            item { Spacer(Modifier.navigationBarsPadding()) }
        }
    }
}

@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val dir = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(dir) + other.calculateStartPadding(dir),
        top = calculateTopPadding() + other.calculateTopPadding(),
        end = calculateEndPadding(dir) + other.calculateEndPadding(dir),
        bottom = calculateBottomPadding() + other.calculateBottomPadding(),
    )
}

@Composable
fun DetailPosterRow(
    detail: Loadable<MovieDetailView>,
    modifier: Modifier = Modifier,
) {
    val detailView = detail.getOrNull()
    Column(modifier = modifier.padding(horizontal = 24.dp)) {
        Text(
            text = detailView?.name ?: "Movie Name",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.textPlaceholder(detailView == null)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "%s • %s • %s".format(
                detailView?.duration ?: "1h 30m",
                detailView?.countryOfOrigin ?: "USA",
                detailView?.releasedAt ?: "2022"
            ),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.textPlaceholder(detailView == null)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = detailView?.directors?.joinToString() ?: "Foobar Boobar",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.textPlaceholder(detailView == null)
        )
        EllipsisText(
            text = detailView?.cast?.joinToString() ?: "Foobar Boobar, Foobar Boobar",
            maxLines = 3,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.textPlaceholder(detailView == null)
        )
        Spacer(Modifier.height(24.dp))
        EllipsisText(
            text = detailView?.description ?: "There was an error loading this detail.",
            maxLines = 5,
            modifier = Modifier.textPlaceholder(detailView == null)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview(
    @PreviewParameter(MovieDetailViewProvider::class)
    detail: MovieDetailView,
    showings: List<CinemaBookingView> = CinemaBookingViewProvider().values.toList()
) {
    Theme {
        MovieScreen(
            detail = Loadable.success(detail),
            poster = Loadable.success(
                ImageViewPreview(
                    url = "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5376O2R-lg.jpg",
                    aspectRatio = DefaultPosterAspectRatio
                )
            ),
            trailer = Loadable.loading(),
            selectedDate = Date(),
            onBackClick = {},
            showings = Loadable.success(showings),
            selectionAvailableStart = Date(),
            onSelectedDateUpdated = {},
            onBookingClick = {}
        )
    }
}

data class ImageViewPreview(
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
        override val availableFrom: String = "23. 4. 2022",
        override val poster: ImageView? = null,
        override val trailer: VideoView? = null,
    ) : MovieDetailView

}

class CinemaBookingViewProvider :
    CollectionPreviewParameterProvider<CinemaBookingView>(
        listOf(
            CinemaBookingViewPreview(),
            CinemaBookingViewPreview()
        )
    ) {

    private data class CinemaBookingViewPreview(
        override val cinema: CinemaView = CinemaViewPreview(),
        override val availability: Map<CinemaBookingView.LanguageAndType, List<CinemaBookingView.Availability>> = mapOf(
            LanguageAndTypePreview() to List(nextInt(1, 5)) { AvailabilityPreview() }
        )
    ) : CinemaBookingView

    private data class LanguageAndTypePreview(
        override val language: String = listOf(
            "English (Czech)",
            "Czech",
            "Hungarian (Czech)"
        ).random(),
        override val type: String = listOf("2D", "3D", "3D | 4DX", "2D | VIP").random()
    ) : CinemaBookingView.LanguageAndType

    private data class CinemaViewPreview(
        override val id: String = String(nextBytes(10)),
        override val name: String = "Some Cinema",
        override val address: List<String> = listOf("Foo bar 12/3"),
        override val city: String = "City",
        override val distance: String? = null
    ) : CinemaView

    private data class AvailabilityPreview(
        override val id: String = String(nextBytes(10)),
        override val url: String = "https://foo.bar",
        override val startsAt: String = "12:10",
        override val isEnabled: Boolean = true
    ) : CinemaBookingView.Availability

}