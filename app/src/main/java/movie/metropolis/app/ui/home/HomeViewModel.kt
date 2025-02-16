package movie.metropolis.app.ui.home

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.booking.BookingFacade
import movie.metropolis.app.presentation.cinema.CinemasFacade
import movie.metropolis.app.presentation.listing.ListingFacade
import movie.metropolis.app.presentation.profile.ProfileFacade
import movie.metropolis.app.presentation.settings.SettingsFacade
import movie.metropolis.app.util.onEachLaunch
import movie.metropolis.app.util.updateWith
import movie.style.layout.LayoutState
import javax.inject.Inject

@Stable
@HiltViewModel
class HomeViewModel @Inject constructor(
    factory: ListingFacade.Factory,
    private val settings: SettingsFacade,
    cinema: CinemasFacade,
    profile: ProfileFacade,
    booking: BookingFacade
) : ViewModel() {

    private val upcoming = factory.upcoming()
    private val current = factory.current()

    val state = HomeScreenState()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                booking.bookings
                    .catch { state.tickets.state = LayoutState.result(null) }
                    .collect {
                        state.tickets.tickets.updateWith(it)
                        state.tickets.state = LayoutState.result(it.size)
                    }
            }
            launch {
                state.profile.user = profile.runCatching { getUser() }.fold(
                    onSuccess = { LayoutState.result(it) },
                    onFailure = { LayoutState.result(null) }
                )
            }
            launch {
                val items = profile.runCatching { getCinemas() }.getOrDefault(emptyList())
                state.profile.cinemas.updateWith(items)
            }
            launch {
                state.profile.membership = profile.runCatching { getMembership() }.fold(
                    onSuccess = { LayoutState.result(it) },
                    onFailure = { LayoutState.result(null) }
                )
            }
        }
        upcoming.get()
            .catch { it.printStackTrace() }
            .onEachLaunch { upcoming ->
                state.comingSoon.updateWith(upcoming.items)
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
        current.get()
            .catch { it.printStackTrace() }
            .onEachLaunch { current ->
                state.recommended.updateWith(current.items)
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
        cinema.cinemas(null)
            .catch { it.printStackTrace() }
            .onEachLaunch {
                state.cinemas.updateWith(it)
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun hide(view: MovieView) = viewModelScope.launch {
        val filters = settings.filters.first()
        settings.setFilters(filters + view.name.substringBefore(":"))
    }

}