package movie.metropolis.app.screen.detail

import android.Manifest
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import movie.metropolis.app.feature.location.rememberLocation
import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.listing.DefaultPosterAspectRatio
import movie.metropolis.app.screen.onLoading
import movie.metropolis.app.screen.onSuccess
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
    onVideoClick: (String) -> Unit,
    onPermissionsRequested: suspend (Array<String>) -> Boolean,
    viewModel: MovieViewModel = hiltViewModel()
) {
    val poster by viewModel.poster.collectAsState(initial = Loadable.loading())
    val trailer by viewModel.trailer.collectAsState(initial = Loadable.loading())
    val detail by viewModel.detail.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val showings by viewModel.showings.collectAsState()
    val options by viewModel.options.collectAsState()
    val favorite by viewModel.favorite.collectAsState()
    val location by rememberLocation(onPermissionsRequested)
    val scope = rememberCoroutineScope()
    LaunchedEffect(location) {
        viewModel.location.value = location ?: return@LaunchedEffect
    }
    MovieScreen(
        detail = detail,
        poster = poster,
        trailer = trailer,
        showings = showings,
        options = options,
        isFavorite = favorite.getOrNull() ?: false,
        selectionAvailableStart = startDate.getOrNull(),
        selectedDate = selectedDate,
        hideShowings = viewModel.hideShowings,
        onBackClick = onBackClick,
        onSelectedDateUpdated = { viewModel.selectedDate.value = it },
        onBookingClick = onBookingClick,
        onVideoClick = onVideoClick,
        onFilterClick = viewModel::toggleFilter,
        onFavoriteClick = {
            scope.launch {
                val granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    onPermissionsRequested(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                } else {
                    true
                }
                if (!granted) return@launch
                viewModel.toggleFavorite()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieScreen(
    detail: Loadable<MovieDetailView>,
    poster: Loadable<ImageView>,
    trailer: Loadable<VideoView>,
    showings: Loadable<List<CinemaBookingView>>,
    options: Loadable<Map<Filter.Type, List<Filter>>>,
    isFavorite: Boolean,
    selectionAvailableStart: Date?,
    selectedDate: Date?,
    hideShowings: Boolean,
    onSelectedDateUpdated: (Date) -> Unit,
    onBackClick: () -> Unit,
    onBookingClick: (String) -> Unit,
    onVideoClick: (String) -> Unit,
    onFilterClick: (Filter) -> Unit,
    onFavoriteClick: () -> Unit
) {
    Scaffold(
        topBar = {
            MovieScreenAppBar(
                onBackClick = onBackClick,
                actions = {
                    if (hideShowings) FavoriteButton(
                        isChecked = isFavorite,
                        onClick = onFavoriteClick
                    )
                }
            )
        }
    ) { padding ->
        val surface = MaterialTheme.colorScheme.surface
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .blur(16.dp)
                .alpha(.2f)
                .drawWithContent {
                    drawContent()
                    drawRect(Brush.verticalGradient(listOf(Color.Transparent, surface)))
                },
            model = poster.getOrNull()?.url,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = padding + PaddingValues(bottom = 24.dp)
        ) {
            item(key = "image") {
                MovieMetadata(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    detail = detail,
                    poster = poster
                )
            }
            item(key = "detail") {
                val detailView = detail.getOrNull()
                EllipsisText(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .textPlaceholder(detailView == null),
                    text = detailView?.description ?: "There was an error loading this detail.",
                    maxLines = 5,
                    startState = hideShowings
                )
            }
            if (!hideShowings) {
                MovieDetailShowings(
                    showings = showings,
                    options = options,
                    selectionAvailableStart = selectionAvailableStart,
                    selectedDate = selectedDate,
                    onSelectedDateUpdated = onSelectedDateUpdated,
                    onBookingClick = onBookingClick,
                    onFilterClick = onFilterClick
                )
            } else {
                trailer.onSuccess {
                    item("trailer") {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            onClick = { onVideoClick(it.url) }
                        ) {
                            Text("View trailer")
                        }
                    }
                }
            }
            item {
                Spacer(Modifier.navigationBarsPadding())
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.MovieDetailShowings(
    showings: Loadable<List<CinemaBookingView>>,
    options: Loadable<Map<Filter.Type, List<Filter>>>,
    selectionAvailableStart: Date?,
    selectedDate: Date?,
    onSelectedDateUpdated: (Date) -> Unit,
    onBookingClick: (String) -> Unit,
    onFilterClick: (Filter) -> Unit
) {
    item(key = "divider") {
        Divider(
            Modifier
                .padding(horizontal = 32.dp)
                .animateItemPlacement()
        )
    }
    if (selectionAvailableStart != null && selectedDate != null) item("picker") {
        DatePickerRow(
            modifier = Modifier.animateItemPlacement(),
            start = selectionAvailableStart,
            selected = selectedDate,
            onClickDate = onSelectedDateUpdated
        )
    }
    options.onSuccess { filters ->
        item("filters-title") {
            Text(
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(horizontal = 24.dp),
                text = "Filters",
                style = MaterialTheme.typography.titleMedium
            )
        }
        item("filters") {
            Column(
                modifier = Modifier.animateItemPlacement(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterRow(
                    filters = filters[Filter.Type.Language].orEmpty(),
                    onFilterToggle = onFilterClick,
                    contentPadding = PaddingValues(horizontal = 24.dp)
                )
                FilterRow(
                    filters = filters[Filter.Type.Projection].orEmpty(),
                    onFilterToggle = onFilterClick,
                    contentPadding = PaddingValues(horizontal = 24.dp)
                )
            }
        }
        item("filters-divider") {
            Divider(Modifier.padding(horizontal = 32.dp))
        }
    }
    showings.onSuccess { showings ->
        items(showings, key = { it.cinema.id }) {
            ShowingItem(
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(horizontal = 24.dp),
                title = it.cinema.name,
                showings = it.availability,
                onClick = onBookingClick
            )
        }
    }.onLoading {
        items(2) {
            ShowingItem(
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(horizontal = 24.dp)
            )
        }
    }
}

@Composable
fun MovieMetadata(
    detail: Loadable<MovieDetailView>,
    poster: Loadable<ImageView>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        AsyncImage(
            modifier = Modifier
                .shadow(32.dp)
                .fillMaxWidth(.3f)
                .aspectRatio(poster.getOrNull()?.aspectRatio ?: DefaultPosterAspectRatio)
                .clip(MaterialTheme.shapes.medium)
                .imagePlaceholder(poster.getOrNull() == null)
                .background(MaterialTheme.colorScheme.surface),
            model = poster.getOrNull()?.url,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        DetailPosterRow(detail = detail)
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
    Column(modifier = modifier) {
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
            options = Loadable.loading(),
            selectedDate = Date(),
            onBackClick = {},
            isFavorite = true,
            showings = Loadable.success(showings),
            hideShowings = false,
            selectionAvailableStart = Date(),
            onSelectedDateUpdated = {},
            onBookingClick = {},
            onVideoClick = {},
            onFilterClick = {},
            onFavoriteClick = {}
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
        override val availability: Map<AvailabilityView.Type, List<AvailabilityView>> = mapOf(
            LanguageAndTypePreview() to List(nextInt(1, 5)) { AvailabilityPreview() }
        )
    ) : CinemaBookingView

    private data class LanguageAndTypePreview(
        override val language: String = listOf(
            "English (Czech)",
            "Czech",
            "Hungarian (Czech)"
        ).random(),
        override val types: List<String> = listOf(
            listOf("2D"),
            listOf("3D"),
            listOf("3D", "4DX"),
            listOf("2D", "VIP")
        ).random()
    ) : AvailabilityView.Type

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
    ) : AvailabilityView

}