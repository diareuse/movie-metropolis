package movie.metropolis.app.screen.detail

import androidx.compose.ui.tooling.preview.datasource.*
import movie.core.model.MovieDetail
import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.PersonView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.listing.component.DefaultPosterAspectRatio
import kotlin.random.Random.Default.nextBytes
import kotlin.random.Random.Default.nextInt
import kotlin.random.Random.Default.nextLong

data class ImageViewPreview(
    override val url: String = "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5376O2R-lg.jpg",
    override val aspectRatio: Float = DefaultPosterAspectRatio
) : ImageView

class MovieDetailViewProvider : CollectionPreviewParameterProvider<MovieDetailView>(
    listOf(
        MovieDetailViewPreview()
    )
) {

    private data class MovieDetailViewPreview(
        override val id: String = nextLong().toString(),
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
        override val cast: List<PersonView> = listOf(Person("Foo Bar"), Person("Bar Foo-Foo")),
        override val directors: List<PersonView> = listOf(Person("Foofoo Barbar")),
        override val description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam vel finibus augue. Praesent porta, nibh rhoncus ultrices tempus, metus lacus facilisis lorem, id venenatis nisl mi non massa. Vestibulum eu ipsum leo. Mauris et sagittis tortor. Fusce dictum cursus quam in ornare. Curabitur posuere ligula sem, et tincidunt lorem commodo vitae. Fusce mollis elementum dignissim. Fusce suscipit massa maximus metus gravida, vitae posuere sem semper. Nullam auctor venenatis elementum. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Phasellus nibh sem, volutpat nec egestas convallis, ultricies quis massa. Duis quis placerat neque, eu bibendum arcu. ",
        override val availableFrom: String = "23. 4. 2022",
        override val poster: ImageView? = null,
        override val backdrop: ImageView? = null,
        override val trailer: VideoView? = null,
        override val rating: String? = "78%"
    ) : MovieDetailView {
        override fun base(): MovieDetail = throw NotImplementedError()
    }

    private data class Person(
        override val name: String,
        override val popularity: Int = -1,
        override val image: String = "",
        override val starredInMovies: Int = -1
    ) : PersonView

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
        override val image: String? = null,
        override val uri: String = ""
    ) : CinemaView

    private data class AvailabilityPreview(
        override val id: String = String(nextBytes(10)),
        override val url: String = "https://foo.bar",
        override val startsAt: String = "12:10",
        override val isEnabled: Boolean = true
    ) : AvailabilityView

}