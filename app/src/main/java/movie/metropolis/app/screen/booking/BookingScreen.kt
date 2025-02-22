@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen.booking

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import movie.metropolis.app.R
import movie.metropolis.app.model.FiltersView
import movie.metropolis.app.model.LazyTimeView
import movie.metropolis.app.model.ProjectionType
import movie.metropolis.app.model.ShowingTag
import movie.metropolis.app.model.SpecificTimeView
import movie.metropolis.app.model.TimeView
import movie.metropolis.app.screen.booking.component.CinemaTimeContainer
import movie.metropolis.app.screen.booking.component.DateBox
import movie.metropolis.app.screen.booking.component.MovieTimeContainer
import movie.metropolis.app.screen.booking.component.ProjectionTypeRow
import movie.metropolis.app.screen.booking.component.ProjectionTypeRowDefaults
import movie.metropolis.app.screen.booking.component.TimeButton
import movie.metropolis.app.screen.booking.component.rememberMultiChildPagerState
import movie.metropolis.app.screen.cinema.component.CinemaViewProvider
import movie.metropolis.app.screen.movie.component.MovieViewProvider
import movie.metropolis.app.util.interpolatePage
import movie.style.BackgroundImage
import movie.style.CollapsingTopAppBar
import movie.style.Image
import movie.style.haptic.TickOnChange
import movie.style.layout.PreviewLayout
import movie.style.layout.alignForLargeScreen
import movie.style.layout.largeScreenPadding
import movie.style.layout.plus
import movie.style.modifier.VerticalGravity
import movie.style.modifier.surface
import movie.style.modifier.verticalOverlay
import movie.style.rememberImageState
import movie.style.rememberPaletteImageState
import movie.style.theme.Theme
import java.text.DateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextInt

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun BookingScreen(
    activeFilterCount: Int,
    poster: String?,
    title: String,
    items: ImmutableList<LazyTimeView>,
    onBackClick: () -> Unit,
    onActionClick: () -> Unit,
    onTimeClick: (SpecificTimeView) -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) = Scaffold(
    modifier = modifier.fillMaxSize(),
    contentWindowInsets = WindowInsets(0),
    topBar = {
        CollapsingTopAppBar(
            modifier = Modifier.alignForLargeScreen(),
            scrollBehavior = scrollBehavior,
            title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(painterResource(R.drawable.ic_back), null)
                }
            },
            actions = {
                Box(
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = onActionClick) {
                        Icon(painterResource(R.drawable.ic_filter), null)
                    }
                    if (activeFilterCount > 0)
                        Text(
                            modifier = Modifier
                                .surface(Theme.color.container.primary, CircleShape)
                                .padding(4.dp, 0.dp),
                            text = "$activeFilterCount",
                            style = Theme.textStyle.caption,
                            color = Theme.color.content.primary
                        )
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
    var largeScreenPadding by remember { mutableStateOf(PaddingValues(0.dp)) }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = padding.calculateTopPadding())
            .largeScreenPadding(widthAtMost = 350.dp) { largeScreenPadding = it },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TickOnChange(statePage.currentPage)
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = stateDate,
            contentPadding = PaddingValues(horizontal = 128.dp) + largeScreenPadding,
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
            beyondViewportPageCount = 1,
            pageNestedScrollConnection = scrollBehavior.nestedScrollConnection,
            verticalAlignment = Alignment.Top,
            contentPadding = largeScreenPadding
        ) {
            val page = items[it].content
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
                val page = page
                for (view in page) {
                    if (view.times.isEmpty()) continue
                    when (view) {
                        is TimeView.Cinema -> item(
                            key = view.cinema.id,
                            span = StaggeredGridItemSpan.FullLine
                        ) {
                            val state = rememberPaletteImageState(url = view.cinema.image)
                            CinemaTimeContainer(
                                modifier = Modifier.animateItem(
                                    fadeInSpec = null,
                                    fadeOutSpec = null
                                ),
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
                                color = state.palette.color,
                                name = { Text(view.movie.name) },
                                modifier = Modifier.animateItem(
                                    fadeInSpec = null,
                                    fadeOutSpec = null
                                )
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
                            modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
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
                                for (p in type.projection)
                                    ProjectionTypeRow(type = p)
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
        activeFilterCount = 2,
        poster = null,
        title = "",
        items = persistentListOf(time, time),
        onBackClick = {},
        onTimeClick = {},
        onActionClick = {}
    )
}

class LazyTimeViewProvider : PreviewParameterProvider<LazyTimeView> {
    override val values = sequence {
        yield(LazyTimeView(Date()).apply {
            content += TimeView.Movie(MovieViewProvider().values.first(), FiltersView()).apply {
                times += mapOf(
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
                        ).shuffled().take(nextInt(1, 3)).toImmutableList()
                    ) to listOf(
                        FormattedTimeView(1672560000000),
                        FormattedTimeView(1672570800000),
                        FormattedTimeView(1672588800000),
                        FormattedTimeView(1672599600000),
                    )
                )
            }
            content += TimeView.Cinema(CinemaViewProvider().values.first(), FiltersView()).apply {
                times += mapOf(
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
                        ).shuffled().take(nextInt(1, 3)).toImmutableList()
                    ) to listOf(
                        FormattedTimeView(1672560000000),
                        FormattedTimeView(1672570800000),
                        FormattedTimeView(1672588800000),
                        FormattedTimeView(1672599600000),
                    )
                )
            }
        })
    }

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