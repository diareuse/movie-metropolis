package movie.metropolis.app.screen.movie.component

import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.PersonView
import kotlin.random.Random

class MovieDetailViewProvider : CollectionPreviewParameterProvider<MovieDetailView>(
    List(1) {
        MovieDetailView(Random.nextLong().toString()).apply {
            name = listOf(
                "Black Adam",
                "Black Panther: Wakanda Forever",
                "Strange World",
                "The Fabelmans"
            ).random()
            nameOriginal = listOf(
                "Black Adam",
                "Black Panther: Wakanda Forever",
                "Strange World",
                "The Fabelmans"
            ).random()
            releasedAt = "2022"
            duration = "1h 34m"
            countryOfOrigin = "USA"
            cast = listOf(PersonView("Foo Bar"), PersonView("Bar Foo-Foo"))
            directors = listOf(PersonView("Foofoo Barbar"))
            description =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam vel finibus augue. Praesent porta, nibh rhoncus ultrices tempus, metus lacus facilisis lorem, id venenatis nisl mi non massa. Vestibulum eu ipsum leo. Mauris et sagittis tortor. Fusce dictum cursus quam in ornare. Curabitur posuere ligula sem, et tincidunt lorem commodo vitae. Fusce mollis elementum dignissim. Fusce suscipit massa maximus metus gravida, vitae posuere sem semper. Nullam auctor venenatis elementum. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Phasellus nibh sem, volutpat nec egestas convallis, ultricies quis massa. Duis quis placerat neque, eu bibendum arcu. "
            availableFrom = "23. 4. 2022"
            poster = ImageViewPreview()
            backdrop = ImageViewPreview()
            trailer = null
            ratingNumber = .78f
        }
    }
)