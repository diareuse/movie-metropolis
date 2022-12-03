package movie.metropolis.app.model

import movie.metropolis.app.feature.global.MovieReference
import movie.metropolis.app.feature.global.Showing
import movie.metropolis.app.util.toStringComponents
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

data class MovieBookingViewFromFeature(
    private val movieRef: MovieReference,
    private val showings: Iterable<Showing>
) : MovieBookingView {

    override val movie: MovieBookingView.Movie
        get() = MovieFromFeature(movieRef)
    override val availability: Map<MovieBookingView.LanguageAndType, List<MovieBookingView.Availability>>
        get() = showings
            .groupBy(MovieBookingViewFromFeature::LanguageAndTypeFromFeature)
            .mapValues { (_, it) -> it.map(MovieBookingViewFromFeature::AvailabilityFromFeature) }

    private data class LanguageAndTypeFromFeature(
        override val type: String,
        override val language: String
    ) : MovieBookingView.LanguageAndType {

        constructor(
            showing: Showing
        ) : this(
            showing.type,
            showing.language
        )

    }

    private data class MovieFromFeature(
        private val movie: MovieReference
    ) : MovieBookingView.Movie {

        private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())

        override val id: String
            get() = movie.id
        override val name: String
            get() = movie.name
        override val releasedAt: String
            get() = yearFormat.format(movie.releasedAt)
        override val duration: String
            get() = movie.duration.toStringComponents()
        override val poster: String
            get() = movie.posterUrl
        override val video: String?
            get() = movie.videoUrl
    }

    private data class AvailabilityFromFeature(
        private val showing: Showing
    ) : MovieBookingView.Availability {

        private val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

        override val id: String
            get() = showing.id
        override val url: String
            get() = showing.bookingUrl
        override val startsAt: String
            get() = timeFormat.format(showing.startsAt)
        override val isEnabled: Boolean
            get() = showing.isEnabled
    }

}