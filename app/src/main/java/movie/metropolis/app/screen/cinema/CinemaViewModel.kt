package movie.metropolis.app.screen.cinema

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
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

    val selectedDate = MutableStateFlow(Date())
    val cinema = flow { emit(event.getCinemas(null)) }
        .map { it.getOrDefault(emptyList()).firstOrNull { it.id == id } }
        .filterNotNull()
        .shareIn(viewModelScope, SharingStarted.Eagerly)
    val items = selectedDate
        .map { event.getShowings(cinema.first(), it).asLoadable() }
        .map {
            it.map {
                it.entries.map { (movie, showings) ->
                    MovieBookingViewFromFeature(movie, showings)
                }
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
    override val availability: List<MovieBookingView.Availability>
        get() = showings.map(::AvailabilityFromFeature)

    data class MovieFromFeature(
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
        override val video: String
            get() = movie.videoUrl
    }

    data class AvailabilityFromFeature(
        private val showing: Showing
    ) : MovieBookingView.Availability {

        private val timeFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM)

        override val id: String
            get() = showing.id
        override val url: String
            get() = showing.bookingUrl
        override val startsAt: String
            get() = timeFormat.format(showing.startsAt)
        override val isEnabled: Boolean
            get() = showing.isEnabled
        override val cinema: CinemaView
            get() = CinemaViewFromFeature(showing.cinema)
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