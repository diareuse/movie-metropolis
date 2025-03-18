package movie.metropolis.app.ui.ticket

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.tooling.preview.*
import movie.style.Barcode
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.rememberImageState
import movie.style.util.pc

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TicketScreen(
    animationScope: AnimatedContentScope,
    state: TicketScreenState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) = TicketScreenScaffold(
    modifier = modifier.sharedBounds(
        sharedContentState = rememberSharedContentState("tickets"),
        animatedVisibilityScope = animationScope,
        clipInOverlayDuringTransition = OverlayClip(MaterialTheme.shapes.medium),
        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(contentScale = ContentScale.Crop)
    ),
    title = { Text("Tickets") },
    navigationIcon = {
        IconButton(onBackClick) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, null)
        }
    },
    state = rememberPagerState { state.tickets.size }
) { index ->
    val t = state.tickets[index]
    Box {
        Image(rememberImageState(t.movie.poster?.url), Modifier.matchParentSize())
        Column(Modifier.padding(1.pc)) {
            Text(t.name)
            Text(t.cinema.name)
            Text(t.date)
            Text(t.time)
            Text(t.hall)
            for (s in t.seats)
                Text("${s.row}, ${s.seat}")
            Barcode(t.id, Modifier.background(Color.White))
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@PreviewLightDark
@PreviewFontScale
@Composable
private fun TicketScreenPreview() = PreviewLayout {
    SharedTransitionLayout {
        AnimatedContent(true) { _ ->
            TicketScreen(this, TicketScreenState(), {})
        }
    }
}