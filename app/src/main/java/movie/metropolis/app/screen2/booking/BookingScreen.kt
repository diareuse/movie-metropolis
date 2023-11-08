@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen2.booking

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.pager.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.LazyTimeView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.model.ShowingTag
import movie.metropolis.app.model.SpecificTimeView
import movie.metropolis.app.model.TimeView
import movie.metropolis.app.screen.cinema.component.CinemaViewParameter
import movie.metropolis.app.screen.listing.MovieViewProvider
import movie.metropolis.app.screen2.booking.component.CinemaTimeContainer
import movie.metropolis.app.screen2.booking.component.DateBox
import movie.metropolis.app.screen2.booking.component.MovieTimeContainer
import movie.metropolis.app.screen2.booking.component.ProjectionTypeRow
import movie.metropolis.app.screen2.booking.component.ProjectionTypeRowDefaults
import movie.metropolis.app.screen2.booking.component.TimeButton
import movie.metropolis.app.screen2.booking.component.rememberMultiChildPagerState
import movie.metropolis.app.util.interpolatePage
import movie.style.AppIconButton
import movie.style.BackgroundImage
import movie.style.CollapsingTopAppBar
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.layout.plus
import movie.style.modifier.VerticalGravity
import movie.style.modifier.verticalOverlay
import movie.style.rememberImageState
import movie.style.rememberPaletteImageState
import java.text.DateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextInt

