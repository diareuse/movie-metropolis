package movie.metropolis.app.presentation.booking

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.cinema.city.Cinema
import movie.cinema.city.CinemaCity
import movie.cinema.city.Movie
import movie.cinema.city.Ticket
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.PersonView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.model.facade.Image
import java.net.URL
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class BookingFacadeCinemaCity(
    private val cinemaCity: CinemaCity
) : BookingFacade {

    override val bookings: Flow<List<BookingView>> = flow {
        emit(cinemaCity.customers.getTickets().map(::BookingViewFromTicket))
    }

    override suspend fun getShareImage(view: BookingView): Image {
        TODO("Not yet implemented")
    }

}

data class BookingViewFromTicket(
    private val ticket: Ticket
) : BookingView {

    override val id: String
        get() = ticket.id
    override val name: String
        get() = ticket.name
    override val date: String
        get() = ticket.startsAt.toString()
    override val time: String
        get() = ticket.startsAt.toString()
    override val isPaid: Boolean
        get() = ticket.paidAt != null
    override val movie: MovieDetailView
        get() = MovieDetailViewFromMovie(ticket.movie)
    override val cinema: CinemaView
        get() = CinemaViewFromCinema(ticket.cinema)
    override val expired: Boolean
        get() = Date().after(ticket.startsAt + (ticket.movie.length ?: 90.minutes))
    override val hall: String
        get() = ticket.venue.name
    override val seats: List<BookingView.Seat>
        get() = ticket.venue.reservations.map(::Seat)

    override fun origin(): BookingView = this

    private data class Seat(
        private val reservation: Ticket.Reservation
    ) : BookingView.Seat {
        override val row: String
            get() = reservation.row
        override val seat: String
            get() = reservation.seat
    }

}

data class CinemaViewFromCinema(
    private val cinema: Cinema
) : CinemaView {
    override val id: String
        get() = cinema.id
    override val name: String
        get() = cinema.name
    override val address: String
        get() = cinema.address.address.joinToString("\n")
    override val city: String
        get() = cinema.address.city
    override val distance: String?
        get() = null
    override val image: String
        get() = cinema.image.toString()
    override val uri: String
        get() = "geo:${cinema.address.latitude},${cinema.address.longitude}?q=" + Uri.encode(name)
}

data class MovieDetailViewFromMovie(
    private val movie: Movie
) : MovieDetailView {

    override val id: String
        get() = movie.id
    override val name: String
        get() = movie.name.localized
    override val nameOriginal: String
        get() = movie.name.original
    override val releasedAt: String
        get() = movie.releasedAt.toString()
    override val duration: String
        get() = movie.length?.toString().orEmpty()
    override val countryOfOrigin: String
        get() = movie.originCountry.orEmpty()
    override val cast: List<PersonView>
        get() = movie.cast.map(::PersonViewFromName)
    override val directors: List<PersonView>
        get() = movie.directors.map(::PersonViewFromName)
    override val description: String
        get() = movie.synopsis
    override val availableFrom: String
        get() = movie.screeningFrom.toString()
    override val poster: ImageView
        get() = movie.images.maxBy { it.height * it.width }.let(::ImageViewFromMovie)
    override val backdrop: ImageView
        get() = movie.images.maxBy { it.height * it.width }.let(::ImageViewFromMovie)
    override val trailer: VideoView?
        get() = movie.videos.firstOrNull()?.let(::VideoViewFromMovie)
    override val rating: String?
        get() = null

}

data class ImageViewFromMovie(
    private val image: Movie.Image
) : ImageView {
    override val aspectRatio: Float
        get() = 1f * image.width / image.height
    override val url: String
        get() = image.url.toString()
}

data class VideoViewFromMovie(
    private val video: URL
) : VideoView {
    override val url: String
        get() = video.toString()
}

data class PersonViewFromName(
    override val name: String
) : PersonView {
    override val url: String = ""
    override val popularity: Int = 0
    override val image: String = ""
    override val starredInMovies: Int = 0
}

operator fun Date.plus(duration: Duration) = Date(time + duration.inWholeMilliseconds)