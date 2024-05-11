package movie.metropolis.app.screen.settings

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import movie.metropolis.app.model.Calendars
import movie.metropolis.app.presentation.settings.SettingsFacade
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@Stable
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val facade: SettingsFacade
) : ViewModel() {

    private val refreshKey = Channel<Unit>()

    val calendars = refreshKey.consumeAsFlow()
        .map { facade.getCalendars() }
        .retainStateIn(viewModelScope, Calendars())
    private val pendingFilter = MutableStateFlow("")
    val state = combine(
        facade.filterSeen,
        facade.onlyMovies,
        facade.addToCalendar,
        facade.clipRadius,
        facade.selectedCalendar
    ) { filterSeen, onlyMovies, addToCalendar, clipRadius, calendar ->
        SettingsState(
            unseenOnly = filterSeen,
            moviesOnly = onlyMovies,
            tickets = addToCalendar,
            nearbyCinemas = clipRadius.takeUnless { it <= 0 }?.toString().orEmpty(),
            selectedCalendar = calendar
        )
    }.combine(facade.filters) { state, filters ->
        state.copy(filters = filters)
    }.combine(pendingFilter) { state, filter ->
        state.copy(pendingFilter = filter)
    }.retainStateIn(viewModelScope, SettingsState())

    fun updateState(state: SettingsState) {
        pendingFilter.value = state.pendingFilter
        facade.setFilterSeen(state.unseenOnly)
        facade.setClipRadius(state.nearbyCinemas.toIntOrNull() ?: 0)
        facade.setOnlyMovies(state.moviesOnly)
        facade.setSelectedCalendar(state.selectedCalendar)
    }

    fun refreshCalendars() {
        viewModelScope.launch out@{
            refreshKey.send(Unit)
            launch {
                calendars.collectLatest {
                    if (it.isNotEmpty()) {
                        this@out.cancel()
                    }
                }
            }
            launch {
                while (true) {
                    delay(100)
                    refreshKey.send(Unit)
                }
            }
        }
    }

    fun addFilter() = viewModelScope.launch {
        val filter = pendingFilter.value
        if (filter.isNotEmpty()) {
            val current = facade.filters.first()
            facade.setFilters(current + filter)
            pendingFilter.value = ""
        }
    }

    fun deleteFilter(filter: String) = viewModelScope.launch {
        val current = facade.filters.first()
        facade.setFilters(current - filter)
    }

}