package movie.metropolis.app.ui.home

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.cinema.CinemasFacade
import movie.metropolis.app.presentation.listing.ListingFacade
import movie.metropolis.app.presentation.settings.SettingsFacade
import javax.inject.Inject

@Stable
@HiltViewModel
class HomeViewModel @Inject constructor(
    factory: ListingFacade.Factory,
    private val settings: SettingsFacade,
    cinema: CinemasFacade
) : ViewModel() {

    private val upcoming = factory.upcoming()
    private val current = factory.current()

    val state = HomeScreenState()

    init {
        upcoming.get()
            .onEach { upcoming ->
                state.upcoming.clear()
                state.upcoming.addAll(upcoming.items)
            }
            .launchIn(viewModelScope)
        current.get()
            .onEach { current ->
                state.current.clear()
                state.current.addAll(current.items)
            }
            .launchIn(viewModelScope)
        cinema.cinemas(null)
            .onEach {
                state.cinemas.clear()
                state.cinemas.addAll(it)
            }
            .launchIn(viewModelScope)
    }

    fun hide(view: MovieView) = viewModelScope.launch {
        val filters = settings.filters.first()
        settings.setFilters(filters + view.name.substringBefore(":"))
    }

}