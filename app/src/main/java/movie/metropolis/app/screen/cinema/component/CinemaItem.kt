package movie.metropolis.app.screen.cinema.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.model.CinemaView
import movie.style.Image
import movie.style.OnClickListener
import movie.style.layout.PreviewLayout
import movie.style.rememberPaletteImageState
import kotlin.random.Random.Default.nextLong

@Composable
fun CinemaItem(
    view: CinemaView,
    onClick: OnClickListener,
    modifier: Modifier = Modifier
) {
    val state = rememberPaletteImageState(url = view.image.orEmpty())
    CinemaLayout(
        modifier = modifier,
        image = { Image(state) },
        city = { Text(view.city) },
        name = { Text(view.name) },
        address = { Text(view.address) },
        distance = {
            val distance = view.distance
            if (distance != null)
                Text(distance, modifier = Modifier.padding(16.dp, 8.dp))
        },
        onClick = onClick
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun CinemaItemPreview(
    @PreviewParameter(CinemaViewParameter::class, 2)
    view: CinemaView
) = PreviewLayout {
    CinemaItem(view = view, onClick = {})
}

class CinemaViewParameter : CollectionPreviewParameterProvider<CinemaView>(
    listOf(Data(), Data(distance = "2.3km"))
) {
    data class Data(
        override val id: String = nextLong().toString(),
        override val name: String = "Nový Smíchov",
        override val address: String = "Plzeňská 8, Praha 5",
        override val city: String = "Praha",
        override val distance: String? = null,
        override val image: String? = "https://www.cinemacity.cz/static/dam/jcr:7d2db264-ff3d-417b-b88d-0d8f95a6ca82"
    ) : CinemaView
}