package movie.metropolis.app.presentation.listing

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.cinema.city.CinemaCity
import movie.cinema.city.Movie
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.ListingView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.presentation.booking.ImageViewFromMovie
import movie.metropolis.app.presentation.booking.VideoViewFromMovie

class ListingFacadeCinemaCity(
    private val future: Boolean,
    private val cinemaCity: CinemaCity
) : ListingFacade {

    override fun get(): Flow<ListingView> = flow {
        val events = cinemaCity.events.getEvents(future)
            .map(::MovieViewFromMovie).toImmutableList().let(::ListingView)
        emit(events)
    }

    override suspend fun toggle(item: MovieView) {
        //TODO("Not yet implemented")
    }

}

data class MovieViewFromMovie(
    private val movie: Movie
) : MovieView {
    override val id: String
        get() = movie.id
    override val name: String
        get() = movie.name.localized
    override val releasedAt: String
        get() = movie.releasedAt.toString()
    override val duration: String
        get() = movie.length?.toString().orEmpty()
    override val availableFrom: String
        get() = movie.screeningFrom.toString()
    override val directors: List<String>
        get() = movie.directors
    override val cast: List<String>
        get() = movie.cast
    override val countryOfOrigin: String
        get() = movie.originCountry.orEmpty()
    override val favorite: Boolean
        get() = false
    override val rating: String?
        get() = null
    override val url: String
        get() = movie.link.toString()
    override val poster: ImageView?
        get() = movie.images.minByOrNull { it.width * it.height }?.let(::ImageViewFromMovie)
    override val posterLarge: ImageView?
        get() = movie.images.maxByOrNull { it.width * it.height }?.let(::ImageViewFromMovie)
    override val video: VideoView?
        get() = movie.videos.firstOrNull()?.let(::VideoViewFromMovie)
}