package movie.metropolis.app.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import movie.metropolis.app.feature.global.Cinema
import movie.metropolis.app.feature.global.CinemaWithShowings
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.Location
import movie.metropolis.app.feature.global.Media
import movie.metropolis.app.feature.global.Movie
import movie.metropolis.app.feature.global.MovieDetail
import movie.metropolis.app.feature.global.Showing
import movie.metropolis.app.feature.user.UserFeature
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.LocationSnapshot
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.screen.cinema.CinemaViewFromFeature
import movie.metropolis.app.screen.listing.ImageViewFromFeature
import movie.metropolis.app.screen.listing.VideoViewFromFeature
import movie.metropolis.app.screen.listing.toStringComponents
import movie.metropolis.app.screen.map
import movie.metropolis.app.screen.mapNotNull
import movie.metropolis.app.screen.onFailure
import movie.metropolis.app.screen.onSuccess
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MovieViewModel @Inject constructor(
    handle: SavedStateHandle,
    private val event: EventFeature,
    private val user: UserFeature
) : ViewModel() {

    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
    private val id = handle.get<String>("movie").orEmpty()
    private val movieDetail = flow { emit(event.getDetail(MovieFromId(id))) }
        .map { it.asLoadable() }
        .shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)

    val startDate = movieDetail.map { it.map { it.screeningFrom } }
        .map { loadable -> loadable.map { if (it.before(Date())) Date() else it } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

    val selectedDate = MutableStateFlow(null as Date?)
    val location = MutableStateFlow(
        Location(
            50.0789968000,
            14.4610091000
        ).toSnapshot() as LocationSnapshot?
    )//(null as LocationSnapshot?)

    val detail = movieDetail
        .map { it.map(::MovieDetailViewFromFeature) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

    val trailer: Flow<Loadable<VideoView>> = movieDetail.map {
        it.mapNotNull {
            it.media
                .asSequence()
                .filterIsInstance<Media.Video>()
                .firstOrNull()
                ?.let(::VideoViewFromFeature)
        }
    }
    val poster: Flow<Loadable<ImageView>> = movieDetail.map {
        it.mapNotNull {
            it.media
                .asSequence()
                .filterIsInstance<Media.Image>()
                .sortedByDescending { it.height * it.width }
                .firstOrNull()
                ?.let(::ImageViewFromFeature)
        }
    }

    val showings = combine(selectedDate, location) { date, location ->
        if (date != null && location != null) date to location else null
    }.filterNotNull()
        .flatMapLatest { (date, location) -> loadShowings(date, location) }
        .map { it.map { it.filter { it.availability.isNotEmpty() } } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

    init {
        viewModelScope.launch {
            startDate
                .dropWhile { it.isLoading }
                .map { it.getOrNull() }
                .filterNotNull()
                .map { if (it.before(Date())) Date() else it }
                .collect { selectedDate.compareAndSet(null, it) }
        }
        viewModelScope.launch {
            user.getToken()
                .mapCatching { user.getUser().getOrThrow() }
                .map { it.favorite?.location?.toSnapshot() }
                .onSuccess { location.compareAndSet(null, it) }
        }
    }

    @Suppress("RemoveExplicitTypeArguments")
    private fun loadShowings(date: Date, location: LocationSnapshot) = flow {
        emit(Loadable.loading())
        movieDetail.collect {
            it.onSuccess { detail ->
                emit(event.getShowings(detail, date, location.toLocation()).asLoadable())
            }.onFailure { error ->
                emit(Loadable.failure<CinemaWithShowings>(error))
            }
        }
    }.map {
        it.map {
            it.map { (cinema, showings) ->
                CinemaBookingViewFromResponse(cinema, showings)
            }
        }
    }

    fun onSelectNextDay() {
        val date = selectedDate.value ?: return
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        selectedDate.value = calendar.time
    }

    fun onSelectPreviousDay() {
        val date = selectedDate.value ?: return
        val startDay = startDate.value.getOrNull()?.coerceAtLeast(Date()) ?: return
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        selectedDate.value = calendar.time.takeUnless { it.before(startDay) } ?: selectedDate.value
    }

}

fun LocationSnapshot.toLocation() = Location(latitude, longitude)
fun Location.toSnapshot() = object : LocationSnapshot {
    override val latitude: Double = this@toSnapshot.latitude
    override val longitude: Double = this@toSnapshot.longitude
}

data class CinemaBookingViewFromResponse(
    private val location: Cinema,
    private val booking: Iterable<Showing>
) : CinemaBookingView {
    override val cinema: CinemaView
        get() = CinemaViewFromFeature(location)
    override val availability: Map<String, List<CinemaBookingView.Availability>>
        get() = booking.groupBy { it.label }
            .mapValues { (_, values) -> values.map(::AvailabilityFromFeature) }

    data class AvailabilityFromFeature(
        private val showing: Showing
    ) : CinemaBookingView.Availability {

        private val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

        override val id: String
            get() = showing.id
        override val url: String
            get() = showing.bookingUrl
        override val startsAt: String
            get() = timeFormat.format(showing.startsAt)
        override val isEnabled: Boolean
            get() = showing.isEnabled && Date().before(showing.startsAt)

    }

}

data class MovieDetailViewFromFeature(
    private val movie: MovieDetail
) : MovieDetailView {

    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)

    override val name: String
        get() = movie.name
    override val nameOriginal: String
        get() = movie.originalName
    override val releasedAt: String
        get() = yearFormat.format(movie.releasedAt)
    override val duration: String
        get() = movie.duration.toStringComponents()
    override val countryOfOrigin: String
        get() = movie.countryOfOrigin
    override val cast: List<String>
        get() = movie.cast.toList()
    override val directors: List<String>
        get() = movie.directors.toList()
    override val description: String
        get() = movie.description
    override val availableFrom: String
        get() = dateFormat.format(movie.screeningFrom)
}

data class MovieFromId(
    override val id: String
) : Movie {

    override val name: String = ""
    override val url: String = ""
    override val releasedAt: Date = Date(0)
    override val duration: Duration = 0.seconds

}