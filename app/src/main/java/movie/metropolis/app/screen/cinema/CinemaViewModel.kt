package movie.metropolis.app.screen.cinema

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import movie.metropolis.app.feature.global.Cinema
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.Location
import movie.metropolis.app.feature.global.MovieReference
import movie.metropolis.app.feature.global.Showing
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.screen.cinema.CinemaFacade.Companion.cinemaFlow
import movie.metropolis.app.screen.cinema.CinemaFacade.Companion.showingsFlow
import movie.metropolis.app.screen.listing.toStringComponents
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CinemaViewModel private constructor(
    facade: CinemaFacade
) : ViewModel() {

    @Inject
    constructor(
        handle: SavedStateHandle,
        facade: CinemaFacade.Factory
    ) : this(
        facade.create(handle.get<String>("cinema").orEmpty())
    )

    val selectedDate = MutableStateFlow(Date())
    val cinema = facade.cinemaFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val items = facade.showingsFlow(selectedDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

}

interface CinemaFacade {

    suspend fun getCinema(): Result<CinemaView>
    suspend fun getShowings(date: Date): Result<List<MovieBookingView>>

    fun interface Factory {
        fun create(id: String): CinemaFacade
    }

    companion object {

        val CinemaFacade.cinemaFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getCinema().asLoadable())
            }

        fun CinemaFacade.showingsFlow(date: Flow<Date>) = date.flatMapLatest {
            flow {
                emit(Loadable.loading())
                emit(getShowings(it).asLoadable())
            }
        }

    }

}

class CinemaFacadeFromFeature(
    private val id: String,
    private val event: EventFeature
) : CinemaFacade {

    override suspend fun getCinema(): Result<CinemaView> {
        return event.getCinemas(null)
            .mapCatching { cinemas -> cinemas.first { it.id == id } }
            .map(::CinemaViewFromFeature)
    }

    override suspend fun getShowings(date: Date): Result<List<MovieBookingView>> {
        val cinema = getCinema()
            .getOrThrow()
            .let(::CinemaFromView)

        val result = event.getShowings(cinema, date)
            .getOrThrow()

        return result.entries
            .map { (movie, events) -> MovieBookingViewFromFeature(movie, events) }
            .sortedByDescending { it.availability.entries.sumOf { it.value.size } }
            .let(Result.Companion::success)
    }

}

class CinemaFacadeRecover(
    private val origin: CinemaFacade
) : CinemaFacade {

    override suspend fun getCinema(): Result<CinemaView> {
        return kotlin.runCatching { origin.getCinema().getOrThrow() }
    }

    override suspend fun getShowings(date: Date): Result<List<MovieBookingView>> {
        return kotlin.runCatching { origin.getShowings(date).getOrThrow() }
    }

}

class CinemaFacadeCaching(
    private val origin: CinemaFacade
) : CinemaFacade by origin {

    private var cinema: CinemaView? = null

    override suspend fun getCinema() = cinema?.let(Result.Companion::success)
        ?: origin.getCinema().onSuccess {
            cinema = it
        }

}

data class CinemaFromView(
    private val cinema: CinemaView
) : Cinema {
    override val id: String
        get() = cinema.id
    override val name: String
        get() = cinema.name
    override val description: String
        get() = ""
    override val city: String
        get() = cinema.city
    override val address: Iterable<String>
        get() = cinema.address
    override val location: Location
        get() = Location(0.0, 0.0)
    override val distance: Double?
        get() = null
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