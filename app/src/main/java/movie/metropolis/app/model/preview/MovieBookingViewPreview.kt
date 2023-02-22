package movie.metropolis.app.model.preview

import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.MovieBookingView
import kotlin.random.Random.Default.nextInt

class MovieBookingViewPreview :
    CollectionPreviewParameterProvider<MovieBookingView>(listOf(MovieBookingViewPreview())) {

    data class MovieBookingViewPreview(
        override val availability: Map<AvailabilityView.Type, List<AvailabilityView>> = mapOf(
            Type() to listOf(AvailabilityViewPreview(), AvailabilityViewPreview())
        ),
        override val movie: MovieBookingView.Movie = MoviePreview()
    ) : MovieBookingView

    data class Type(
        override val types: List<String> = listOf("3D", "IMAX"),
        override val language: String = "English"
    ) : AvailabilityView.Type

    data class AvailabilityViewPreview(
        override val id: String = nextInt().toString(),
        override val url: String = "",
        override val startsAt: String = "10:20",
        override val isEnabled: Boolean = true
    ) : AvailabilityView

    data class MoviePreview(
        override val id: String = nextInt().toString(),
        override val name: String = "Knock at the Cabin",
        override val releasedAt: String = "2022",
        override val duration: String = "2h10m",
        override val poster: String = "https://m.media-amazon.com/images/M/MV5BZTc4MjU0MjMtYTEwNy00YjNlLTk2MGYtMjNlNzFjMmY4MjQ0XkEyXkFqcGdeQXVyODk4OTc3MTY@._V1_QL75_UX140_CR0,7,140,207_.jpg",
        override val video: String? = null
    ) : MovieBookingView.Movie

}