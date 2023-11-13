package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.screen.listing.component.DefaultPosterAspectRatio
import movie.rating.MovieMetadata

class MovieDetailViewWithRating(
    private val origin: MovieDetailView,
    private val ratings: MovieMetadata?
) : MovieDetailView by origin {

    override val rating: String?
        get() = ratings?.rating?.takeIf { it > 0 }?.let { "%d%%".format(it) }
    override val poster: ImageView?
        get() = ratings?.posterImageUrl?.let {
            object : ImageView {
                override val aspectRatio: Float
                    get() = DefaultPosterAspectRatio
                override val url: String
                    get() = it
            }
        } ?: origin.poster
    override val backdrop: ImageView?
        get() = ratings?.overlayImageUrl?.let {
            object : ImageView {
                override val aspectRatio: Float
                    get() = -1f
                override val url: String
                    get() = it
            }
        } ?: origin.backdrop

}