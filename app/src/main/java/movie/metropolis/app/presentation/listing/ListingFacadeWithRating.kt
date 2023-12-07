package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.core.EventDetailFeature
import movie.core.model.Movie
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewWithRating
import movie.rating.MetadataProvider
import movie.rating.MovieDescriptor
import movie.rating.MovieMetadata
import java.util.Calendar

class ListingFacadeWithRating(
    private val origin: ListingFacade,
    private val rating: MetadataProvider,
    private val detail: EventDetailFeature
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

    private fun withRating(movies: List<MovieView>) = channelFlow {
        if (cache.isEmpty()) send(movies)
        val writeLock = Mutex()
        val movies = movies.toMutableList()
        for ((index, movie) in movies.withIndex()) launch {
            val base = movie.getBase()
            val rating = getRating(base) ?: return@launch
            val updated = MovieViewWithRating(movie, rating)
            val output = writeLock.withLock {
                movies[index] = updated
                movies.toList()
            }
            send(output)
        }
    }

    private suspend fun getRating(movie: Movie): MovieMetadata? = cache.getOrPut(movie.id) {
        val detail = try {
            detail.get(movie)
        } catch (e: Throwable) {
            return null
        }
        val year = Calendar.getInstance().apply { time = detail.releasedAt }[Calendar.YEAR]
        val descriptors = listOf(
            MovieDescriptor.Original(detail.originalName, year),
            MovieDescriptor.Local(detail.name, year)
        )
        for (descriptor in descriptors) {
            return@getOrPut rating.get(descriptor) ?: continue
        }
        return null
    }

}