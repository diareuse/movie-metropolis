package movie.metropolis.app.model.adapter

import movie.cinema.city.Movie
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.PersonView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.util.toStringComponents
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

data class MovieDetailViewFromFeature(
    private val movie: Movie
) : MovieDetailView {

    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)

    override val id: String
        get() = movie.id
    override val name: String
        get() = movie.name.localized
    override val nameOriginal: String
        get() = movie.name.original
    override val releasedAt: String
        get() = yearFormat.format(movie.releasedAt)
    override val duration: String
        get() = movie.length?.toStringComponents().orEmpty()
    override val countryOfOrigin: String
        get() = movie.originCountry.orEmpty()
    override val cast: List<PersonView>
        get() = movie.cast.map(::PersonViewFromName)
    override val directors: List<PersonView>
        get() = movie.directors.map(::PersonViewFromName)
    override val description: String
        get() = movie.synopsis
    override val availableFrom: String
        get() = dateFormat.format(movie.screeningFrom)
    override val poster: ImageView?
        get() = movie.images.firstOrNull()?.let(::ImageViewFromFeature)
    override val backdrop: ImageView?
        get() = poster
    override val trailer: VideoView?
        get() = movie.videos.firstOrNull()?.let(Any::toString)?.let(::VideoViewFromFeature)
    override val rating: String?
        get() = null//if (movie.rating == 0.toByte()) null else "%d%%".format(movie.rating)

    data class PersonViewFromName(
        override val name: String
    ) : PersonView {
        override val url = ""
        override val popularity: Int = -1
        override val image: String = ""
        override val starredInMovies: Int = -1
    }

}