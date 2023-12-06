package movie.metropolis.app.presentation.favorite

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
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

class FavoriteFacadeRating(
    private val origin: FavoriteFacade,
    private val rating: MetadataProvider,
    private val detail: EventDetailFeature
) : FavoriteFacade by origin {

    private val cache = mutableMapOf<String, MovieMetadata>()

    override fun get(): Flow<List<MovieView>> = origin.get().flatMapLatest { withRating(it) }

    private fun withRating(items: List<MovieView>) = channelFlow {
        if (cache.isEmpty()) send(items)
        val output = items.toMutableList()
        val lock = Mutex()
        for ((index, movie) in output.withIndex()) launch {
            val base = movie.getBase()
            val rating = getRating(base) ?: return@launch
            val out = lock.withLock {
                output[index] = MovieViewWithRating(movie, rating)
                output.toList()
            }
            send(out)
        }
    }

    private suspend fun getRating(movie: Movie): MovieMetadata? = cache.getOrPut(movie.id) {
        val descriptors = detail.runCatching { get(movie) }.map {
            val year = Calendar.getInstance().apply { time = it.releasedAt }[Calendar.YEAR]
            arrayOf(
                MovieDescriptor.Original(it.originalName, year),
                MovieDescriptor.Local(it.name, year)
            )
        }.getOrNull() ?: return null
        descriptors.fold(null as MovieMetadata?) { acc, it ->
            acc ?: rating.get(it)
        } ?: return@getRating null
    }

}