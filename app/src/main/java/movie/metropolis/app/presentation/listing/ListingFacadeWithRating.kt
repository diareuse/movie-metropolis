package movie.metropolis.app.presentation.listing

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.rating.MetadataProvider
import movie.rating.MovieDescriptor
import movie.rating.MovieMetadata
import movie.style.layout.DefaultPosterAspectRatio
import java.util.Calendar

class ListingFacadeWithRating(
    private val origin: ListingFacade,
    private val rating: MetadataProvider
) : ListingFacade by origin {

    private val cache = mutableMapOf<String, MovieMetadata>()

    override fun get() = origin.get().flatMapLatest {
        flow {
            withRating(it.items)
                .collect { items ->
                    emit(it.copy(items = items))
                }
        }
    }

    private fun withRating(movies: ImmutableList<MovieView>) = channelFlow {
        if (cache.isEmpty()) send(movies)
        val movies = movies.toMutableList()
        for (movie in movies) launch {
            val rating = getRating(movie) ?: return@launch
            movie.rating = rating.rating.takeIf { it > 0 }?.let { "%d%%".format(it) }
            movie.url = rating.url
            movie.poster = rating.posterImageUrl.takeUnless { it.isBlank() }?.let {
                object : ImageView {
                    override val aspectRatio: Float
                        get() = DefaultPosterAspectRatio
                    override val url: String
                        get() = it
                }
            }
            movie.posterLarge = movie.poster
        }
    }

    private suspend fun getRating(movie: MovieView): MovieMetadata? = cache.getOrPut(movie.id) {
        val year = movie.releasedAt.toIntOrNull() ?: Calendar.getInstance()[Calendar.YEAR]
        val descriptors = listOf(
            MovieDescriptor.Original(movie.name, year)
        )
        for (descriptor in descriptors) {
            return@getOrPut rating.get(descriptor) ?: continue
        }
        return null
    }

}