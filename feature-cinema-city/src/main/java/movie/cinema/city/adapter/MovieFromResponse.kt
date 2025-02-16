package movie.cinema.city.adapter

import movie.cinema.city.Movie
import movie.cinema.city.model.MovieDetailResponse
import java.net.URL
import java.util.Date
import kotlin.time.Duration

internal data class MovieFromResponse(
    private val movie: MovieDetailResponse
) : Movie {
    override val id: String
        get() = movie.id
    override val name: Movie.Label = Label()
    override val length: Duration?
        get() = movie.duration
    override val link: URL
        get() = URL(movie.url)
    override val releasedAt: Date
        get() = movie.releasedAt ?: Date()
    override val originCountry: String?
        get() = movie.countryOfOrigin
    override val cast: List<String>
        get() = movie.cast.orEmpty().split(", ", ",")
    override val directors: List<String>
        get() = movie.directors.orEmpty().split(", ", ",")
    override val synopsis: String
        get() = movie.synopsis.orEmpty()
    override val screeningFrom: Date
        get() = movie.screeningFrom
    override val genres: List<String>
        get() = movie.genres
    override val ageRestriction: URL
        get() = URL(movie.restrictionUrl)
    override val videos: List<URL>
        get() = movie.media.filterIsInstance<MovieDetailResponse.Media.Video>().map { URL(it.url) }
    override val images: List<Movie.Image>
        get() = movie.media.filterIsInstance<MovieDetailResponse.Media.Image>().map(
            MovieFromResponse::Image
        )

    private data class Image(
        private val image: MovieDetailResponse.Media.Image
    ) : Movie.Image {
        override val width: Int
            get() = image.width
        override val height: Int
            get() = image.height
        override val url: URL
            get() = URL(image.url)
    }

    private inner class Label : Movie.Label {
        override val localized: String
            get() = movie.name
        override val original: String
            get() = movie.nameOriginal
                .replace(Technology, "")
                .replace(Sound, "")
                .trim()
    }

    companion object {
        private val Technology = Regex("(IMAX|(?:[2-4]DX?))")
        private val Sound = Regex("\\s*-\\s*((?:(d|s)ub(?:titles)?)|original)")
    }
}