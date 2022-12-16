package movie.metropolis.app.model.adapter

import movie.core.model.MovieReference
import movie.core.model.Showing
import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.util.toStringComponents
import java.text.SimpleDateFormat
import java.util.Locale

data class MovieBookingViewFromFeature(
    private val movieRef: MovieReference,
    private val showings: Iterable<Showing>
) : MovieBookingView {

    override val movie: MovieBookingView.Movie
        get() = MovieFromFeature(movieRef)
    override val availability: Map<AvailabilityView.Type, List<AvailabilityView>>
        get() = showings
            .groupBy(::LanguageAndTypeFromFeature)
            .mapValues { (_, it) -> it.map(::AvailabilityFromFeature) }

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

}