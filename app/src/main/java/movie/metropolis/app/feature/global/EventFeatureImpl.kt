package movie.metropolis.app.feature.global

import movie.metropolis.app.feature.global.model.CinemaResponse
import movie.metropolis.app.feature.global.model.EventResponse
import movie.metropolis.app.feature.global.model.ExtendedMovieResponse
import movie.metropolis.app.feature.global.model.MovieDetailResponse
import movie.metropolis.app.feature.global.model.MovieResponse
import movie.metropolis.app.feature.global.model.ShowingType
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.time.Duration

internal class EventFeatureImpl(
    private val event: EventService,
    private val cinema: CinemaService
) : EventFeature {

    override suspend fun getShowings(cinema: Cinema, at: Date): Result<MovieWithShowings> {
        return event.getEventsInCinema(cinema.id, at).map { response ->
            val events = response.body.events
            val movies = response.body.movies
            events
                .groupBy { it.movieId }
                .map { (movieId, events) ->
                    val movie = movies.first { it.id == movieId }
                    val reference = MovieFromResponse(movie)
                    val showings = events.map { event -> ShowingFromResponse(event, cinema) }
                    reference to showings
                }
                .toMap()
        }
    }

    override suspend fun getCinemas(location: Location?): Result<Iterable<Cinema>> {
        val cinemas = cinema.getCinemas().map { it.results.map(::CinemaFromResponse) }
        return when (location) {
            null -> cinemas
            else -> {
                val nearby = event.getNearbyCinemas(location.latitude, location.longitude)
                    .map { it.body }
                    .getOrDefault(emptyList())
                if (cinemas.isFailure || nearby.isEmpty()) return cinemas
                val cinemas = cinemas.getOrThrow()
                val mapped = nearby.mapNotNull { nearby ->
                    cinemas.firstOrNull { it.id == nearby.cinemaId }
                        ?.copy(distance = nearby.distanceKms)
                }
                Result.success(mapped)
            }
        }
    }

    override suspend fun getDetail(movie: Movie): Result<MovieDetail> {
        return event.getDetail(movie.id).map { it.body.details }.map(::MovieDetailFromResponse)
    }

    override suspend fun getCurrent(): Result<Iterable<MoviePreview>> {
        return event.getMoviesByType(ShowingType.Current)
            .map { it.body.map(::MoviePreviewFromResponse) }
            .map { it.sortedBy { abs(Date().time - it.screeningFrom.time) } }
    }

    override suspend fun getUpcoming(): Result<Iterable<MoviePreview>> {
        return event.getMoviesByType(ShowingType.Upcoming)
            .map { it.body.map(::MoviePreviewFromResponse) }
            .map { it.sortedBy { abs(Date().time - it.screeningFrom.time) } }
    }
}

internal data class MoviePreviewFromResponse(
    private val response: ExtendedMovieResponse
) : MoviePreview {

    private val metadata = response.metadata[Locale.getDefault()]
        ?: response.metadata[Locale("en", "GB")]
        ?: response.metadata.entries.first().value

    override val id: String
        get() = response.id.key
    override val name: String
        get() = metadata.name
    override val url: String
        get() = response.url
    override val releasedAt: Date
        get() = response.releasedAt
    override val duration: Duration
        get() = response.duration
    override val screeningFrom: Date
        get() = response.screeningFrom
    override val media: Iterable<Media>
        get() = response.media.mapNotNull(::MediaFromResponse)
    override val description: String
        get() = metadata.synopsis
    override val directors: Iterable<String>
        get() = metadata.directors.split(", ", ",")
    override val cast: Iterable<String>
        get() = metadata.cast?.split(", ", ",").orEmpty()
    override val countryOfOrigin: String
        get() = metadata.countryOfOrigin.orEmpty()

    private fun MediaFromResponse(media: ExtendedMovieResponse.Media) = when (media) {
        is ExtendedMovieResponse.Media.Image -> Media.Image(media.width, media.height, media.url)
        is ExtendedMovieResponse.Media.Video -> Media.Video(media.url)
        else -> null
    }
}

internal data class MovieDetailFromResponse(
    private val response: MovieDetailResponse
) : MovieDetail {

    override val id: String
        get() = response.id
    override val name: String
        get() = response.name
    override val url: String
        get() = response.url
    override val releasedAt: Date
        get() = response.releasedAt
    override val duration: Duration
        get() = response.duration
    override val originalName: String
        get() = response.nameOriginal
    override val countryOfOrigin: String
        get() = response.countryOfOrigin
    override val cast: Iterable<String>
        get() = response.cast.split(", ", ",")
    override val directors: Iterable<String>
        get() = response.directors.split(", ", ",")
    override val description: String
        get() = response.synopsis
    override val screeningFrom: Date
        get() = response.screeningFrom
    override val ageRestrictionUrl: String
        get() = response.restrictionUrl
    override val media: Iterable<Media>
        get() = response.media.mapNotNull(::MediaFromResponse)

    private fun MediaFromResponse(media: MovieDetailResponse.Media) = when (media) {
        is MovieDetailResponse.Media.Image -> Media.Image(media.width, media.height, media.url)
        is MovieDetailResponse.Media.Video -> Media.Video(media.url)
        else -> null
    }

}

internal data class CinemaFromResponse(
    private val response: CinemaResponse,
    override val distance: Double? = null
) : Cinema {

    override val id: String
        get() = response.id
    override val name: String
        get() = response.name
    override val description: String
        get() = response.description
    override val city: String
        get() = response.city
    override val address: Iterable<String>
        get() = listOfNotNull(response.addressLine, response.addressLine2)
    override val location: Location
        get() = Location(response.latitude, response.longitude)

}

internal data class MovieFromResponse(
    private val movie: MovieResponse
) : MovieReference {

    override val id: String
        get() = movie.id
    override val name: String
        get() = movie.name
    override val url: String
        get() = movie.url
    override val releasedAt: Date
        get() = movie.releasedAt
    override val duration: Duration
        get() = movie.duration
    override val posterUrl: String
        get() = movie.posterUrl
    override val videoUrl: String
        get() = movie.videoUrl

}

internal data class ShowingFromResponse(
    private val showing: EventResponse,
    override val cinema: Cinema
) : Showing {

    override val id: String
        get() = showing.id
    override val startsAt: Date
        get() = showing.startsAt
    override val bookingUrl: String
        get() = showing.bookingUrl
    override val isEnabled: Boolean
        get() = !showing.soldOut && Date().before(startsAt)
    override val auditorium: String
        get() = showing.auditorium

}