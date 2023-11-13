package movie.metropolis.app.model.adapter

import movie.core.model.Media
import movie.core.model.MovieDetail
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.PersonView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.util.toStringComponents
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

data class MovieDetailViewFromFeature(
    private val movie: MovieDetail
) : MovieDetailView {

    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)

    override val id: String
        get() = movie.id
    override val name: String
        get() = movie.name
    override val nameOriginal: String
        get() = movie.originalName
    override val releasedAt: String
        get() = yearFormat.format(movie.releasedAt)
    override val duration: String
        get() = movie.duration.toStringComponents()
    override val countryOfOrigin: String
        get() = movie.countryOfOrigin.orEmpty()
    override val cast: List<PersonView>
        get() = movie.cast.map(::PersonViewFromName)
    override val directors: List<PersonView>
        get() = movie.directors.map(::PersonViewFromName)
    override val description: String
        get() = movie.description
    override val availableFrom: String
        get() = dateFormat.format(movie.screeningFrom)
    override val poster: ImageView?
        get() = movie.media.asSequence().filterIsInstance<Media.Image>()
            .sortedByDescending { it.width * it.height }.firstOrNull()
            ?.let { ImageViewFromFeature(it) }
    override val backdrop: ImageView?
        get() = poster
    override val trailer: VideoView?
        get() = movie.media.filterIsInstance<Media.Video>().firstOrNull()
            ?.let(::VideoViewFromFeature)
    override val rating: String?
        get() = null//if (movie.rating == 0.toByte()) null else "%d%%".format(movie.rating)

    override fun base(): MovieDetail {
        return movie
    }

    data class PersonViewFromName(
        override val name: String
    ) : PersonView {
        override val popularity: Int = -1
        override val image: String = ""
        override val starredInMovies: Int = -1
    }

}