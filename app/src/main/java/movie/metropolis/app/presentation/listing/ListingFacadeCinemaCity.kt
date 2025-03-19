package movie.metropolis.app.presentation.listing

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.cinema.city.CinemaCity
import movie.metropolis.app.model.ListingView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.ImageViewFromMovie
import movie.metropolis.app.model.adapter.VideoViewFromMovie
import movie.metropolis.app.util.retryOnNetworkError
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

class ListingFacadeCinemaCity(
    private val future: Boolean,
    private val cinemaCity: CinemaCity
) : ListingFacade {

    override fun get(): Flow<ListingView> = flow {
        val events = cinemaCity.events.getEvents(future)
            .map { movie ->
                MovieView(movie.id).apply {
                    name = movie.name.original
                    releasedAt = movie.releasedAt.toString()
                    durationTime = movie.length ?: 0.seconds
                    availableFrom = movie.screeningFrom.toString()
                    directors = movie.directors
                    cast = movie.cast
                    countryOfOrigin = movie.originCountry.orEmpty()
                    url = movie.link.toString()
                    poster =
                        movie.images.minByOrNull { it.width * it.height }?.let(::ImageViewFromMovie)
                    posterLarge =
                        movie.images.maxByOrNull { it.width * it.height }?.let(::ImageViewFromMovie)
                    video = movie.videos.firstOrNull()?.let(::VideoViewFromMovie)
                    val locale = Locale.getDefault()
                    genre =
                        movie.genres.joinToString { it.replaceFirstChar { it.titlecase(locale) } }
                }
            }
            .toImmutableList()
            .let(::ListingView)
        emit(events)
    }.retryOnNetworkError()

}