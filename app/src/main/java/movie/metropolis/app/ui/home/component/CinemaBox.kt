package movie.metropolis.app.ui.home.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.screen.cinema.component.CinemaViewProvider
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.rememberImageState
import movie.style.util.pc

@Composable
fun CinemaBox(
    onClick: () -> Unit,
    name: @Composable () -> Unit,
    city: @Composable () -> Unit,
    distance: @Composable () -> Unit,
    image: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) = Card(
    modifier = modifier,
    onClick = onClick,
    shape = MaterialTheme.shapes.large
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = MaterialTheme.shapes.large
    ) {
        image()
    }
    Column(modifier = Modifier.padding(2.pc, 1.pc)) {
        name()
        city()
        distance()
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun CinemaBoxPreview(
    @PreviewParameter(CinemaViewProvider::class, limit = 1)
    view: CinemaView
) = PreviewLayout {
    CinemaBox(
        onClick = {},
        name = { Text(view.name) },
        city = { Text(view.city) },
        distance = {
            val d = view.distance
            if (d != null) Text(d)
        },
        image = { Image(rememberImageState(view.image)) }
    )
}