package movie.metropolis.app.screen.detail

import android.Manifest
import android.os.Build
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.ActivityActions
import movie.metropolis.app.LocalActivityActions
import movie.metropolis.app.R
import movie.metropolis.app.feature.location.rememberLocation
import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.map
import movie.metropolis.app.presentation.onEmpty
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.cinema.component.MovieShowingItemEmpty
import movie.metropolis.app.screen.detail.component.FavoriteButton
import movie.metropolis.app.screen.detail.component.FilterRow
import movie.metropolis.app.screen.detail.component.MovieMetadata
import movie.metropolis.app.screen.detail.component.MovieScreenAppBar
import movie.metropolis.app.screen.detail.component.ShowingItem
import movie.metropolis.app.screen.listing.component.DefaultPosterAspectRatio
import movie.style.AppButton
import movie.style.AppDropdownIconButton
import movie.style.AppImage
import movie.style.DatePickerRow
import movie.style.EllipsisText
import movie.style.modifier.overlay
import movie.style.state.ImmutableDate
import movie.style.state.ImmutableDate.Companion.immutable
import movie.style.state.ImmutableList.Companion.immutable
import movie.style.state.ImmutableMap.Companion.immutable
import movie.style.textPlaceholder
import movie.style.theme.Theme
import java.util.Date
import kotlin.random.Random.Default.nextBytes
import kotlin.random.Random.Default.nextInt

@Composable
fun MovieScreen(
    onBackClick: () -> Unit,
    onBookingClick: (String) -> Unit,
    viewModel: MovieViewModel = hiltViewModel(),
    actions: ActivityActions = LocalActivityActions.current
) {
    val poster by viewModel.poster.collectAsState(initial = Loadable.loading())
    val trailer by viewModel.trailer.collectAsState(initial = Loadable.loading())
    val detail by viewModel.detail.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val showings by viewModel.showings.collectAsState()
    val options by viewModel.options.collectAsState()
    val favorite by viewModel.favorite.collectAsState()
    val showFavorite by viewModel.showFavorite.collectAsState()
    val location by rememberLocation()
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
        selectionAvailableStart = startDate.getOrNull()?.immutable(),
        selectedDate = selectedDate?.immutable(),
        hideShowings = viewModel.hideShowings,
        showFavorite = showFavorite,
        onBackClick = onBackClick,
        onSelectedDateUpdated = { viewModel.selectedDate.value = it },
        onBookingClick = onBookingClick,
        onFilterClick = viewModel::toggleFilter,
        onFavoriteClick = {
            scope.launch {
                val granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    actions.requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
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
    selectionAvailableStart: ImmutableDate?,
    selectedDate: ImmutableDate?,
    hideShowings: Boolean,
    showFavorite: Boolean,
    onSelectedDateUpdated: (Date) -> Unit,
    onBackClick: () -> Unit,
    onBookingClick: (String) -> Unit,
    onFilterClick: (Filter) -> Unit,
    onFavoriteClick: () -> Unit,
    actions: ActivityActions = LocalActivityActions.current
) {
    Scaffold(
        topBar = {
            MovieScreenAppBar(
                onBackClick = onBackClick,
                actions = {
                    if (showFavorite) FavoriteButton(
                        isChecked = isFavorite,
                        onClick = onFavoriteClick
                    )
                    val links = detail.map { it.links }.getOrNull()
                    if (links != null) AppDropdownIconButton(painterResource(id = R.drawable.ic_rate)) {
                        val csfd = links.csfd
                        if (csfd != null) DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.csfd)) },
                            onClick = { actions.actionView(csfd) },
                            leadingIcon = { Icon(painterResource(R.drawable.ic_csfd), null) },
                            trailingIcon = { Icon(painterResource(R.drawable.ic_link), null) }
                        )
                        val imdb = links.imdb
                        if (imdb != null) DropdownMenuItem(
                            text = {},
                            onClick = { actions.actionView(imdb) },
                            leadingIcon = { Icon(painterResource(R.drawable.ic_imdb), null) },
                            trailingIcon = { Icon(painterResource(R.drawable.ic_link), null) }
                        )
                        val rottenTomatoes = links.rottenTomatoes
                        if (rottenTomatoes != null) DropdownMenuItem(
                            text = {},
                            onClick = { actions.actionView(rottenTomatoes) },
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
        AppImage(
            modifier = Modifier
                .fillMaxSize()
                .blur(16.dp)
                .alpha(.2f)
                .overlay(),
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
                            onClick = { actions.actionView(it.url) }
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
    selectionAvailableStart: ImmutableDate?,
    selectedDate: ImmutableDate?,
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
                    filters = filters[Filter.Type.Language].orEmpty().immutable(),
                    onFilterToggle = onFilterClick,
                    contentPadding = PaddingValues(horizontal = 24.dp)
                )
                FilterRow(
                    filters = filters[Filter.Type.Projection].orEmpty().immutable(),
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
                showings = it.availability.immutable(),
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
    }.onEmpty {
        item {
            MovieShowingItemEmpty(
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(horizontal = 24.dp)
            )
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
            selectedDate = Date().immutable(),
            onBackClick = {},
            isFavorite = true,
            showings = Loadable.success(showings),
            hideShowings = false,
            showFavorite = false,
            selectionAvailableStart = Date().immutable(),
            onSelectedDateUpdated = {},
            onBookingClick = {},
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
        override val address: String = "Foo bar 12/3",
        override val city: String = "City",
        override val distance: String? = null,
        override val image: String? = null
    ) : CinemaView

    private data class AvailabilityPreview(
        override val id: String = String(nextBytes(10)),
        override val url: String = "https://foo.bar",
        override val startsAt: String = "12:10",
        override val isEnabled: Boolean = true
    ) : AvailabilityView

}