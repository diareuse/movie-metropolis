package movie.metropolis.app.model.adapter

import movie.cinema.city.Movie
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.util.toStringComponents
import java.text.SimpleDateFormat
import java.util.Locale

data class MovieViewFromReference(
    private val ref: Movie
) : MovieView {

    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    override val id: String
        get() = ref.id
    override val name: String
        get() = ref.name.localized
    override val releasedAt: String
        get() = yearFormat.format(ref.releasedAt)
    override val duration: String
        get() = ref.length?.toStringComponents().orEmpty()
    override val availableFrom: String
        get() = releasedAt
    override val directors: List<String>
        get() = emptyList()
    override val cast: List<String>
        get() = emptyList()
    override val countryOfOrigin: String
        get() = ""
    override val favorite: Boolean
        get() = false
    override val rating: String?
        get() = null
    override val poster: ImageView
        get() = ref.images.first().let(::ImageViewFromMovie)
    override val posterLarge: ImageView
        get() = poster
    override val video: VideoView?
        get() = ref.videos.firstOrNull()?.let(Any::toString)?.let(::VideoViewFromFeature)
    override val url: String
        get() = ref.link.toString()

}