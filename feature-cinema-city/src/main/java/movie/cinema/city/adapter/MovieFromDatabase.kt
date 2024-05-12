package movie.cinema.city.adapter

import movie.cinema.city.Movie
import movie.cinema.city.persistence.MovieStored
import java.net.URL
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal data class MovieFromDatabase(
    private val storedMovie: MovieStored,
    private val storedCast: List<MovieStored.Cast>,
    private val storedDirectors: List<MovieStored.Director>,
    private val storedGenre: List<MovieStored.Genre>,
    private val storedImage: List<MovieStored.Image>,
    private val storedVideo: List<MovieStored.Video>
) : Movie {

    override val id: String
        get() = storedMovie.id
    override val name: Movie.Label = Label()
    override val length: Duration?
        get() = storedMovie.length?.milliseconds
    override val link: URL
        get() = storedMovie.link
    override val releasedAt: Date
        get() = storedMovie.releasedAt
    override val originCountry: String?
        get() = storedMovie.originCountry
    override val cast: List<String>
        get() = storedCast.map { it.name }
    override val directors: List<String>
        get() = storedDirectors.map { it.name }
    override val synopsis: String
        get() = storedMovie.synopsis
    override val screeningFrom: Date
        get() = storedMovie.screeningFrom
    override val genres: List<String>
        get() = storedGenre.map { it.genre }
    override val ageRestriction: URL
        get() = storedMovie.ageRestriction
    override val videos: List<URL>
        get() = storedVideo.map { it.url }
    override val images: List<Movie.Image>
        get() = storedImage.map(MovieFromDatabase::Image)

    private inner class Label : Movie.Label {
        override val localized: String
            get() = storedMovie.nameLocalized
        override val original: String
            get() = storedMovie.nameOriginal
    }

    private data class Image(
        private val stored: MovieStored.Image
    ) : Movie.Image {
        override val width: Int
            get() = stored.width
        override val height: Int
            get() = stored.height
        override val url: URL
            get() = stored.url
    }

}