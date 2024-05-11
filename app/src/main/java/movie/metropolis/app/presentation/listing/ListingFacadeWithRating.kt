package movie.metropolis.app.presentation.listing

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewWithRating
import movie.rating.MetadataProvider
import movie.rating.MovieDescriptor
import movie.rating.MovieMetadata
import java.util.Calendar

class ListingFacadeWithRating(
    private val origin: ListingFacade,
    private val rating: MetadataProvider
) : ListingFacade by origin {

    private val cache = mutableMapOf<String, MovieMetadata>()

    override fun get() = origin.get().flatMapLatest {
        flow {
            withRating(it.items)
                .catch { _ -> emit(it.items) }
                .collect { items ->
                    emit(it.copy(items = items))
                }
        }
    }

    private fun withRating(movies: ImmutableList<MovieView>) = channelFlow {
        if (cache.isEmpty()) send(movies)
        val writeLock = Mutex()
        val movies = movies.toMutableList()
        for ((index, movie) in movies.withIndex()) launch {
            val rating = getRating(movie) ?: return@launch
            val updated = MovieViewWithRating(movie, rating)
            val output = writeLock.withLock {
                movies[index] = updated
                movies.toPersistentList()
            }
            send(output)
        }
    }

    private suspend fun getRating(movie: MovieView): MovieMetadata? = cache.getOrPut(movie.id) {
        val year = Calendar.getInstance()[Calendar.YEAR]
        val descriptors = listOf(
            MovieDescriptor.Original(movie.name, year)
        )
        for (descriptor in descriptors) {
            return@getOrPut rating.get(descriptor) ?: continue
        }
        return null
    }

}