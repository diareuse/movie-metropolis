package movie.metropolis.app.screen.listing

import androidx.compose.ui.tooling.preview.datasource.*
import movie.core.model.MoviePreview
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.listing.component.DefaultPosterAspectRatio
import kotlin.random.Random

class MovieViewProvider :
    CollectionPreviewParameterProvider<MovieView>(List(10) { PreviewMovie() }) {
    private data class PreviewMovie(
        override val id: String = String(Random.nextBytes(10)),
        override val name: String = listOf(
            "Black Adam",
            "Black Panther: Wakanda Forever",
            "Strange World",
            "The Fabelmans"
        ).random(),
        override val releasedAt: String = "2022",
        override val duration: String = "1h 35m",
        override val availableFrom: String = "21. 3. 2022",
        override val directors: List<String> = emptyList(),
        override val cast: List<String> = emptyList(),
        override val countryOfOrigin: String = "USA",
        override val poster: ImageView? = PreviewImage(),
        override val video: VideoView? = PreviewVideo(),
        override val favorite: Boolean = Random.nextBoolean(),
        override val rating: String? = "75%",
        override val posterLarge: ImageView? = PreviewImage()
    ) : MovieView {
        override fun getBase(): MoviePreview = throw IllegalAccessError()
    }

    private data class PreviewImage(
        override val aspectRatio: Float = DefaultPosterAspectRatio,
        override val url: String = listOf(
            "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5145S2R-lg.jpg",
            "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5219S2R-lg.jpg",
            "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5228D2R-lg.jpg",
            "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5392S2R-lg.jpg"
        ).random()
    ) : ImageView

    private data class PreviewVideo(
        override val url: String = ""
    ) : VideoView
}