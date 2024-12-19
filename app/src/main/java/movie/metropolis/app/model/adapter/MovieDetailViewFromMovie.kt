package movie.metropolis.app.model.adapter

import movie.cinema.city.Movie
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.PersonView
import movie.metropolis.app.util.toStringComponents
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun MovieDetailViewFromMovie(
    movie: Movie
) = MovieDetailView(movie.id).apply {
    val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)

    name = movie.name.localized
    nameOriginal = movie.name.original
    releasedAt = yearFormat.format(movie.releasedAt)
    duration = movie.length?.toStringComponents().orEmpty()
    countryOfOrigin = movie.originCountry.orEmpty()
    cast = movie.cast.map(::PersonView)
    directors = movie.directors.map(::PersonView)
    description = movie.synopsis
    availableFrom = dateFormat.format(movie.screeningFrom)
    poster = movie.images.maxBy { it.height * it.width }.let(::ImageViewFromMovie)
    backdrop = movie.images.maxBy { it.height * it.width }.let(::ImageViewFromMovie)
    trailer = movie.videos.firstOrNull()?.let(::VideoViewFromMovie)
    rating = null
    url = movie.link.toString()
}