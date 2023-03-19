package movie.core.adapter

import movie.core.db.model.BookingSeatsStored
import movie.core.db.model.BookingStored
import movie.core.db.model.CinemaStored
import movie.core.db.model.MovieDetailStored
import movie.core.db.model.MovieMediaStored
import movie.core.db.model.MoviePreviewStored
import movie.core.db.model.MovieReferenceStored
import movie.core.db.model.MovieStored
import movie.core.db.model.ShowingStored
import movie.core.model.Booking
import movie.core.model.Cinema
import movie.core.model.Media
import movie.core.model.Movie
import movie.core.model.MovieDetail
import movie.core.model.MoviePreview
import movie.core.model.MovieReference
import movie.core.model.Showing


fun Cinema.asStored() = CinemaStored(
    id = id,
    name = name,
    description = description,
    city = city,
    address = address,
    latitude = location.latitude,
    longitude = location.longitude,
    image = image
)

fun Movie.asStored() = MovieStored(
    id = id,
    name = name,
    url = url,
    releasedAt = releasedAt,
    durationMillis = duration.inWholeMilliseconds
)

fun MovieDetail.asStored() = MovieDetailStored(
    movie = id,
    originalName = originalName,
    countryOfOrigin = countryOfOrigin,
    cast = cast,
    directors = directors,
    description = description,
    screeningFrom = screeningFrom,
    ageRestrictionUrl = ageRestrictionUrl
)

fun Media.asStored(movie: Movie) = when (this) {
    is Media.Image -> asStored(movie)
    is Media.Video -> asStored(movie)
}

fun Media.Image.asStored(movie: Movie) = MovieMediaStored(
    movie = movie.id,
    width = width,
    height = height,
    url = url,
    type = MovieMediaStored.Type.Image
)

fun Media.Video.asStored(movie: Movie) = MovieMediaStored(
    movie = movie.id,
    width = null,
    height = null,
    url = url,
    type = MovieMediaStored.Type.Video
)

fun MovieReference.asStored() = MovieReferenceStored(
    movie = id,
    poster = posterUrl,
    video = videoUrl
)

fun Showing.asStored(movie: Movie) = ShowingStored(
    id = id,
    cinema = cinema.id,
    startsAt = startsAt,
    bookingUrl = bookingUrl,
    isEnabled = isEnabled,
    auditorium = auditorium,
    language = language,
    types = types.toList(),
    movie = movie.id
)

fun MoviePreview.asStored(upcoming: Boolean) = MoviePreviewStored(
    movie = id,
    screeningFrom = screeningFrom,
    description = description,
    directors = directors,
    cast = cast,
    countryOfOrigin = countryOfOrigin,
    isUpcoming = upcoming,
    genres = genres
)

fun Booking.asStored() = BookingStored(
    id = id,
    name = name,
    startsAt = startsAt,
    paidAt = paidAt,
    movieId = movieId,
    eventId = eventId,
    cinemaId = cinema.id,
    hall = (this as? Booking.Active)?.hall
)

fun Booking.Active.Seat.asStored(booking: Booking) = BookingSeatsStored(
    booking = booking.id,
    row = row,
    seat = seat
)