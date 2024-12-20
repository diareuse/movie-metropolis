package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.util.onEachLaunch
import movie.metropolis.app.util.retryOnNetworkError
import movie.rating.MetadataProvider
import movie.rating.MovieDescriptor
import movie.rating.MovieMetadata
import movie.style.layout.DefaultPosterAspectRatio
import java.util.Calendar

class MovieFacadeRating(
    private val origin: MovieFacade,
    private val rating: MetadataProvider
) : MovieFacade by origin {

    private var metadata: MovieMetadata? = null

    override val movie: Flow<MovieDetailView> = origin.movie.onEachLaunch {
        val m = getMetadata(it) ?: return@onEachLaunch
        it.url = m.url
        it.rating = m.rating.takeIf { it > 0 }?.let { "%d%%".format(it) }
        it.poster = m.posterImageUrl.let {
            object : ImageView {
                override val aspectRatio: Float
                    get() = DefaultPosterAspectRatio
                override val url: String
                    get() = it
            }
        }
        it.backdrop = m.overlayImageUrl.let {
            object : ImageView {
                override val aspectRatio: Float
                    get() = -1f
                override val url: String
                    get() = it
            }
        }
    }.retryOnNetworkError()

    private suspend fun getMetadata(movie: MovieDetailView): MovieMetadata? {
        val m = metadata
        if (m != null) return m
        val year = Calendar.getInstance()[Calendar.YEAR]
        val descriptors = arrayOf(
            MovieDescriptor.Original(movie.nameOriginal, year),
            MovieDescriptor.Local(movie.name, year)
        )
        metadata = descriptors.fold(null as MovieMetadata?) { acc, it ->
            acc ?: rating.get(it)
        }
        return metadata
    }

}