package movie.metropolis.app.screen2.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import movie.metropolis.app.model.Calendars
import movie.metropolis.app.presentation.settings.SettingsFacade
import movie.metropolis.app.presentation.settings.SettingsFacade.Companion.addToCalendarFlow
import movie.metropolis.app.presentation.settings.SettingsFacade.Companion.calendarFlow
import movie.metropolis.app.presentation.settings.SettingsFacade.Companion.clipRadiusFlow
import movie.metropolis.app.presentation.settings.SettingsFacade.Companion.filterSeenFlow
import movie.metropolis.app.presentation.settings.SettingsFacade.Companion.onlyMoviesFlow
import movie.metropolis.app.presentation.settings.SettingsFacade.Companion.selectedCalendarFlow
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val facade: SettingsFacade
) : ViewModel() {

    private val refreshKey = Channel<Unit>()

    val calendars = refreshKey.consumeAsFlow()
        .flatMapLatest { facade.calendarFlow }
        .retainStateIn(viewModelScope, Calendars())
    val state = combine(
        facade.filterSeenFlow,
        facade.onlyMoviesFlow,
        facade.addToCalendarFlow,
        facade.clipRadiusFlow,
        facade.selectedCalendarFlow
    ) { filterSeen, onlyMovies, addToCalendar, clipRadius, calendar ->
        SettingsState(
            unseenOnly = filterSeen,
            moviesOnly = onlyMovies,
            tickets = addToCalendar,
            nearbyCinemas = clipRadius.takeUnless { it <= 0 }?.toString().orEmpty(),
            selectedCalendar = calendar
        )
    }.retainStateIn(viewModelScope, SettingsState())

    fun updateState(state: SettingsState) {
        facade.filterSeen = state.unseenOnly
        facade.clipRadius = state.nearbyCinemas.toIntOrNull() ?: 0
        facade.onlyMovies = state.moviesOnly
        facade.selectedCalendar = state.selectedCalendar
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

}