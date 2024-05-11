package movie.metropolis.app.presentation.favorite

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewWithRating
import movie.rating.MetadataProvider
import movie.rating.MovieDescriptor
import movie.rating.MovieMetadata
import java.util.Calendar

class FavoriteFacadeRating(
    private val origin: FavoriteFacade,
    private val rating: MetadataProvider
) : FavoriteFacade by origin {

    private val cache = mutableMapOf<String, MovieMetadata>()

    override fun get(): Flow<List<MovieView>> = origin.get().flatMapLatest { withRating(it) }

    private fun withRating(items: List<MovieView>) = channelFlow {
        if (cache.isEmpty()) send(items)
        val output = items.toMutableList()
        val lock = Mutex()
        for ((index, movie) in output.withIndex()) launch {
            val rating = getRating(movie) ?: return@launch
            val out = lock.withLock {
                output[index] = MovieViewWithRating(movie, rating)
                output.toList()
            }
            send(out)
        }
    }

    private suspend fun getRating(movie: MovieView): MovieMetadata? = cache.getOrPut(movie.id) {
        val year = Calendar.getInstance()[Calendar.YEAR]
        val descriptors = arrayOf(
            MovieDescriptor.Original(movie.name, year)
        )
        for (descriptor in descriptors)
            return rating.get(descriptor) ?: continue
        return null
    }

}