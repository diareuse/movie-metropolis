package movie.metropolis.app.screen.detail

import android.Manifest
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.R
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
import movie.metropolis.app.screen.map
import movie.metropolis.app.screen.mapNotNull
import movie.metropolis.app.screen.onLoading
import movie.metropolis.app.screen.onSuccess
import movie.style.AppButton
import movie.style.AppDropdownIconButton
import movie.style.AppImage
import movie.style.DatePickerRow
import movie.style.EllipsisText
import movie.style.imagePlaceholder
import movie.style.textPlaceholder
import movie.style.theme.Theme
import java.util.Date
import kotlin.random.Random.Default.nextBytes
import kotlin.random.Random.Default.nextInt

@Composable
fun MovieScreen(
    onBackClick: () -> Unit,
    onBookingClick: (String) -> Unit,
    onLinkClick: (String) -> Unit,
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
        onLinkClick = onLinkClick,
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
    onLinkClick: (String) -> Unit,
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
                    val links = detail.map { it.links }.getOrNull()
                    if (links != null) AppDropdownIconButton(painterResource(id = R.drawable.ic_rate)) {
                        val csfd = links.csfd
                        if (csfd != null) DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.csfd)) },
                            onClick = { onLinkClick(csfd) },
                            leadingIcon = { Icon(painterResource(R.drawable.ic_csfd), null) },
                            trailingIcon = { Icon(painterResource(R.drawable.ic_link), null) }
                        )
                        val imdb = links.imdb
                        if (imdb != null) DropdownMenuItem(
                            text = {},
                            onClick = { onLinkClick(imdb) },
                            leadingIcon = { Icon(painterResource(R.drawable.ic_imdb), null) },
                            trailingIcon = { Icon(painterResource(R.drawable.ic_link), null) }
                        )
                        val rottenTomatoes = links.rottenTomatoes
                        if (rottenTomatoes != null) DropdownMenuItem(
                            text = {},
                            onClick = { onLinkClick(rottenTomatoes) },
                            leadingIcon = {
                                Icon(
                                    painterResource(R.drawable.ic_rotten_tomatoes),
                                    null
                                )
                            },
                            trailingIcon = { Icon(painterResource(R.drawable.ic_link), null) }
                        )
                    }
                }
            )
        }
    ) { padding ->
        val surface = Theme.color.container.background
        AppImage(
            modifier = Modifier
                .fillMaxSize()
                .blur(16.dp)
                .alpha(.2f)
                .drawWithContent {
                    drawContent()
                    drawRect(Brush.verticalGradient(listOf(Color.Transparent, surface)))
                },
            url = poster.getOrNull()?.url
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
                    text = detailView?.description ?: stringResource(R.string.detail_error),
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
                        AppButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            onClick = { onLinkClick(it.url) }
                        ) {
                            Text(stringResource(R.string.view_trailer))
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
                text = stringResource(R.string.filters),
                style = Theme.textStyle.headline
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
        Box(
            Modifier
                .shadow(
                    elevation = 32.dp,
                    ambientColor = poster.getOrNull()?.spotColor ?: Color.Black,
                    spotColor = poster.getOrNull()?.spotColor ?: Color.Black
                )
                .clip(Theme.container.poster)
        ) {
            AppImage(
                modifier = Modifier
                    .fillMaxWidth(.3f)
                    .aspectRatio(poster.getOrNull()?.aspectRatio ?: DefaultPosterAspectRatio)
                    .imagePlaceholder(poster.getOrNull() == null)
                    .background(Theme.color.container.background),
                url = poster.getOrNull()?.url
            )
            detail.mapNotNull { it.rating }.onSuccess {
                Text(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(
                            Theme.container.button.copy(
                                topStart = ZeroCornerSize,
                                bottomEnd = ZeroCornerSize
                            )
                        )
                        .background(Theme.color.container.tertiary)
                        .padding(8.dp)
                        .padding(start = 2.dp),
                    text = it
                )
            }
        }
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
            text = detailView?.name ?: "#".repeat(10),
            style = Theme.textStyle.title,
            modifier = Modifier.textPlaceholder(detailView == null)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "%s ??? %s ??? %s".format(
                detailView?.duration ?: "#".repeat(6),
                detailView?.countryOfOrigin ?: "#".repeat(3),
                detailView?.releasedAt ?: "#".repeat(4)
            ),
            style = Theme.textStyle.caption,
            modifier = Modifier.textPlaceholder(detailView == null)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = detailView?.directors?.joinToString() ?: "#".repeat(13),
            style = Theme.textStyle.caption,
            modifier = Modifier.textPlaceholder(detailView == null)
        )
        EllipsisText(
            text = detailView?.cast?.joinToString() ?: "#".repeat(28),
            maxLines = 3,
            style = Theme.textStyle.caption,
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
            onLinkClick = {},
            onFilterClick = {},
            onFavoriteClick = {}
        )
    }
}

data class ImageViewPreview(
    override val aspectRatio: Float,
    override val url: String,
    override val spotColor: Color = Color.Yellow
) : ImageView

class MovieDetailViewProvider : CollectionPreviewParameterProvider<MovieDetailView>(
    listOf(
        MovieDetailViewPreview()
    )
) {

    private data class MovieDetailViewPreview(
        override val id: String = "id",
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
        override val rating: String? = "78%",
        override val links: MovieDetailView.Links? = Links(),
    ) : MovieDetailView

    private data class Links(
        override val imdb: String? = "https://imdb.com/",
        override val csfd: String? = "https://csfd.cz/",
        override val rottenTomatoes: String? = "https://rottentomatoes.com/"
    ) : MovieDetailView.Links

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