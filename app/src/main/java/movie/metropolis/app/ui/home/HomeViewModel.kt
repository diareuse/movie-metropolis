package movie.metropolis.app.ui.home

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
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
        viewModelScope.launch {
            launch {
                booking.bookings.collect {
                    state.tickets.tickets.updateWith(it)
                    state.tickets.state = LayoutState.result(it.size)
                }
            }
            launch {
                state.profile.user = LayoutState.result(profile.getUser())
            }
            launch {
                state.profile.cinemas.updateWith(profile.getCinemas())
                while (true) {
                    state.profile.cinemas.sortBy { it.distance }
                    delay(1000)
                }
            }
            launch {
                state.profile.membership = LayoutState.result(profile.getMembership())
            }
        }
        upcoming.get()
            .onEachLaunch { upcoming ->
                state.comingSoon.updateWith(upcoming.items)
                while (true) {
                    state.comingSoon.sortByDescending { it.rating }
                    delay(1000)
                }
            }
            .launchIn(viewModelScope)
        current.get()
            .onEachLaunch { current ->
                state.recommended.updateWith(current.items)
                while (true) {
                    state.recommended.sortByDescending { it.rating }
                    delay(1000)
                }
            }
            .launchIn(viewModelScope)
        cinema.cinemas(null)
            .onEachLaunch {
                state.cinemas.updateWith(it)
                while (true) {
                    state.cinemas.sortBy { it.distance }
                    delay(1000)
                }
            }
            .launchIn(viewModelScope)
    }

    fun hide(view: MovieView) = viewModelScope.launch {
        val filters = settings.filters.first()
        settings.setFilters(filters + view.name.substringBefore(":"))
    }

}