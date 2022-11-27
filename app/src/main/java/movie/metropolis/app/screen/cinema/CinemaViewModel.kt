package movie.metropolis.app.screen.cinema

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import movie.metropolis.app.feature.global.Cinema
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.MovieReference
import movie.metropolis.app.feature.global.Showing
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.screen.getOrThrow
import movie.metropolis.app.screen.listing.toStringComponents
import movie.metropolis.app.screen.map
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CinemaViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val event: EventFeature
) : ViewModel() {

    private val id = handle.get<String>("cinema").orEmpty()
    private val cinemaInternal = flow { emit(event.getCinemas(null)) }
        .map { it.mapCatching { it.first { it.id == id } } }
        .map { it.asLoadable() }
        .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    private val sharedCinema = cinemaInternal
        .dropWhile { !it.isSuccess }
        .map { it.getOrThrow() }

    val selectedDate = MutableStateFlow(Date())
    val cinema = cinemaInternal
        .map { it.map(::CinemaViewFromFeature) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val items = combine(selectedDate, sharedCinema) { date, cinema ->
        event.getShowings(cinema, date).asLoadable()
    }
        .map {
            it.map {
                it.entries.map { (movie, showings) ->
                    MovieBookingViewFromFeature(movie, showings)
                }.sortedByDescending { it.availability.entries.sumOf { it.value.size } }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

}

data class MovieBookingViewFromFeature(
    private val movieRef: MovieReference,
    private val showings: Iterable<Showing>
) : MovieBookingView {

    override val movie: MovieBookingView.Movie
        get() = MovieFromFeature(movieRef)
    override val availability: Map<MovieBookingView.LanguageAndType, List<MovieBookingView.Availability>>
        get() = showings
            .groupBy(::LanguageAndTypeFromFeature)
            .mapValues { (_, it) -> it.map(::AvailabilityFromFeature) }

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

data class CinemaViewFromFeature(
    private val cinema: Cinema
) : CinemaView {
    override val id: String
        get() = cinema.id
    override val name: String
        get() = cinema.name
    override val address: List<String>
        get() = cinema.address.toList()
    override val city: String
        get() = cinema.city
    override val distance: String?
        get() = cinema.distance?.let { "%.2fkm".format(it) }
}