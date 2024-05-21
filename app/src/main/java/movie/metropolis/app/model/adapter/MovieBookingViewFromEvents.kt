package movie.metropolis.app.model.adapter

import movie.cinema.city.Movie
import movie.cinema.city.Occurrence
import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.MovieBookingView
import java.util.Date

data class MovieBookingViewFromEvents(
    private val _movie: Movie,
    private val _occurrences: List<Occurrence>
) : MovieBookingView {

    override val movie: MovieBookingView.Movie = MovieImpl()
    override val availability: Map<AvailabilityView.Type, List<AvailabilityView>>
        get() = _occurrences.groupBy(
            keySelector = {
                Type(
                    it.flags.map { it.tag },
                    it.dubbing.toString() + "/" + (it.subtitles?.toString() ?: "-")
                )
            },
            valueTransform = {
                Availability(it)
            }
        )

    private data class Availability(
        private val occurrence: Occurrence
    ) : AvailabilityView {
        override val id: String
            get() = occurrence.id
        override val url: String
            get() = occurrence.booking.toString()
        override val startsAt: String
            get() = occurrence.startsAt.toString()
        override val isEnabled: Boolean
            get() = Date().before(occurrence.startsAt)
    }

    private data class Type(
        override val types: List<String>,
        override val language: String
    ) : AvailabilityView.Type

    private inner class MovieImpl : MovieBookingView.Movie {
        override val id: String
            get() = _movie.id
        override val name: String
            get() = _movie.name.localized
        override val releasedAt: String
            get() = _movie.releasedAt.toString()
        override val duration: String
            get() = _movie.length?.toString().orEmpty()
        override val poster: String
            get() = _movie.images.maxBy { it.height * it.width }.url.toString()
        override val video: String?
            get() = _movie.videos.firstOrNull()?.toString()
    }
}