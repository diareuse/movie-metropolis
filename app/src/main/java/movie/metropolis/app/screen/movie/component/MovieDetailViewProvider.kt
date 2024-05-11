package movie.metropolis.app.screen.movie.component

import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.PersonView
import movie.metropolis.app.model.VideoView
import kotlin.random.Random

class MovieDetailViewProvider : CollectionPreviewParameterProvider<MovieDetailView>(
    listOf(
        MovieDetailViewPreview()
    )
) {

    private data class MovieDetailViewPreview(
        override val id: String = Random.nextLong().toString(),
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
        override val poster: ImageView? = ImageViewPreview(),
        override val backdrop: ImageView? = ImageViewPreview(),
        override val trailer: VideoView? = null,
        override val rating: String? = "78%"
    ) : MovieDetailView

    private data class Person(
        override val name: String,
        override val popularity: Int = -1,
        override val image: String = "",
        override val starredInMovies: Int = -1,
        override val url: String = ""
    ) : PersonView

}