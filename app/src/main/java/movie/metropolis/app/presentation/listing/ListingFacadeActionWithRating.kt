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
import movie.rating.MetadataProvider
import movie.rating.MovieDescriptor
import movie.rating.MovieMetadata
import java.util.Calendar

class ListingFacadeActionWithRating(
    private val origin: ListingFacade.Action,
    private val rating: MetadataProvider,
    private val detail: EventDetailFeature
) : ListingFacade.Action by origin {

    private val cache = mutableMapOf<String, Byte>()

    override val groups = origin.groups.flatMapResult { withRating(it) }

    private fun withRating(items: Map<Genre, List<MovieView>>) = channelFlow {
        send(items)
        val output = items.mapValues { (_, it) -> it.toMutableList() }
        val writeLock = Mutex()
        val movies = items.asSequence()
            .flatMap { it.value }
            .distinctBy { it.id }
        for (movie in movies) launch {
            val base = movie.getBase()
            val rating = getRating(base) ?: return@launch
            val updated = MovieViewWithRating(movie, rating)
            writeLock.withLock {
                output.mapValues { (_, it) ->
                    it.replaceAll { m ->
                        if (m.id == movie.id) updated else m
                    }
                }
            }
            send(output)
        }
    }.map(Result.Companion::success)

    private suspend fun getRating(movie: MoviePreview): Byte? = cache.getOrPut(movie.id) {
        val descriptors = detail.get(movie).map {
            val year = Calendar.getInstance().apply { time = it.releasedAt }[Calendar.YEAR]
            arrayOf(
                MovieDescriptor.Original(it.originalName, year),
                MovieDescriptor.Local(it.name, year)
            )
        }.getOrNull() ?: return null
        descriptors.fold(null as MovieMetadata?) { acc, it ->
            acc ?: rating.get(it)
        }?.rating ?: return null
    }

}