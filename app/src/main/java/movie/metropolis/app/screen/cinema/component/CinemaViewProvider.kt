package movie.metropolis.app.screen.cinema.component

import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.model.CinemaView
import kotlin.random.Random.Default.nextLong

class CinemaViewProvider : CollectionPreviewParameterProvider<CinemaView>(
    listOf(Data(), Data(distance = "2.3km"))
) {
    data class Data(
        override val id: String = nextLong().toString(),
        override val name: String = "Nový Smíchov",
        override val address: String = "Plzeňská 8, Praha 5",
        override val city: String = "Praha",
        override val distance: String? = null,
        override val image: String? = "https://www.cinemacity.cz/static/dam/jcr:7d2db264-ff3d-417b-b88d-0d8f95a6ca82",
        override val uri: String = ""
    ) : CinemaView
}