@Composable
fun BookingScreen(
    poster: String?,
    title: String,
    items: ImmutableList<LazyTimeView>,
    onBackClick: () -> Unit,
    onTimeClick: (SpecificTimeView) -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) = Scaffold(
    modifier = modifier.fillMaxSize(),
    contentWindowInsets = WindowInsets(0),
    topBar = {
        CollapsingTopAppBar(
            scrollBehavior = scrollBehavior,
            title = { Text(title) },
            navigationIcon = {
                AppIconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, null)
                }
            }
        )
    }
) { padding ->
    val locale = remember { Locale.getDefault() }
    val (statePage, stateDate) = rememberMultiChildPagerState(childCount = 1) {
        items.size
    }
    BackgroundImage(
        modifier = Modifier.fillMaxSize(),
        state = rememberImageState(url = poster)
    )
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = padding.calculateTopPadding()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = stateDate,
            contentPadding = PaddingValues(horizontal = 128.dp),
            pageSpacing = 16.dp,
            userScrollEnabled = false
        ) {
            val view = items[it]
            DateBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .interpolatePage(
                        state = stateDate,
                        page = it,
                        offset = DpOffset.Zero,
                        blur = 8.dp,
                        alphaRange = .8f..1f,
                        rotationZ = 0f,
                        rotationY = 0f
                    ),
                active = stateDate.currentPage == it
            ) {
                Text(view.dateString)
            }
        }
        HorizontalPager(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalOverlay(48.dp, VerticalGravity.Top),
            state = statePage,
            pageSpacing = 32.dp,
            beyondBoundsPageCount = 1,
            pageNestedScrollConnection = scrollBehavior.nestedScrollConnection,
            verticalAlignment = Alignment.Top
        ) {
            val page by items[it].content.collectAsState(emptyList())
            val bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            LazyVerticalStaggeredGrid(
                modifier = Modifier.interpolatePage(
                    state = statePage,
                    page = it,
                    offset = DpOffset.Zero,
                    rotationZ = 0f,
                    rotationY = 0f
                ),
                columns = StaggeredGridCells.Adaptive(100.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalItemSpacing = 16.dp,
                contentPadding = PaddingValues(
                    bottom = padding.calculateBottomPadding() + bottom,
                    top = 16.dp
                ) + PaddingValues(24.dp)
            ) {
                for (view in page) {
                    if (view.times.isEmpty()) continue
                    when (view) {
                        is TimeView.Cinema -> item(
                            key = view.cinema.id,
                            span = StaggeredGridItemSpan.FullLine
                        ) {
                            val state = rememberPaletteImageState(url = view.cinema.image)
                            CinemaTimeContainer(
                                modifier = Modifier.animateItemPlacement(),
                                color = state.palette.color,
                                contentColor = state.palette.textColor,
                                name = { Text(view.cinema.name) }
                            ) {
                                Image(state)
                            }
                        }

                        is TimeView.Movie -> item(key = view.movie.id) {
                            val state = rememberPaletteImageState(url = view.movie.poster?.url)
                            MovieTimeContainer(
                                modifier = Modifier.animateItemPlacement(),
                                color = state.palette.color,
                                contentColor = state.palette.textColor,
                                name = { Text(view.movie.name) }
                            ) {
                                Image(state)
                            }
                        }
                    }
                    val id = when (view) {
                        is TimeView.Cinema -> view.cinema.id
                        is TimeView.Movie -> view.movie.id
                    }
                    for ((type, times) in view.times) item(key = "$id-${type.hashCode()}") {
                        Column(
                            modifier = Modifier.animateItemPlacement(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ProjectionTypeRow(
                                modifier = Modifier.fillMaxWidth(),
                                language = {
                                    ProjectionTypeRowDefaults.Speech {
                                        Text(type.language.getDisplayLanguage(locale))
                                    }
                                },
                                subtitle = {
                                    if (type.subtitles != null) ProjectionTypeRowDefaults.Subtitle {
                                        Text(type.subtitles.getDisplayLanguage(locale))
                                    }
                                }
                            ) {
                                for (p in type.projection) when (p) {
                                    ProjectionType.Imax -> ProjectionTypeRowDefaults.TypeImax()
                                    ProjectionType.Plane2D -> ProjectionTypeRowDefaults.Type2D()
                                    ProjectionType.Plane3D -> ProjectionTypeRowDefaults.Type3D()
                                    ProjectionType.Plane4DX -> ProjectionTypeRowDefaults.Type4DX()
                                    ProjectionType.DolbyAtmos -> ProjectionTypeRowDefaults.DolbyAtmos()
                                    ProjectionType.HighFrameRate -> ProjectionTypeRowDefaults.HighFrameRate()
                                    ProjectionType.VIP -> ProjectionTypeRowDefaults.VIP()
                                    is ProjectionType.Other -> ProjectionTypeRowDefaults.TypeOther(p.type)
                                }
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                for (time in times)
                                    TimeButton(
                                        modifier = Modifier.fillMaxWidth(),
                                        time = time.time,
                                        onClick = { onTimeClick(time) }
                                    ) {
                                        Text(time.formatted)
                                    }
                            }
                        }
                    }
                    if (view is TimeView.Movie) item(span = StaggeredGridItemSpan.FullLine) {
                        Spacer(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun TimeScreenPreview(
    @PreviewParameter(LazyTimeViewProvider::class)
    time: LazyTimeView
) = PreviewLayout {
    BookingScreen(
        poster = null,
        title = "",
        items = persistentListOf(time, time),
        onBackClick = {},
        onTimeClick = {}
    )
}

class LazyTimeViewProvider : PreviewParameterProvider<LazyTimeView> {
    override val values = sequence<LazyTimeView> {
        yield(InstantTimeView(TimeViewMovie(), TimeViewCinema()))
    }

    private class InstantTimeView(
        items: List<TimeView>
    ) : LazyTimeView {
        constructor(vararg items: TimeView) : this(items.toList())

        override val content: Flow<List<TimeView>> = flowOf(items)
        override val date: Date = Date()
    }

    private data class TimeViewMovie(
        override val movie: MovieView = MovieViewProvider().values.first(),
        override val times: Map<ShowingTag, List<SpecificTimeView>> = mapOf(
            ShowingTag(
                language = listOf(
                    Locale.FRENCH,
                    Locale.ENGLISH,
                    Locale.ITALIAN,
                    Locale.KOREAN
                ).random(),
                subtitles = listOf(
                    Locale.ENGLISH,
                    Locale.CHINESE,
                    Locale.GERMAN,
                    Locale.JAPANESE,
                    null
                ).random(),
                projection = listOf(
                    ProjectionType.Imax,
                    ProjectionType.Plane3D,
                    ProjectionType.Plane2D,
                    ProjectionType.Other("Foo")
                ).shuffled().take(nextInt(1, 3))
            ) to listOf(
                FormattedTimeView(1672560000000),
                FormattedTimeView(1672570800000),
                FormattedTimeView(1672588800000),
                FormattedTimeView(1672599600000),
            )
        )
    ) : TimeView.Movie

    private data class TimeViewCinema(
        override val cinema: CinemaView = CinemaViewParameter().values.first(),
        override val times: Map<ShowingTag, List<SpecificTimeView>> = mapOf(
            ShowingTag(
                language = listOf(
                    Locale.FRENCH,
                    Locale.ENGLISH,
                    Locale.ITALIAN,
                    Locale.KOREAN
                ).random(),
                subtitles = listOf(
                    Locale.ENGLISH,
                    Locale.CHINESE,
                    Locale.GERMAN,
                    Locale.JAPANESE,
                    null
                ).random(),
                projection = listOf(
                    ProjectionType.Imax,
                    ProjectionType.Plane3D,
                    ProjectionType.Plane2D,
                    ProjectionType.Other("Foo")
                ).shuffled().take(nextInt(1, 3))
            ) to listOf(
                FormattedTimeView(1672560000000),
                FormattedTimeView(1672570800000),
                FormattedTimeView(1672588800000),
                FormattedTimeView(1672599600000),
            )
        )
    ) : TimeView.Cinema

    private data class FormattedTimeView(
        override val time: Long,
        override val formatted: String = formatter.format(Date(time)),
    ) : SpecificTimeView {
        companion object {
            private val formatter = DateFormat.getTimeInstance(DateFormat.SHORT)
        }

        override val url: String = ""
        override val isEnabled: Boolean = nextBoolean()
    }
}