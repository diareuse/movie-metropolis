package movie.metropolis.app.model.adapter

import movie.core.model.MoviePreview
import movie.core.model.MovieReference
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.listing.component.DefaultPosterAspectRatio
import movie.metropolis.app.util.toStringComponents
import java.text.SimpleDateFormat
import java.util.Locale

data class MovieViewFromReference(
    private val ref: MovieReference
) : MovieView {

    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    override val id: String
        get() = ref.id
    override val name: String
        get() = ref.name
    override val releasedAt: String
        get() = yearFormat.format(ref.releasedAt)
    override val duration: String
        get() = ref.duration.toStringComponents()
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
        get() = object : ImageView {
            override val aspectRatio: Float
                get() = DefaultPosterAspectRatio
            override val url: String
                get() = ref.posterUrl
        }
    override val posterLarge: ImageView
        get() = poster
    override val video: VideoView?
        get() = ref.videoUrl?.let {
            object : VideoView {
                override val url: String
                    get() = it
            }
        }

    override fun getBase(): MoviePreview {
        throw NotImplementedError()
    }
}