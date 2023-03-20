package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.core.EventDetailFeature
import movie.core.model.MoviePreview
import movie.metropolis.app.model.Genre
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewWithRating
import movie.metropolis.app.util.flatMapResult
import movie.rating.MovieDescriptor
import movie.rating.RatingProvider
import java.util.Calendar

class ListingFacadeActionWithRating(
    private val origin: ListingFacade.Action,
    private val rating: RatingProvider.Composed,
    private val detail: EventDetailFeature
) : ListingFacade.Action by origin {

    private val cache = mutableMapOf<String, Byte>()

    override val groups = origin.groups.flatMapResult { withRating(it) }

    private fun withRating(items: Map<Genre, List<MovieView>>) = channelFlow {
        val output = items.mapValues { (_, it) -> it.toMutableList() }
        val locks = mutableMapOf<String, Mutex>()
        send(output)
        for ((genre, movies) in items) {
            for ((index, movie) in movies.withIndex()) launch {
                val updated = locks.getOrPut(movie.id) { Mutex() }.withLock {
                    val base = movie.getBase()
                    val rating = cache.getOrPut(movie.id) { getRating(base) ?: return@launch }
                    val updated = MovieViewWithRating(movie, rating)
                    output.getValue(genre)[index] = updated
                    output.mapValues { (_, it) -> it.toList() }
                }
                send(updated)
            }
        }
    }.map(Result.Companion::success)

    private suspend fun getRating(movie: MoviePreview): Byte? {
        val descriptors = detail.get(movie).map {
            val year = Calendar.getInstance().apply { time = it.releasedAt }[Calendar.YEAR]
            arrayOf(
                MovieDescriptor.Original(it.originalName, year),
                MovieDescriptor.Local(it.name, year)
            )
        }.getOrNull() ?: return null
        return rating.get(descriptors = descriptors).max?.value
    }

}