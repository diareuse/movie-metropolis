package movie.metropolis.app.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.metropolis.app.feature.global.Cinema
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.Location
import movie.metropolis.app.feature.global.Media
import movie.metropolis.app.feature.global.Movie
import movie.metropolis.app.feature.global.MovieDetail
import movie.metropolis.app.feature.global.Showing
import movie.metropolis.app.feature.global.UserFeature
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.LocationSnapshot
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.screen.cinema.CinemaViewFromFeature
import movie.metropolis.app.screen.detail.MovieFacade.Companion.availableFromFlow
import movie.metropolis.app.screen.detail.MovieFacade.Companion.movieFlow
import movie.metropolis.app.screen.detail.MovieFacade.Companion.posterFlow
import movie.metropolis.app.screen.detail.MovieFacade.Companion.showingsFlow
import movie.metropolis.app.screen.detail.MovieFacade.Companion.trailerFlow
import movie.metropolis.app.screen.listing.ImageViewFromFeature
import movie.metropolis.app.screen.listing.VideoViewFromFeature
import movie.metropolis.app.screen.listing.toStringComponents
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MovieViewModel private constructor(
    private val facade: MovieFacade,
    private val user: UserFeature
) : ViewModel() {

    @Inject
    constructor(
        handle: SavedStateHandle,
        facade: MovieFacade.Factory,
        user: UserFeature
    ) : this(
        facade.create(handle.get<String>("movie").orEmpty()),
        user
    )

    val selectedDate = MutableStateFlow(null as Date?)
    val location = MutableStateFlow(null as android.location.Location?)

    val startDate = facade.availableFromFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val detail = facade.movieFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val trailer = facade.trailerFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val poster = facade.posterFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val showings = facade.showingsFlow(selectedDate.filterNotNull(), location.filterNotNull())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

    init {
        viewModelScope.launch {
            facade.getAvailableFrom()
                .map { if (it.before(Date())) Date() else it }
                .onSuccess { selectedDate.compareAndSet(null, it) }
        }
        viewModelScope.launch {
            user.getUser()
                .map { it.favorite?.location?.toPlatform() }
                .onSuccess { location.compareAndSet(null, it) }
        }
    }

}

interface MovieFacade {

    suspend fun getAvailableFrom(): Result<Date>
    suspend fun getMovie(): Result<MovieDetailView>
    suspend fun getPoster(): Result<ImageView>
    suspend fun getTrailer(): Result<VideoView>
    suspend fun getShowings(
        date: Date,
        latitude: Double,
        longitude: Double
    ): Result<List<CinemaBookingView>>

    fun interface Factory {
        fun create(id: String): MovieFacade
    }

    companion object {

        val MovieFacade.availableFromFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getAvailableFrom().asLoadable())
            }

        val MovieFacade.movieFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getMovie().asLoadable())
            }

        val MovieFacade.posterFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getPoster().asLoadable())
            }

        val MovieFacade.trailerFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getTrailer().asLoadable())
            }

        fun MovieFacade.showingsFlow(
            date: Flow<Date>,
            location: Flow<android.location.Location>
        ) = date
            .combine(location) { date, location -> date to location }
            .flatMapLatest { (date, location) ->
                flow {
                    emit(Loadable.loading())
                    emit(getShowings(date, location.latitude, location.longitude).asLoadable())
                }
            }

    }

}

class MovieFacadeFromFeature(
    private val id: String,
    private val event: EventFeature
) : MovieFacade {

    private val mutex = Mutex(false)
    private var movie: MovieDetail? = null

    override suspend fun getAvailableFrom(): Result<Date> {
        return Result.success(getDetail().screeningFrom)
    }

    override suspend fun getMovie(): Result<MovieDetailView> {
        return Result.success(MovieDetailViewFromFeature(getDetail()))
    }

    override suspend fun getPoster(): Result<ImageView> {
        val image = getDetail().media
            .asSequence()
            .filterIsInstance<Media.Image>()
            .sortedByDescending { it.height * it.width }
            .map(::ImageViewFromFeature)
            .first()
        return Result.success(image)
    }

    override suspend fun getTrailer(): Result<VideoView> {
        val video = getDetail().media
            .asSequence()
            .filterIsInstance<Media.Video>()
            .map(::VideoViewFromFeature)
            .first()
        return Result.success(video)
    }

    override suspend fun getShowings(
        date: Date,
        latitude: Double,
        longitude: Double
    ): Result<List<CinemaBookingView>> {
        val items = event.getShowings(getDetail(), date, Location(latitude, longitude)).getOrThrow()
            .asSequence()
            .map { (cinema, showings) -> CinemaBookingViewFromResponse(cinema, showings) }
            .filter { it.availability.isNotEmpty() }
            .toList()
        return Result.success(items)
    }

    //

    private suspend fun getDetail(): MovieDetail = mutex.withLock {
        movie ?: event.getDetail(MovieFromId(id)).getOrThrow().also {
            movie = it
        }
    }

}

class MovieFacadeRecover(
    private val origin: MovieFacade
) : MovieFacade {

    override suspend fun getAvailableFrom() =
        kotlin.runCatching { origin.getAvailableFrom().getOrThrow() }

    override suspend fun getMovie() =
        kotlin.runCatching { origin.getMovie().getOrThrow() }

    override suspend fun getPoster() =
        kotlin.runCatching { origin.getPoster().getOrThrow() }

    override suspend fun getTrailer() =
        kotlin.runCatching { origin.getTrailer().getOrThrow() }

    override suspend fun getShowings(
        date: Date,
        latitude: Double,
        longitude: Double
    ) = kotlin.runCatching { origin.getShowings(date, latitude, longitude).getOrThrow() }

}

fun LocationSnapshot.toLocation() = Location(latitude, longitude)
fun Location.toPlatform() = android.location.Location(null).also {
    it.latitude = latitude
    it.longitude = longitude
}

data class CinemaBookingViewFromResponse(
    private val location: Cinema,
    private val booking: Iterable<Showing>
) : CinemaBookingView {
    override val cinema: CinemaView
        get() = CinemaViewFromFeature(location)
    override val availability: Map<CinemaBookingView.LanguageAndType, List<CinemaBookingView.Availability>>
        get() = booking.groupBy(::LanguageAndTypeFromFeature)
            .mapValues { (_, values) -> values.map(::AvailabilityFromFeature) }

    private class LanguageAndTypeFromFeature(
        private val item: Showing
    ) : CinemaBookingView.LanguageAndType {
        override val language: String get() = item.language
        override val type: String get() = item.type

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is LanguageAndTypeFromFeature) return false

            if (language != other.language) return false
            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            var result = language.hashCode()
            result = 31 * result + type.hashCode()
            return result
        }

    }

    private data class AvailabilityFromFeature(
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
        get() = movie.countryOfOrigin.orEmpty()
    override val cast: List<String>
        get() = movie.cast.toList()
    override val directors: List<String>
        get() = movie.directors.toList()
    override val description: String
        get() = movie.description
    override val availableFrom: String
        get() = dateFormat.format(movie.screeningFrom)
    override val poster: ImageView?
        get() = movie.media.asSequence().filterIsInstance<Media.Image>()
            .sortedByDescending { it.width * it.height }.firstOrNull()?.let(::ImageViewFromFeature)
    override val trailer: VideoView?
        get() = movie.media.filterIsInstance<Media.Video>().firstOrNull()
            ?.let(::VideoViewFromFeature)
}

data class MovieFromId(
    override val id: String
) : Movie {

    override val name: String = ""
    override val url: String = ""
    override val releasedAt: Date = Date(0)
    override val duration: Duration = 0.seconds

}