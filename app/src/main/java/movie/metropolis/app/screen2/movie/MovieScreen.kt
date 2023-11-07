@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen2.movie

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.screen.detail.MovieDetailViewProvider
import movie.metropolis.app.screen.listing.component.DefaultPosterAspectRatio
import movie.style.BackgroundImage
import movie.style.CollapsingTopAppBar
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.modifier.VerticalGravity
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.modifier.verticalOverlay
import movie.style.rememberImageState
import movie.style.theme.Theme

@Composable
fun MovieScreen(
    movie: MovieDetailView?,
    onBackClick: () -> Unit,
    onBookClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) = Scaffold(
    modifier = modifier,
    topBar = {
        CollapsingTopAppBar(
            title = {
                if (movie != null) Text(
                    "Detail",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, null)
                }
            },
            scrollBehavior = scrollBehavior
        )
    }
) { padding ->
    val poster = rememberImageState(url = movie?.poster?.url)
    BackgroundImage(
        modifier = Modifier
            .fillMaxSize()
            .verticalOverlay(.5f, VerticalGravity.Bottom),
        state = poster
    )
    Column(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onBookClick) {
            Text("Book")
        }
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(DefaultPosterAspectRatio)
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp)
                .surface(1.dp, Theme.container.poster)
                .glow(Theme.container.poster),
            state = poster
        )
        if (movie != null) Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = movie.name,
                style = Theme.textStyle.title,
                textAlign = TextAlign.Center
            )
            Text(
                text = "%s • %s • %s".format(
                    movie.duration,
                    movie.countryOfOrigin,
                    movie.releasedAt
                ),
                textAlign = TextAlign.Center
            )
            if (movie.directors.isNotEmpty()) Text(
                text = movie.directors.joinToString(),
                style = Theme.textStyle.emphasis,
                textAlign = TextAlign.Center
            )
            if (movie.cast.isNotEmpty()) Text(
                text = movie.cast.joinToString(),
                style = Theme.textStyle.caption,
                textAlign = TextAlign.Center
            )
            if (movie.description.isNotEmpty()) Text(
                text = movie.description,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun MovieScreenPreview() = PreviewLayout {
    val movie = MovieDetailViewProvider().values.first()
    MovieScreen(movie, {}, {})
}