package movie.metropolis.app.model.adapter

import androidx.compose.ui.graphics.*
import movie.core.model.Media
import movie.core.model.MovieDetail
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
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
    override val cast: List<String>
        get() = movie.cast.toList()
    override val directors: List<String>
        get() = movie.directors.toList()
    override val description: String
        get() = movie.description
    override val availableFrom: String
        get() = dateFormat.format(movie.screeningFrom)
    override val poster: ImageView?
        get() = movie.media.asSequence().filterIsInstance<Media.Image>()
            .sortedByDescending { it.width * it.height }.firstOrNull()
            ?.let { ImageViewFromFeature(it, Color.Black) }
    override val trailer: VideoView?
        get() = movie.media.filterIsInstance<Media.Video>().firstOrNull()
            ?.let(::VideoViewFromFeature)
    override val rating: String?
        get() = null//if (movie.rating == 0.toByte()) null else "%d%%".format(movie.rating)
    override val links: MovieDetailView.Links?
        get() = Links().takeIf { it.isValid }

    inner class Links : MovieDetailView.Links {
        override val imdb: String?
            get() = null//movie.linkImdb
        override val csfd: String?
            get() = null//movie.linkCsfd
        override val rottenTomatoes: String?
            get() = null//movie.linkRottenTomatoes
        val isValid get() = imdb != null || csfd != null || rottenTomatoes != null
    }
}